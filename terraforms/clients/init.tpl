#!/bin/bash
sudo apt update
sudo apt install -y jq
sudo mkdir /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_monitor/raw/master/scripts/benchmark/wrk -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_monitor/raw/master/scripts/benchmark/massbit.lua -P /opt/benchmark
#sudo wget https://raw.githubusercontent.com/massbitprotocol/massbitroute_monitor/master/scripts/benchmark/massbit.lua -P /opt/benchmark
chmod +x /opt/benchmark/wrk

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
zone=[[ZONE]]
dapiURL=[[DAPI_URL]]
dapiURL=https://88cb8e20-1093-46fb-ab8e-1060a1ab5d2d.eth-mainnet.massbitroute.dev/h13wl_yQfekTRxKef-C_Zg
dapi_response_code=$(curl -o /dev/null -s -w "%{http_code}\n" --request POST $dapiURL \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "jsonrpc": "2.0",
    "method": "eth_getBlockByNumber",
    "params": [
        "latest",
        true
    ],
    "id": 1
}' -L)
if [[ "$dapi_response_code" != "200" ]]; then
  echo "Calling dAPI: Failed"
  exit 1
fi
echo "Calling dAPI: Pass"

thread=10
connection=10
duration=20s
rate=1000
output=/opt/benchmark/summary.txt
/opt/benchmark/wrk -t$thread -c$connection -d$duration -R$rate -s /opt/benchmark/massbit.lua $dapiURL > $output
latency_row=$(cat $output  | grep -A 4 "Thread Stats   Avg      Stdev     Max   +/- Stdev" | sed -n "2 p")
IFS='    ' read -ra latency <<< "$latency_row"
latency_Avg=${latency[1]}
latency_Stdev=${latency[2]}
latency_Max=${latency[3]}
latency_Stdev=${latency[4]}

req_sec_row=$(cat $output  | grep -A 4 "Thread Stats   Avg      Stdev     Max   +/- Stdev" | sed -n "3 p")
IFS='    ' read -ra req_sec <<< "$req_sec_row"
req_sec_Avg=${req_sec[1]}
req_sec_Stdev=${req_sec[2]}
req_sec_Max=${req_sec[3]}
req_sec_Stdev=${req_sec[4]}

#response=$(/opt/benchmark/wrk -t$thread -c$connection -d$duration -R$rate -s /opt/benchmark/massbit.lua $dapiURL)
#echo $response > /opt/benchmark/summary.txt

curl 'https://docs.google.com/forms/d/1gzn6skD5MH7D3cyIsv8qcbkbox6QRcxzhkT9AomXE8o/formResponse' \
        --data "entry.1670770464=$zone&entry.1089136036=$duration&entry.770798199=$thread&entry.660928238=$connection&entry.144814654=${latency[1]}&entry.542037870=${latency[2]}&entry.1977269592=${latency[3]}&entry.1709096639=${latency[4]}&entry.1567713965=${r>
        --silent >/dev/null