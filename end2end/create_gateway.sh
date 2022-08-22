#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
LENGTH=${1:-1}
OFFSET_IP=${2:-130}
END=$(( $LENGTH+$OFFSET_IP ))
for (( i=$OFFSET_IP; i<$END; i++ ));
do
  printf "Create gateway with ip %d\n" $i
	#State5: Create gateway
	docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_gateway
	GATEWAY_ID=$(cat $ENV_DIR/docker-client/vars/GATEWAY_ID)
	GATEWAY_APP_KEY=$(cat $ENV_DIR/docker-client/vars/GATEWAY_APP_KEY)
	USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
	cat $ENV_DIR/gateway-docker-compose.yaml.template | \
		sed "s|\[\[GATEWAY_IP\]\]|$i|g" | \
		sed "s/\[\[APP_KEY\]\]/$GATEWAY_APP_KEY/g" | \
		sed "s/\[\[GATEWAY_ID\]\]/$GATEWAY_ID/g" \
		> $ENV_DIR/gateway-docker-compose-${i}.yaml
	docker-compose -f $ENV_DIR/gateway-docker-compose-${i}.yaml up -d --force-recreate
	#State6 waiting for gateway approval
	docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway approved
	#State4: Stake gateway
	docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider Gateway
done
