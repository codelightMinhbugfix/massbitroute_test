#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
cat $ROOT_DIR/docker-client/common.conf | sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" > $PROXY_DIR/nginx.conf
