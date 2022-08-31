#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
docker-compose -f $ENV_DIR/gateway-docker-compose.yaml down
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway investigate
