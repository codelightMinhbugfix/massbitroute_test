#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
source $ROOT_DIR/base.sh
docker rmi massbit/massbitroute_test_proxy:$PROXY_TAG
docker rmi massbit/massbitroute_test_client:$TEST_CLIENT_TAG
docker rmi massbit/massbitroute_portal:$PORTAL_TAG
docker rmi massbit/massbitroute_fisherman:$FISHERMAN_TAG
docker rmi massbit/massbitroute_api:$API_TAG
docker rmi massbit/massbitroute_git:$GIT_TAG
docker rmi massbit/massbitroute_stat:$STAT_TAG
docker rmi massbit/massbitroute_monitor:$MONITOR_TAG
docker rmi massbit/massbitroute_gateway:$GATEWAY_TAG
docker rmi massbit/massbitroute_node:$NODE_TAG
docker rmi massbit/massbitroute_gwman:$GWMAN_TAG
