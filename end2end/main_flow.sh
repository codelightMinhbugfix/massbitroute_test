#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
LOOP=${1:-1}
#NETWORK defined in base
source $ROOT_DIR/base.sh

bash -x prepare_runtime.sh

#Prepare nginx config for proxy
bash -x prepare_proxy.sh

#create git docker
bash -x create_git.sh
#create other dockers: core + portal + admin
bash -x create_docker_compose.sh

#Exec commands in test-client
#Stage1: Login
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _login

#State2: Create node, gateway

bash -x $ENV_DIR/provider_flow.sh $LOOP

touch $ENV_DIR/.deletable
#clean up test environment
#bash -x cleanup.sh $network_number
