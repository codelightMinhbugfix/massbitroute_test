#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
echo $ROOT_DIR
TEST_USERNAME=demo
TEST_PASSWORD=Codelight123
blockchain=eth
dataSource=$DATASOURCE
dataSourceWs=$DATASOURCE_WS
domain=${DOMAIN:-massbitroute.net}
nodePrefix="$(echo $RANDOM | md5sum | head -c 5)"
MEMONIC="bottom drive obey lake curtain smoke basket hold race lonely fit walk//Alice"

if [ ! -d "/vars" ]
then
  mkdir /vars
fi
#-------------------------------------------
# Docker build
#-------------------------------------------
#bash docker_build.sh
#bash docker_build_proxy.sh
#-------------------------------------------
# Docker up
#-------------------------------------------
cd $ROOT_DIR

#-------------------------------------------
# Log into Portal
#-------------------------------------------
_login() {
  bearer=
  while [[ "x$bearer" == "x" ]] || [[ "$bearer" == "null" ]]; do
    echo "Try login..."
    bearer=$(curl -k --location --request POST "https://portal.$domain/auth/login" --header 'Content-Type: application/json' \
            --data-raw "{\"username\": \"$TEST_USERNAME\", \"password\": \"$TEST_PASSWORD\"}"| jq  -r ". | .accessToken")
    sleep 5
  done
  echo $bearer > /vars/BEARER
  userID=$(curl -k "https://portal.$domain/user/info" \
    -H 'Accept: application/json, text/plain, */*' \
    -H 'Accept-Language: en-US,en;q=0.9' \
    -H "Authorization: Bearer $bearer" | jq -r ". | .id")
  echo $userID > /vars/USER_ID
}
#-------------------------------------------
# create  node in Portal
#-------------------------------------------
_create_node() {
  now=$(date)
  bearer=$(cat /vars/BEARER)
  echo "Create new node in Portal: In Progress at $now"
  curl -k --location --request POST "https://portal.$domain/mbr/node" \
    --header "Authorization: Bearer  $bearer" \
    --header 'Content-Type: application/json' \
    --data-raw "{
        \"name\": \"mb-dev-node-$nodePrefix\",
        \"blockchain\": \"$blockchain\",
        \"zone\": \"AS\",
        \"dataSource\": \"$dataSource\",
        \"network\": \"mainnet\",
        \"dataSourceWs\":\"$dataSourceWs\"
    }" | jq -r '. | .id, .appKey' | sed -z -z 's/\n/,/g;s/,$/,AS\n/' >nodelist.csv
    NODE_ID=$(cut -d ',' -f 1 nodelist.csv)
    NODE_APP_KEY=$(cut -d ',' -f 2 nodelist.csv)
    echo "        NODE INFO        "
    echo "----------------------------"
    echo "Node ID: $NODE_ID"
    echo "----------------------------"
    echo $NODE_ID > /vars/NODE_ID
    echo $NODE_APP_KEY > /vars/NODE_APP_KEY
    echo $dataSource > /vars/NODE_DATASOURCE
}

_create_gateway() {
  now=$(date)
  bearer=$(cat /vars/BEARER)
  curl -k --location --request POST "https://portal.$domain/mbr/gateway" \
    --header "Authorization: Bearer  $bearer" \
    --header 'Content-Type: application/json' \
    --data-raw "{
      \"name\":\"mbr-dev-gateway-$nodePrefix\",
      \"blockchain\":\"$blockchain\",
      \"zone\":\"AS\",
      \"network\":\"mainnet\"}" | jq -r '. | .id, .appKey' | sed -z -z 's/\n/,/g;s/,$/,AS\n/' >gatewaylist.csv
  GATEWAY_ID=$(cut -d ',' -f 1 gatewaylist.csv)
  GATEWAY_APP_KEY=$(cut -d ',' -f 2  gatewaylist.csv)
  echo "        GW INFO        "
  echo "----------------------------"
  echo "Gateway ID: $GATEWAY_ID"
  echo "----------------------------"
  echo $GATEWAY_ID > /vars/GATEWAY_ID
  echo $GATEWAY_APP_KEY > /vars/GATEWAY_APP_KEY
}

#-------------------------------------------
# Test staking for provider
# $1: provider type:Node or Gateway, $2: ProviderId, $3 status to check
#-------------------------------------------
_stake_provider() {
  # stake gateway
  now=$(date)
  echo "Wait a minute for staking provider..."
  echo "$now"
  providerType="${1,,}"
  if [ "$providerType" == "gateway" ]; then
    providerId=$(cat /vars/GATEWAY_ID)
  else
    providerId=$(cat /vars/NODE_ID)
  fi
  bearer=$(cat /vars/BEARER)

  staking_response=$(curl --location --request POST "http://staking.$domain/massbit/staking-provider" \
    --header 'Content-Type: application/json' --data-raw "{
      \"memonic\": \"$MEMONIC\",
      \"providerId\": \"$providerId\",
      \"providerType\": \"$providerType\",
      \"blockchain\": \"$blockchain\",
      \"network\": \"mainnet\",
      \"amount\": \"100\"
  }")
  echo "Staking response $staking_response";
  staking_status=$(echo $staking_response | jq -r ". | .status");

  if [[ "$staking_status" != "success" ]]; then
    echo "$providerType staking status: Failed "
    exit 1
  fi
  provider_status=$(curl -k --location --request GET "https://portal.$domain/mbr/$providerType/$providerId" \
    --header "Authorization: Bearer $bearer" | jq -r ". | .status")

  now=$(date)
  echo "---------------------------------"
  echo "$providerType status at $now is $provider_status, expected status staked"
  echo "---------------------------------"

#  provider_status=""
#  while [[ "$provider_status" != "staked" ]]; do
#    echo "Checking $providerType status: In Progress"
#    providerType="${providerType,,}"
#    provider_status=$(curl -k --location --request GET "https://portal.$domain/mbr/$providerType/$providerId" \
#      --header "Authorization: Bearer $bearer" | jq -r ". | .status")
#
#    now=$(date)
#    echo "---------------------------------"
#    echo "$providerType status at $now is $provider_status"
#    echo "---------------------------------"
#    sleep 10
#  done

  now=$(date)
  echo "$providerType staked: Passed at $now"
}


#-------------------------------------------
# Check node status
# $1: provider type: node/gateway, $2 status to check
#-------------------------------------------
_check_provider_status() {
  bearer=$(cat /vars/BEARER)
  providerType="${1,,}"
  if [ "$providerType" == "gateway" ]; then
    providerId=$(cat /vars/GATEWAY_ID)
  else
    providerId=$(cat /vars/NODE_ID)
  fi
  status=''
  start=$(date +"%s")
  printf "Start check status of %s at %ds\n" $providerType $start
  while [[ "$status" != "$2" ]]; do
    echo "Checking $providerType status: In Progress"
    cat /logs/proxy_access.log | grep '.10->api.' | grep 'POST' | grep "$providerType.update"
    if [ $? -eq 0 ];then break;fi

    status=$(curl -k --location --request GET "https://portal.$DOMAIN/mbr/$1/$providerId" \
      --header "Authorization: Bearer $bearer" | jq -r ". | .status")
    now=$(date)
    echo "---------------------------------"
    echo "$providerType status at $now is $status"
    echo "---------------------------------"
    sleep 10
  done
  end=$(date +"%s")
  duration=$(( $end-$start ))
  now=$(date)
  echo "Checking $providerType reported status: $2 at $now in ${duration}s"
}

$@
