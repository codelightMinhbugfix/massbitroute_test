#!/bin/bash

#-------------------------------------------
# $1 - DockerId, $2 - providerType, $3 - providerId
#-------------------------------------------
_destroy_provider() {
  DOCKER_ID=$1
  PROVIDER_TYPE="${2,,}"
  PROVIDER_ID=$3
  if [ "$PROVIDER_TYPE" == "node" ]; then
    docker-compose -f $ENV_DIR/node-docker-compose-${DOCKER_ID}.yaml down
  else
    docker-compose -f $ENV_DIR/gateway-docker-compose-${DOCKER_ID}.yaml down
  fi
  docker exec mbr_proxy_$network_number /test/scripts/test_main_flow.sh _check_provider_status node investigate $PROVIDER_ID
  status=$(cat /vars/status/$PROVIDER_ID)
  echo status
}

$@
