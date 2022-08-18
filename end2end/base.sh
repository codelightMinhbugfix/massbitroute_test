#!/bin/bash
export RUNTIME_DIR=/massbit/test_runtime
#random=$(echo $RANDOM | md5sum | head -c 5)
#ENV=${ENV:random}
export SCHEDULER_AUTHORIZATION=11967d5e9addc5416ea9224eee0e91fc
export MASSBIT_ROUTE_SID=403716b0f58a7d6ddec769f8ca6008f2c1c0cea6
export MASSBIT_ROUTE_PARTNER_ID=fc78b64c5c33f3f270700b0c4d3e7998188035ab
export blockchain=eth
export PROXY_TAG=v0.1.0
export TEST_CLIENT_TAG=v0.1.0
export FISHERMAN_TAG=v0.1.0
export STAKING_TAG=v0.1-dev
export PORTAL_TAG=v0.1.0-dev
export WEB_TAG=v0.1
export MASSBIT_CHAIN_TAG=v0.1
export NODE_TAG=v0.1.0
export GATEWAY_TAG=v0.1.0
export API_TAG=v0.1.7
export GIT_TAG=v0.1.9
export GWMAN_TAG=v0.1.0
export STAT_TAG=v0.1.0
export MONITOR_TAG=v0.1.0
export NETWORK_PREFIX=mbr_test_network
if [ "x$network_number" == "x" ]; then
  while docker network ls | grep "$find_string"
  do
      network_number=$(shuf -i 0-255 -n 1)
      find_string="\"Subnet\": \"172.24.$network_number.0/24\","
      echo $find_string
  done
fi
ENV=$network_number
export ENV_DIR=$RUNTIME_DIR/$ENV
export PROXY_LOGS=$ENV_DIR/proxy/logs
#init docker-compose file
echo '--------------------------'
echo '-----Init environment-----'
echo '--------------------------'
if [ ! -d "$PROXY_LOGS" ]
then
  mkdir -p $PROXY_LOGS
fi
if [ -f "$PROXY_LOGS/proxy_access.log" ]; then
  truncate -s 0 "$PROXY_LOGS/proxy_access.log"
fi
if [ -f "$PROXY_LOGS/proxy_error.log" ]; then
  truncate -s 0 "$PROXY_LOGS/proxy_error.log"
fi
export network_number=$network_number
