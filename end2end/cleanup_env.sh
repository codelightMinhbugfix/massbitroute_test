#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
network_number=$1
source $ROOT_DIR/base.sh
docker-compose -f $ENV_DIR/node-docker-compose.yaml down
docker-compose -f $ENV_DIR/gateway-docker-compose.yaml down
docker-compose -f $ENV_DIR/docker-compose.yaml down
docker-compose -f $ENV_DIR/git-docker-compose.yaml down
#clean runtime data
rm -rf $ENV_DIR/api
rm -rf $ENV_DIR/gateway
rm -rf $ENV_DIR/git
rm -rf $ENV_DIR/gwman
rm -rf $ENV_DIR/monitor
rm -rf $ENV_DIR/node
rm -rf $ENV_DIR/stat
#rm -rf $ENV_DIR/proxy/logs
rm $ENV_DIR/hosts
rm -rf $ENV_DIR
