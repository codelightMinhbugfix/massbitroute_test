#!/usr/bin/env bash
source /opt/benchmark/params.sh

_get_dapi_session() {
  #curl -s -X HEAD -I "$dapiURL"   --header 'Content-Type: application/json' | tr -d '\r' | sed -En 's/^location: (.*)/\1/p'
  #return
  #dapiURL=$(curl -s -X HEAD -I "$dapiURL"   --header 'Content-Type: application/json' | grep -i "location:" | cut -d':' -f2- | awk '{print $1}')
  #echo "|$dapiURL|"
  #return
    #Get sessionUrl
    _dapiURL=$(curl -s -X HEAD -I "$1"   --header 'Content-Type: application/json' | awk -F': ' '/^location:/{gsub(/[\s\r]+$/,"",$2);print $2}')
    #Call sessionUrl to get dapiURL with session
    #echo "|$dapiURL|"
    _dapiURL=$(curl -s -X HEAD -I "$_dapiURL"   --header 'Content-Type: application/json' | awk -F': ' '/^location:/{gsub(/[\s\r]+$/,"",$2);print $2}')
    echo "$_dapiURL"
}

_test_data_source() {
    response=$(curl -o /dev/null -s -w "%{http_code}:%{time_total}s\n" --request POST "$1" \
      --header 'Content-Type: application/json' \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            true
        ],
        "id": 1
    }' -k)
    echo "Datasource response: $response"
    # if [[ "$dapi_response_code" != "200" ]]; then
    #   echo "Calling dAPI: Failed"
    #   exit 1
    # fi
}
_test_node_api() {
    response=$(curl -o /dev/null -s -w "%{http_code}:%{time_total}s\n" --request POST "https://$1" \
      --header 'Content-Type: application/json' \
      --header "Host: $2.node.mbr.$domain" \
      --header "X-Api-Key: $3" \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            true
        ],
        "id": 1
    }' -k -vv)
    echo "Node response: $response"
    # if [[ "$dapi_response_code" != "200" ]]; then
    #   echo "Calling dAPI: Failed"
    #   exit 1
    # fi
}

_test_gateway_api() {
    #echo "|$1|$2|$3|"
    #echo "Test host $2.gw.mbr.$domain"
    # curl --request POST "https://$1" \
    #   --header 'Content-Type: application/json' \
    #   --header "Host: $2.gw.mbr.$domain" \
    #   --header "X-Api-Key: $3" \
    #   --data-raw '{
    #     "jsonrpc": "2.0",
    #     "method": "eth_getBlockByNumber",
    #     "params": [
    #         "latest",
    #         true
    #     ],
    #     "id": 1
    # }' -k
    response=$(curl -o /dev/null -s -w "%{http_code}:%{time_total}s\n" --request POST "https://$1" \
      --header 'Content-Type: application/json' \
      --header "Host: $2.gw.mbr.$domain" \
      --header "X-Api-Key: $3" \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            true
        ],
        "id": 1
    }' -k -vv)
    echo "Gateway response: $response"
}
_test_dapi() {
    response=$(curl -o /dev/null -s -w "%{http_code}:%{time_total}s\n" --request POST "$1" \
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
    echo "Dapi response: $response"
    # if [[ "$response_code" != "200" ]]; then
    #   echo "Calling dAPI: Failed"
    #   exit 1
    # fi
    # echo "Calling dAPI: Pass"
}
_benchmark() {
  for rate in "${rates[@]}"
    do
      $wrk_dir/wrk -t$thread -c$connection -d$duration -R$rate --latency -T$timeout -s $wrk_dir/benchmark.lua $1 -- $2 $3 $4 > $output
      latency_row=$(cat $output  | grep -A 4 "Thread Stats   Avg      Stdev     Max   +/- Stdev" | sed -n "2 p")
      IFS='    ' read -ra latency <<< "$latency_row"
      req_sec_row=$(cat $output  | grep -A 4 "Thread Stats   Avg      Stdev     Max   +/- Stdev" | sed -n "3 p")
      IFS='    ' read -ra req_sec <<< "$req_sec_row"
      hdrhistogram50=$(cat $output  | grep -A 4 "Latency Distribution (HdrHistogram - Recorded Latency)" | sed -n "2 p")
      IFS='    ' read -ra hdrhistogram50 <<< "$hdrhistogram50"
      hdrhistogram75=$(cat $output  | grep -A 4 "Latency Distribution (HdrHistogram - Recorded Latency)" | sed -n "3 p")
      IFS='    ' read -ra hdrhistogram75 <<< "$hdrhistogram75"
      hdrhistogram90=$(cat $output  | grep -A 4 "Latency Distribution (HdrHistogram - Recorded Latency)" | sed -n "4 p")
      IFS='    ' read -ra hdrhistogram90 <<< "$hdrhistogram90"
      hdrhistogram99=$(cat $output  | grep -A 4 "Latency Distribution (HdrHistogram - Recorded Latency)" | sed -n "5 p")
      IFS='    ' read -ra hdrhistogram99 <<< "$hdrhistogram99"
      cat $output
      curl 'https://docs.google.com/forms/d/1gzn6skD5MH7D3cyIsv8qcbkbox6QRcxzhkT9AomXE8o/formResponse' --silent >/dev/null \
        --data "entry.721172135=$2&entry.1670770464=$zone&entry.1360977389=$blockchain&entry.1089136036=$duration&entry.770798199=$rate&entry.144814654=${latency[1]}&entry.542037870=${latency[2]}&entry.1977269592=${latency[3]}&entry.1930208986=${hdrhistogram75[1]}&entry.1037348686=${hdrhistogram90[1]}&entry.131454525=${hdrhistogram99[1]}&entry.1567713965=${req_sec[1]}"
    done
}

_run() {

  #echo "Benchmarking datasource $datasourceUrl ..."
  #_benchmark $datasourceUrl datasource

  #_test_data_source $datasourceUrl
  _test_node_api $nodeIp $nodeId $nodeKey
  echo "Node response $?"
  nodeUrl="https://$nodeIp"
  echo "Benchmarking node $nodeUrl ..."
  _benchmark $nodeUrl node $nodeId $nodeKey

  _test_gateway_api $gatewayIp $gatewayId $gatewayKey
  echo "Gateway response $?"
  gatewayUrl="https://$gatewayIp"
  echo "Benchmarking gateway $gatewayUrl ..."
  _benchmark "$gatewayUrl" gateway $gatewayId $gatewayKey

  echo "Get dapiURL with session"
  _dapiURL=$(_get_dapi_session $dapiURL)
  echo "Test dapi $_dapiURL"
  _test_dapi $_dapiURL
  echo "Benchmarking dapi $_dapiURL ..."
  _benchmark "$_dapiURL" dapi
}

$@
