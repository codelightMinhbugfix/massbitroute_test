#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
network_number=[[NETWORK_NUMBER]]
LENGTH=${1:-1}
NODE_OFFSET_IP=30
GATEWAY_OFFSET_IP=130

for (( i=0; i<$LENGTH; i++ ));
do
  NODE_IP=$(( $NODE_OFFSET_IP + $i ))
  GATEWAY_IP=$(( $GATEWAY_OFFSET_IP + $1 ))
  printf "Create node with ip %d, gateway with ip %d\n" $NODE_IP $GATEWAY_IP
  #Create node in the portal
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_node
  NODE_ID=$(cat $ENV_DIR/docker-client/vars/NODE_ID)
  NODE_APP_KEY=$(cat $ENV_DIR/docker-client/vars/NODE_APP_KEY)
  NODE_DATASOURCE=$(cat $ENV_DIR/docker-client/vars/NODE_DATASOURCE)
  USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
  cat $ENV_DIR/node-docker-compose.yaml.template | \
      sed "s|\[\[NODE_IP\]\]|$NODE_IP|g" | \
      sed "s/\[\[APP_KEY\]\]/$NODE_APP_KEY/g" | \
      sed "s/\[\[NODE_ID\]\]/$NODE_ID/g" \
      > $ENV_DIR/node-docker-compose-${NODE_IP}.yaml
  #Create docker node
  docker-compose -f $ENV_DIR/node-docker-compose-${NODE_IP}.yaml up -d --force-recreate
  #Waiting for node approval

  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status node approved
  #Stake node
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider node

  #Create gateway in the portal
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_gateway
	GATEWAY_ID=$(cat $ENV_DIR/docker-client/vars/GATEWAY_ID)
	GATEWAY_APP_KEY=$(cat $ENV_DIR/docker-client/vars/GATEWAY_APP_KEY)
	USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
	cat $ENV_DIR/gateway-docker-compose.yaml.template | \
		sed "s|\[\[GATEWAY_IP\]\]|$GATEWAY_IP|g" | \
		sed "s/\[\[APP_KEY\]\]/$GATEWAY_APP_KEY/g" | \
		sed "s/\[\[GATEWAY_ID\]\]/$GATEWAY_ID/g" \
		> $ENV_DIR/gateway-docker-compose-${GATEWAY_IP}.yaml
	docker-compose -f $ENV_DIR/gateway-docker-compose-${GATEWAY_IP}.yaml up -d --force-recreate
	#Waiting for gateway approval
	docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway approved
	#Stake gateway
	docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider Gateway

  #Turn off gateway
  docker-compose -f $ENV_DIR/gateway-docker-compose-${GATEWAY_IP}.yaml down
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway investigate

  #Turn off node
  docker-compose -f $ENV_DIR/node-docker-compose-${NODE_IP}.yaml down
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status node investigate
done
