#!/bin/bash
sudo apt update
sudo apt install -y jq
sudo mkdir /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_monitor/raw/master/scripts/benchmark/wrk -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_monitor/raw/master/scripts/benchmark/massbit.lua -P /opt/benchmark
#sudo wget https://raw.githubusercontent.com/massbitprotocol/massbitroute_monitor/master/scripts/benchmark/massbit.lua -P /opt/benchmark
sudo chmod +x /opt/benchmark/wrk

#-------------------------------------------
#  Set up supervisor
#-------------------------------------------

#sudo cat > /etc/supervisor/conf.d/mbr_client.conf <<EOL
#[program:mbr_client]
#command=bash /opt/mbr_run.sh
#autostart=true
#autorestart=true
#stderr_logfile=/var/log/mbr_client.err.log
#stdout_logfile=/var/log/mbr_client.out.log
#user=root
#stopasgroup=true
#EOL

sudo cat > /opt/mbr_benchmark.sh <<EOL
#!/bin/bash
zone=[[ZONE]]
nodeId=[[NODE_ID]]
nodeIp=[[NODE_IP]]
nodeKey=[[NODE_KEY]]
gatewayId=[[GATEWAY_ID]]
gatewayIp=[[GATEWAY_IP]]
gatewayKey=[[GATEWAY_KEY]]
dapiURL=[[DAPI_URL]]
thread=[[THREAD]]
connection=[[CONNECTION]]
duration=[[DURATION]]

_get_dapi_session() {
    #Get sessionUrl
    dapiURL=\$(curl -X HEAD -I "\$dapiURL"   --header 'Content-Type: application/json' | awk -F': ' '/^location:/{gsub(/\s*\$/,"",\$2);print \$2}')
    #Call sessionUrl to get dapiURL with session
    dapiURL=\$(curl -X HEAD -I "\$dapiURL"   --header 'Content-Type: application/json' | awk -F': ' '/^location:/{gsub(/\s*\$/,"",\$2);print \$2}')
    return \$dapiURL
}

output=/opt/benchmark/summary.txt
rates=[[REQUEST_RATES]]
_test_dapi() {
    response_code=\$(curl -o /dev/null -s -w "%{http_code}\n" --request POST "\$dapiURL" \
      --header 'Content-Type: application/json' \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            true
        ],
        "id": 1
    }' -L -k)
    return \$response_code
    if [[ "\$dapi_response_code" != "200" ]]; then
      echo "Calling dAPI: Failed"
      exit 1
    fi
    echo "Calling dAPI: Pass"
}
_test_node_api() {
    response_code=\$(curl -o /dev/null -s -w "%{http_code}\n" --request POST "https://\$nodeIp" \
      --header 'Content-Type: application/json' \
      --header "Host: \$nodeId.node.mbr.[[DOMAIN]]" \
      --header "X-Api-Key: \$nodeKey" \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            true
        ],
        "id": 1
    }' -k)
    return \$response_code
    if [[ "\$dapi_response_code" != "200" ]]; then
      echo "Calling dAPI: Failed"
      exit 1
    fi
}
_test_gateway_api() {
    response_code=\$(curl -o /dev/null -s -w "%{http_code}\n" --request POST "https://\$gatewayIp" \
      --header 'Content-Type: application/json' \
      --header "Host: \$gatewayId.gw.mbr.[[DOMAIN]]" \
      --header "X-Api-Key: \$gatewayKey" \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            true
        ],
        "id": 1
    }' -k)
    return \$response_code
}
_benchmark() {
    for rate in "\${rates[@]}"
    do
        /opt/benchmark/wrk -t\$thread -c\$connection -d\$duration -R\$rate -s /opt/benchmark/massbit.lua \$1 -- \$2 \$3 \$4> \$output
        latency_row=\$(cat \$output  | grep -A 4 "Thread Stats   Avg      Stdev     Max   +/- Stdev" | sed -n "2 p")
        IFS='    ' read -ra latency <<< "\$latency_row"
        #latency_Avg=\${latency[1]}
        #latency_Stdev=\${latency[2]}
        #latency_Max=\${latency[3]}
        #latency_Stdev=\${latency[4]}

        req_sec_row=\$(cat \$output  | grep -A 4 "Thread Stats   Avg      Stdev     Max   +/- Stdev" | sed -n "3 p")
        IFS='    ' read -ra req_sec <<< "\$req_sec_row"
        #req_sec_Avg=\${req_sec[1]}
        #req_sec_Stdev=\${req_sec[2]}
        #req_sec_Max=\${req_sec[3]}
        #req_sec_Stdev=\${req_sec[4]}
        cat \$output
        curl 'https://docs.google.com/forms/d/1gzn6skD5MH7D3cyIsv8qcbkbox6QRcxzhkT9AomXE8o/formResponse' --silent >/dev/null \
                --data "entry.721172135=\$2&entry.1670770464=\$zone&entry.1089136036=\$duration&entry.770798199=\$rate&entry.144814654=\${latency[1]}&entry.542037870=\${latency[2]}&entry.1977269592=\${latency[3]}&entry.1709096639=\${latency[4]}&entry.1567713965=\${req_sec[1]}"
    done
}
_run() {
    #Benchmark node
    _test_node_api
    if [[ "\$?" != "200" ]]; then
        echo "Calling Node API: Failed"
    else
        nodeUrl="https://\$nodeIp"
        _benchmark \$nodeUrl node \$nodeId \$nodeKey
    fi

    #Benchmark gateway
    _test_gateway_api
    if [[ "\$?" != "200" ]]; then
        echo "Calling Gateway API: Failed"
    else
        gatewayUrl="https://\$gatewayIp"
        _benchmark \$gatewayUrl gateway \$gatewayId \$gatewayKey
    fi
    #Benchmark dapi
    _test_dapi
    if [[ "\$?" != "200" ]]; then
        echo "Calling \$dapiURL: Failed"
    else
        _get_dapi_session
        dapiUR=\$?
        _benchmark \$dapiURL dapi
    fi
}
\$@

EOL

#random=$(echo $RANDOM | md5sum | head -c 5)
##-------------------------------------------
## Create dAPI
##-------------------------------------------
#create_dapi_response=$(curl -s --location --request POST 'https://portal.massbitroute.dev/mbr/d-apis' \
#  --header "Authorization: Bearer [[BEARER]]" \
#  --header 'Content-Type: application/json' \
#  --data-raw "{
#    \"name\": \"[[PROJECT_NAME]]-$random\",
#    \"projectId\": \"[[PROJECT_ID]]\"
#}")
#create_dapi_status=$(echo $create_dapi_response | jq .status)
#if [[ "$create_dapi_status" != "1" ]]; then
#  echo "Create new dAPI: Failed"
#  exit 1
#fi
#echo "Create new dAPI: Passed"
#
#sleep 30
##-------------------------------------------
## Test call dAPI
##-------------------------------------------
#apiId=$(echo $create_dapi_response | jq -r '. | .entrypoints[0].apiId')
#appKey=$(echo $create_dapi_response | jq -r '. | .appKey')
#dapiURL="https://$apiId.[[BLOCKCHAIN]]-mainnet.massbitroute.dev/$appKey"

sudo chmod +x /opt/mbr_benchmark.sh
/opt/mbr_benchmark.sh _run
