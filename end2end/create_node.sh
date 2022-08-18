#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
NODE_ID=$(cat $ENV_DIR/docker-client/vars/NODE_ID)
NODE_APP_KEY=$(cat $ENV_DIR/docker-client/vars/NODE_APP_KEY)
NODE_DATASOURCE=$(cat $ENV_DIR/docker-client/vars/NODE_DATASOURCE)
USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
cat $ROOT_DIR/node-docker-compose.yaml.template | \
    sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
    sed "s|\[\[GIT_PRIVATE_BRANCH\]\]|$GIT_PRIVATE_BRANCH|g" | \
    sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
		sed "s/\[\[NODE_ID\]\]/$NODE_ID/g" | \
	 	sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" | \
		sed "s/\[\[NODE_TAG\]\]/$NODE_TAG/g" | \
    sed "s/\[\[DATA_URL\]\]/$NODE_DATASOURCE/g" | \
	 	sed "s/\[\[APP_KEY\]\]/$NODE_APP_KEY/g" | \
	 	sed "s/\[\[USER_ID\]\]/$USER_ID/g" > $ENV_DIR/node-docker-compose.yaml
docker-compose -f $ENV_DIR/node-docker-compose.yaml up -d --force-recreate
