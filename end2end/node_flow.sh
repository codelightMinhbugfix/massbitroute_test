#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
network_number=$1
source $ROOT_DIR/base.sh
#Stage1: Login
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _login
#State2: Create node
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_node
#State3: Create docker node
bash -x create_node.sh
#State4 waiting for node approval
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status node approved
#State4: Stake node
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider node
