#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
source $ROOT_DIR/base.sh
ENV_DIR=$RUNTIME_DIR/$1
docker-compose -f $ENV_DIR/node-docker-compose.yaml down
docker-compose -f $ENV_DIR/gateway-docker-compose.yaml down
docker-compose -f $ENV_DIR/docker-compose.yaml down
docker-compose -f $ENV_DIR/git-docker-compose.yaml down
