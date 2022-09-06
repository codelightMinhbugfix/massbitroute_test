#!/bin/bash
export RUNTIME_DIR=/massbit/test_runtime
export network_number=$(cat .vars/NETWORK_NUMBER)
export gateway=$(cat .vars/GATEWAY)
export node=$(cat .vars/NODE)
export stat=$(cat .vars/STAT)
export git=$(cat .vars/GIT)
export chain=$(cat .vars/CHAIN)
export fisherman=$(cat .vars/FISHERMAN)
export staking=$(cat .vars/STAKING)
export portal=$(cat .vars/PORTAL)
export web=$(cat .vars/WEB)
export api=$(cat .vars/API)
export gwman=$(cat .vars/GWMAN)
export session=$(cat .vars/SESSION)

export PROXY_TAG=${proxy:-v0.1.0}
export TEST_CLIENT_TAG=${test_client:-v0.1.0}
#export FISHERMAN_TAG=${fisherman:-v0.1.0}
export FISHERMAN_TAG=v0.1.0-dev
export STAKING_TAG=${STAKING_TAG:-v0.1-dev}
#export PORTAL_TAG=v0.1.0-test
export PORTAL_TAG=${portal:-v0.1.0-test}
export WEB_TAG=${web:-v0.1}
export MASSBIT_CHAIN_TAG=${chain:-v0.1}
export SESSION_TAG=${session:-v0.1.0}
export API_TAG=${api:-v0.1.11}
export GIT_TAG=${git:-v0.1.11}
export GWMAN_TAG=${gwman:-v0.1.4}
export STAT_TAG=${stat:-v0.1.2}
export MONITOR_TAG=${monitor:-v0.1.0}
export NODE_TAG=${node:-v0.1.3}
export GATEWAY_TAG=${gateway:-v0.1.4}

export GIT_PRIVATE_BRANCH=shamu
export PROTOCOL=http
export SCHEDULER_AUTHORIZATION=SomeSecretString
export MASSBIT_ROUTE_SID=403716b0f58a7d6ddec769f8ca6008f2c1c0cea6
export MASSBIT_ROUTE_PARTNER_ID=fc78b64c5c33f3f270700b0c4d3e7998188035ab
export GIT_PRIVATE_BRANCH=shamu
export NETWORK_PREFIX=mbr_test_network
#IPs: 30-230: Node and gateway

export PROXY_IP=254
export TEST_CLIENT_IP=253
export CHAIN_IP=20

export PORTAL_IP=10
export WEB_IP=11
export STAKING_IP=12
export POSTGRES_IP=13
export REDIS_IP=14
export FISHERMAN_SCHEDULER_IP=15
export FISHERMAN_WORKER01_IP=16
export FISHERMAN_WORKER02_IP=17

export GWMAN_IP=2
export GIT_IP=5
export API_IP=6
export SESSION_IP=7

export START_IP=20

export MONITOR_IP=50
export NODE_DOT_STAT_IP=21
export NODE_ETH_STAT_IP=22
export NODE_DOT_MONITOR_IP=23
export NODE_ETH_MONITOR_IP=24
export GATEWAY_DOT_STAT_IP=25
export GATEWAY_ETH_STAT_IP=26
export GATEWAY_DOT_MONITOR_IP=27
export GATEWAY_ETH_MONITOR_IP=28


# if [ "x$network_number" == "x" ]; then
#   while docker network ls | grep "$find_string"
#   do
#       network_number=$(shuf -i 0-255 -n 1)
#       find_string="\"Subnet\": \"172.24.$network_number.0/24\","
#       echo $find_string
#   done
# fi
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

declare -A blockchains=()
blockchains["eth"]="mainnet rinkerby"
blockchains["dot"]="mainnet"
export blockchains
export domain=massbitroute.net
