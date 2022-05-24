#!/usr/bin/env bash
ROOT=$(realpath $(dirname $(realpath $0))/)
source $ROOT/params.sh
_IFS=$IFS

_login() {
  bearer=$(curl -s --location --request POST "https://portal.$domain/auth/login" --header 'Content-Type: application/json' \
          --data-raw "{\"username\": \"$USERNAME\", \"password\": \"$PASSWORD\"}"| jq  -r ". | .accessToken")

  if [[ "$bearer" == "null" ]]; then
    echo "Getting JWT token: Failed"
    exit 1
  fi

  userId=$(curl -s --location --request GET "https://portal.$domain/user/info" \
  --header "Authorization: Bearer $bearer" \
  --header 'Content-Type: application/json' | jq  -r ". | .id")
  echo "User ID $userId"
}
#
#_get_node_info node {nodeId}
#_get_node_info gateway {gatewayId}
#
_get_node_info() {
  _login
  res=$(curl -s --location --request GET "https://portal.$domain/mbr/$1/$2" \
    --header "Authorization: Bearer  $bearer" \
    --header 'Content-Type: application/json' \
    | jq -r '. | .id, .appKey, .name, .blockchain, .zone, .status, .geo.ip' \
    | sed -z -z "s/\n/,/g")
  IFS=$',' fields=($res)
  nodeId=fields[1]
  nodeKey=fields[2]
  nodeIp=fields[7]
}
_get_dapi_session() {
    #Get sessionUrl
    _dapiURL=$(curl -s -X HEAD -I "$1"   --header 'Content-Type: application/json' | awk -F': ' '/^location:/{gsub(/[\s\r]+$/,"",$2);print $2}')
    #Call sessionUrl to get dapiURL with session
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
            false
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
            false
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
    response=$(curl -o /dev/null -s -w "%{http_code}:%{time_total}s\n" --request POST "https://$1" \
      --header 'Content-Type: application/json' \
      --header "Host: $2.gw.mbr.$domain" \
      --header "X-Api-Key: $3" \
      --data-raw '{
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [
            "latest",
            false
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
            false
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
# $1 url
# $2 type: node, gateway, dAPI
# $3 rate
# $4 provider id
# $5 provider appkey
# $6 provider name
# $7 blockchain
_single_benchmark() {
  url=$1
  type=$2
  rate=$3
  providerId=$4
  appKey=$5
  providerName=$6
  blockchain=$7
  $wrk_dir/wrk -t$thread -c$connection -d$duration -R$rate --latency -T$timeout -s $wrk_dir/benchmark.lua $url -- $type $providerId $appKey $domain $blockchain > $output
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
  #Request rates
  _rate=$(cat $output | grep "Requests/sec")
  IFS=':' read -ra fields <<< "$_rate"
  requestRate=$(echo ${fields[1]} | tr -d " ")
  #Transfer rates
  _rate=$(cat $output | grep "Transfer/sec")
  IFS=':' read -ra fields <<< "$_rate"
  transferRate=$(echo ${fields[1]} | tr -d " ")

  cat $output
  if [ "$formPerformance" != "x" ]; then
    curl "$formPerformance" --silent >/dev/null \
      --data "entry.721172135=$type&entry.140673538=$providerId&entry.1145125196=$providerName&entry.1670770464=$client&entry.1360977389=$blockchain&entry.1089136036=$duration&entry.770798199=$requestRate&entry.796670045=$transferRate&entry.144814654=${latency[1]}&entry.542037870=${latency[2]}&entry.1977269592=${latency[3]}&entry.1930208986=${hdrhistogram75[1]}&entry.1037348686=${hdrhistogram90[1]}&entry.131454525=${hdrhistogram99[1]}&entry.1567713965=${req_sec[1]}"
  fi
}

# $1 url
# $2 type: node, gateway, dAPI
# $3 provider id
# $4 provider appkey
# $5 provider name
# $6 blockchain
_benchmark() {
  for rate in "${rates[@]}"
    do
      _single_benchmark $1 $2 $rate $3 $4 $5 $6
    done
}
# $1 - Type
_ping_nodes() {
  if [ "$1" == "node" ]; then
    type=node
  else
    type=gateway
  fi
  nodes=$(curl -s --location --request GET "https://portal.$domain/mbr/$type/list/verify" --header "Authorization: $bearerAdmin")
  len=$(echo $nodes | jq length)
  ((len=len-1))
  for i in $( seq 0 $len )
  do
    node=$(echo "$nodes" | jq ".[$i]" | jq ". | .id, .ip, .appKey, .zone, .name, .status" | sed -z "s/\"//g; s/\n/,/g;")
    _IFS=$IFS
    IFS=$',' fields=($node);
    IFS=$_IFS
    nodeZone=${fields[3]^^}
    zone=${zone^^}
    #if [ "$zone" == "$nodeZone" ]; then
    if [ "x${fields[1]}" != "x" ]; then
      url="http://${fields[1]}/ping"
      response=$(timeout 1 curl -s --location --request GET "$url"\
        --header "X-Api-Key: ${fields[2]}" \
        --header "Host: ${fields[0]}.$1.mbr.$domain")
      if [ "x$response" == "xpong" ]; then
        echo "ping $type $url success"
      else
        if [ "x$formPingResult" != "x" ]; then
          curl "$formPingResult"  --silent >/dev/null \
          --data "entry.2056253786=$client&entry.2038576234=$1&entry.814843005=$blockchain&entry.1408740996=$network&entry.1585210645=${fields[4]}&entry.1395047356=${fields[0]}&entry.2030347037=${fields[1]}&entry.1230249318=fail"
        fi
        echo "ping $type $url fail"
        echo ${fields[@]};
      fi
    fi
  done
}
_benchmark_ping() {
  if [ "$1" == "node" ]; then
    type=node
  else
    type=gateway
  fi
  rate=10
  statuses=("staked" "verified")
  for status in ${statuses[@]}; do
    nodes=$(curl -s --location --request GET "https://portal.$domain/mbr/$type/list/verify?status=$status" --header "Authorization: $bearerAdmin")
    len=$(echo $nodes | jq length)
    ((len=len-1))
    for i in $( seq 0 $len )
      do
        node=$(echo "$nodes" | jq ".[$i]" | jq ". | .id, .ip, .appKey, .zone, .name, .blockchain" | sed -z "s/\n/,/g;")
        _IFS=$IFS
        IFS=$',' fields=($node);
        IFS=$_IFS

        zone=${zone^^}
        ip=$(echo ${fields[1]} | sed -z "s/\"//g;")
        appKey=$(echo ${fields[2]} | sed -z "s/\"//g;")
        nodeZone=${fields[3]^^}
        providerId=$(echo ${fields[0]} | sed -z "s/\"//g;")
        providerName=${fields[4]}
        blockchain=$(echo ${fields[5]} | sed -z "s/\"//g;")
        if [ "$zone" == "$nodeZone" ]; then
          echo "Benchmarking node ${fields[@]}"
          url="http://$ip/ping"
          _single_benchmark $url $type $rate $providerId $appKey $providerName $blockchain
        fi
      done
  done
}
# $1 - rate
_benchmark_nodes() {
  statuses=("staked" "verified")
  for status in ${statuses[@]}; do
    https://portal.massbitroute.net/mbr/node/list?limit=6
    nodes=$(curl -s --location --request GET "https://portal.$domain/mbr/node/list/verify?status=$status" --header "Authorization: $bearerAdmin")
    len=$(echo $nodes | jq length)
    ((len=len-1))
    for i in $( seq 0 $len )
    do
      node=$(echo "$nodes" | jq ".[$i]" | jq ". | .id, .appKey, .zone, .name, .blockchain, .ip" | sed -z "s/\n/,/g;" )
      _IFS=$IFS
      IFS=$',' fields=($node);
      IFS=$_IFS
      id=$(echo ${fields[0]} | sed -z "s/\"//g;")
      appKey=$(echo ${fields[1]} | sed -z "s/\"//g;")
      nodeZone=$(echo ${fields[2]^^} | sed -z "s/\"//g;")
      name=${fields[3]}
      blockchain=$(echo ${fields[4]} | sed -z "s/\"//g;")
      ip=$(echo ${fields[5]} | sed -z "s/\"//g;")
      rate=$1
      zone=${zone^^}
      if [[ "$zone" == "$nodeZone" ]]; then
        echo "Benchmarking node ${fields[@]}"
        if [ "x$rate" == "x" ]; then
          _benchmark "http://$ip" node $id $appKey $name $blockchain
        else
          _single_benchmark "http://$ip" node $rate $id $appKey $name $blockchain
        fi
      fi
    done
  done
}
_benchmark_gateways() {
  statuses=("staked" "verified")
  for status in ${statuses[@]}; do
    nodes=$(curl -s --location --request GET "https://portal.$domain/mbr/gateway/list/verify?status=$status" --header "Authorization: $bearerAdmin")
    len=$(echo $nodes | jq length)
    ((len=len-1))
    for i in $( seq 0 $len )
    do
      node=$(echo "$nodes" | jq ".[$i]" | jq ". | .id, .appKey, .zone, .name, .blockchain, .ip" | sed -z "s/\n/,/g;")
      _IFS=$IFS
      IFS=$',' fields=($node);
      IFS=$_IFS
      id=$(echo ${fields[0]} | sed -z "s/\"//g;")
      appKey=$(echo ${fields[1]} | sed -z "s/\"//g;")
      nodeZone=$(echo ${fields[2]^^} | sed -z "s/\"//g;")
      name=${fields[3]}
      blockchain=$(echo ${fields[4]} | sed -z "s/\"//g;")
      ip=$(echo ${fields[5]} | sed -z "s/\"//g;")
      zone=${zone^^}
      if [[ "$zone" == "$nodeZone" ]]; then
        echo "Benchmarking gateway ${fields[@]}"
        _benchmark "http://$ip" gw-$nodeZone $id $appKey $name $blockchain
      fi
    done
  done
}
_benchmark_dapis() {
  #Get random dapi in projectId
  dApis=$(curl -s --location --request GET "https://portal.$domain/mbr/d-apis/list/$projectId?limit=100" \
    --header "Authorization: Bearer $bearer" | jq  -r ". | .dApis")
  len=$(echo $dApis | jq length)
  min=0
  if (( len > 0 )); then
    randomInd=$(($RANDOM % $len + $min))
    dApi=$(echo "$dApis" | jq ".[$randomInd]" | jq ". | .appId, .appKey" | sed -z "s/\"//g; s/\n/,/g; s/,$//g;s/,/.$blockchain-$network.$domain\//g")
    _dapiURL="https://$dApi"
    echo "Test dapi $_dapiURL"
    _test_dapi $_dapiURL
    echo "Benchmarking dapi $_dapiURL ..."
    _benchmark "$_dapiURL" dapi
  fi
}

_run() {
  _login
  #echo "Get dapiURL with session"
  #_dapiURL=$(_get_dapi_session $dapiURL)  #Temporary disable session
  _benchmark_dapis
  _ping_nodes node;
  _ping_nodes gw
  _benchmark_nodes
  _benchmark_gateways

}

$@
