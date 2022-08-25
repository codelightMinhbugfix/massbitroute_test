#!/bin/bash
echo 'Run scenario call apis'
exit 0;
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
ENV_DIR=${ENV_DIR:-.}
network_number=[[NETWORK_NUMBER]]

docker exec mbr_proxy_$network_number /test/scripts/test_main_flow.sh _login

docker exec mbr_proxy_$network_number /test/scripts/test_dapi.sh _create_project

docker exec mbr_proxy_$network_number /test/scripts/test_dapi.sh _create_api

#Execute block chain testing
docker exec mbr_proxy_$network_number /test/scripts/test_dapi.sh _execute_apis_testing
