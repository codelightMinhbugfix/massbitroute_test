#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
GATEWAY_ID=$(cat $ENV_DIR/docker-client/vars/GATEWAY_ID)
GATEWAY_APP_KEY=$(cat $ENV_DIR/docker-client/vars/GATEWAY_APP_KEY)
USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
cat $ROOT_DIR/gateway-docker-compose.yaml.template | \
	sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" > $ENV_DIR/gateway-docker-compose.yaml

echo "------------------------------------------------------------------------------"
echo "To create Massbit Gateway, run the following command: "
echo "docker-compose -f $ENV_DIR/gateway-docker-compose.yaml up -d --force-recreate"
echo "------------------------------------------------------------------------------"
