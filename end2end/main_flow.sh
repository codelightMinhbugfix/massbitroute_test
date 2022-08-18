#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
#NETWORK defined in base
source $ROOT_DIR/base.sh

rsync -avz migrations $ENV_DIR/
rsync -avz scheduler $ENV_DIR/
rsync -avz fisherman $ENV_DIR/

echo "--------------------------------------------"
echo "Creating network 172.24.$network_number.0/24"
echo "--------------------------------------------"
docker network create -d bridge --gateway "172.24.$network_number.1" --subnet "172.24.$network_number.0/24"   ${NETWORK_PREFIX}_$network_number
#Prepare nginx config for proxy
bash -x prepare_proxy.sh
#Prepare nginx config for test client (fake server block ipv4.icanhazip.com) for gateway
#bash -x prepare_test_client.sh
#create git docker
bash -x create_git.sh
#create other dockers: core + portal + admin
bash -x create_docker_compose.sh
#Exec commands in test-client
#Stage1: Login
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _login
#State2: Create node
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_node
#State3: Create docker node
bash -x create_node.sh
#State4 waiting for node approval
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status node approved
#State4: State node
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider node
#State5: Create gateway
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_gateway
#State3: Create docker gateway
bash -x create_gateway.sh
#State6 waiting for gateway approval
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway approved
#State4: Stake gateway
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider gateway

bash turnoff-gateway.sh

bash turnoff-node.sh

touch $ENV_DIR/.deletable
#clean up test environment
#bash -x cleanup.sh $network_number
