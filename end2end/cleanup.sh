#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
network_number=$1
source $ROOT_DIR/base.sh
docker-compose -f $ENV_DIR/node-docker-compose.yaml down
docker-compose -f $ENV_DIR/gateway-docker-compose.yaml down
docker-compose -f $ENV_DIR/docker-compose.yaml down
docker-compose -f $ENV_DIR/git-docker-compose.yaml down
