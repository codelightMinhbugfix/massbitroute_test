#!/bin/bash
export RUNTIME_DIR=/massbit/test_runtime
#random=$(echo $RANDOM | md5sum | head -c 5)
#ENV=${ENV:random}
export PROTOCOL=http
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
export API_TAG=v0.1.11
export GIT_TAG=v0.1.9
export GWMAN_TAG=v0.1.0
export STAT_TAG=v0.1.0
export MONITOR_TAG=v0.1.0

export NODE_TAG=v0.1.0
export GATEWAY_TAG=v0.1.0

export GIT_PRIVATE_BRANCH=shamu
export NETWORK_PREFIX=mbr_test_network

#IPs: 30-230: Node and gateway
#Ips: 1
export PROXY_IP=254
export TEST_CLIENT_IP=253
export CHAIN_IP=20

export STAKING_IP=12
export FISHERMAN_SCHEDULER_IP=15
export FISHERMAN_WORKER01_IP=16
export FISHERMAN_WORKER02_IP=17
export PORTAL_IP=10
export WEB_IP=11
export POSTGRES_IP=13
export REDIS_IP=14

export GWMAN_IP=2
export GIT_IP=5
export API_IP=6
export STAT_IP=7
export MONITOR_IP=8


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
