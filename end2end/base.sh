#!/bin/bash
RUNTIME_DIR=/massbit/test_runtime
random=$(echo $RANDOM | md5sum | head -c 5)
ENV=${ENV:random}
blockchain=eth
export PROXY_TAG=v0.1.0
export TEST_CLIENT_TAG=v0.1.0
export FISHERMAN_TAG=v0.1.0
export STAKING_TAG=v0.1-dev
export PORTAL_TAG=v0.1.0-dev
export WEB_TAG=v0.1
export MASSBIT_CHAIN_TAG=v0.1
export NODE_TAG=v0.1.0
export GATEWAY_TAG=v0.1.0
export API_TAG=v0.1.4
export GIT_TAG=v0.1.5
export GWMAN_TAG=v0.1.0
export STAT_TAG=v0.1.0
export MONITOR_TAG=v0.1.0

while docker network ls -q | grep "$find_string"
do
    network_number=$(shuf -i 0-255 -n 1)
    find_string="\"Subnet\": \"172.24.$network_number.0/24\","
    echo $find_string
done

export network_number=$network_number