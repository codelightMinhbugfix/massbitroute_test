#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
source $ROOT_DIR/base.sh

ENV=${ENV:-$random}
ENV=$network_number
ENV_DIR=$RUNTIME_DIR/$ENV
PROXY_LOGS=$ENV_DIR/proxy/logs
#init docker-compose file
echo '--------------------------'
echo '-----Init environment-----'
echo '--------------------------'
if [ ! -d "$PROXY_LOGS" ]
then
  mkdir -p $PROXY_LOGS
fi
rsync -avz migrations $ENV_DIR/
rsync -avz scheduler $ENV_DIR/
rsync -avz fisherman $ENV_DIR/

echo "--------------------------------------------"
echo "Creating network 172.24.$network_number.0/24"
echo "--------------------------------------------"
docker network create -d bridge --gateway "172.24.$network_number.1" --subnet "172.24.$network_number.0/24"   mbr_test_network_$network_number

cat git-docker-compose.yaml.template |  \
     #sed "s/\[\[RUN_ID\]\]/$network_number/g" | \
     sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
     sed "s/\[\[GIT_TAG\]\]/$GIT_TAG/g"  \
    > $ENV_DIR/git-docker-compose.yaml
#cp git-docker-compose.yaml $ENV/git-docker-compose.yaml
#Init docker
docker-compose -f $ENV_DIR/git-docker-compose.yaml up -d --force-recreate
sleep 10
docker exec -it mbr_git_$network_number rm -rf /massbit/massbitroute/app/src/sites/services/git/data
docker exec -it mbr_git_$network_number rm -rf /massbit/massbitroute/app/src/sites/services/git/vars
docker exec -it mbr_git_$network_number /massbit/massbitroute/app/src/sites/services/git/scripts/run _repo_init
#Get PRIVATE_GIT_READ
PRIVATE_GIT_READ=$(docker exec -it mbr_git_$network_number cat /massbit/massbitroute/app/src/sites/services/git/data/env/git.env  | grep GIT_PRIVATE_READ_URL  | cut -d "=" -f 2 | sed "s/'//g" | sed "s|http://||g")
echo $PRIVATE_GIT_READ

#cp docker-compose.yaml $ENV/docker-compose.yaml
cat docker-compose.yaml.template |  \
     sed "s|\[\[VOLUME_LOGS\]\]|$PROXY_LOGS|g" | \
     sed "s/\[\[PROXY_TAG\]\]/$PROXY_TAG/g" | \
     sed "s/\[\[TEST_CLIENT_TAG\]\]/$TEST_CLIENT_TAG/g" | \
     sed "s/\[\[FISHERMAN_TAG\]\]/$FISHERMAN_TAG/g" | \
     #sed "s/\[\[RUN_ID\]\]/$network_number/g" | \
     sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
     sed "s/\[\[MASSBIT_CHAIN_TAG\]\]/$MASSBIT_CHAIN_TAG/g" | \
     sed "s/\[\[STAKING_TAG\]\]/$STAKING_TAG/g" | \
     sed "s/\[\[PORTAL_TAG\]\]/$PORTAL_TAG/g" | \
     sed "s/\[\[WEB_TAG\]\]/$WEB_TAG/g" | \
     sed "s/\[\[CHAIN_TAG\]\]/$CHAIN_TAG/g" | \
     sed "s/\[\[API_TAG\]\]/$API_TAG/g" | \
     sed "s/\[\[GIT_TAG\]\]/$GIT_TAG/g" | \
     sed "s/\[\[GWMAN_TAG\]\]/$GWMAN_TAG/g" | \
     sed "s/\[\[STAT_TAG\]\]/$STAT_TAG/g" | \
     sed "s/\[\[MONITOR_TAG\]\]/$MONITOR_TAG/g" | \
     sed "s|\[\[PRIVATE_GIT_READ\]\]|$PRIVATE_GIT_READ|g" \
    > $ENV_DIR/docker-compose.yaml
docker-compose -f $ENV_DIR/docker-compose.yaml up -d --force-recreate
truncate -s 0 $PROXY_LOGS/proxy_*
sleep 10
#docker exec mbr_api bash -c '/massbit/massbitroute/app/src/sites/services/api/cmd_server start nginx'
docker exec mbr_portal_api_$network_number bash -c 'cd /app; npm run dbm:init'
docker exec mbr_db_$network_number bash -c 'bash /docker-entrypoint-initdb.d/3_init_user.sh'
docker exec mbr_db_$network_number bash -c 'bash /docker-entrypoint-initdb.d/2_clean_node.sh'
#Exec commands in test-client
#Stage1: Login
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _login
#State2: Create node
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_node
#State3: Create docker node
bash -x create_node.sh $ENV_DIR
#State4 waiting for node approval
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status node approved
#State4: State node
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider node
#State5: Create gateway
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_gateway
#State3: Create docker gateway
bash -x create_gateway.sh $ENV_DIR
#State6 waiting for gateway approval
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status gateway approved
#State4: Stake gateway
docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider gateway

touch $ENV_DIR/.deletable
#clean up test environment
