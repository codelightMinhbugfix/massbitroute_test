#!/bin/bash
network_number=$1
source $ROOT_DIR/base.sh
#State5: Create gateway
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_gateway
#State3: Create docker gateway
bash -x create_gateway.sh
#State6 waiting for gateway approval
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway approved
#State4: Stake gateway
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider gateway
