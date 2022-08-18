#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
GATEWAY_ID=$(cat $ENV_DIR/docker-client/vars/GATEWAY_ID)
GATEWAY_APP_KEY=$(cat $ENV_DIR/docker-client/vars/GATEWAY_APP_KEY)
USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
cat $ROOT_DIR/gateway-docker-compose.yaml.template | \
	sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
	sed "s|\[\[GIT_PRIVATE_BRANCH\]\]|$GIT_PRIVATE_BRANCH|g" | \
	sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
  sed "s/\[\[GATEWAY_ID\]\]/$GATEWAY_ID/g" | \
	sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" | \
  sed "s/\[\[GATEWAY_TAG\]\]/$GATEWAY_TAG/g" | \
	sed "s/\[\[APP_KEY\]\]/$GATEWAY_APP_KEY/g" | \
	sed "s/\[\[USER_ID\]\]/$USER_ID/g" > $ENV_DIR/gateway-docker-compose.yaml
docker-compose -f $ENV_DIR/gateway-docker-compose.yaml up -d --force-recreate
