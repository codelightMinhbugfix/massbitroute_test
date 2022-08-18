#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
NODE_ID=$(cat $ENV_DIR/docker-client/vars/NODE_ID)
NODE_APP_KEY=$(cat $ENV_DIR/docker-client/vars/NODE_APP_KEY)
NODE_DATASOURCE=$(cat $ENV_DIR/docker-client/vars/NODE_DATASOURCE)
USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
cat $ROOT_DIR/node-docker-compose.yaml.template | \
     	sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" > $ENV_DIR/node-docker-compose.yaml

echo "------------------------------------------------------------------------------"
echo "To create Massbit Gateway, run the following command: "
echo "docker-compose -f $ENV_DIR/node-docker-compose.yaml up -d --force-recreate"
echo "------------------------------------------------------------------------------"

