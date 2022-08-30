#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))

#Get PRIVATE_GIT_READ
#PRIVATE_GIT_READ=$(docker exec -it mbr_git_$network_number cat /massbit/massbitroute/app/src/sites/services/git/data/env/git.env  | grep GIT_PRIVATE_READ_URL  | cut -d "=" -f 2 | sed "s/'//g" | sed "s|http://||g")
PRIVATE_GIT_READ=$(docker exec mbr_git_$network_number cat /massbit/massbitroute/app/src/sites/services/git/data/env/git.env  | grep GIT_PRIVATE_READ_URL  | cut -d "=" -f 2 | sed "s/'//g")
echo $PRIVATE_GIT_READ
echo SCHEDULER_AUTHORIZATION=$SCHEDULER_AUTHORIZATION > $ENV_DIR/fisherman/.env_fisherman
#cp docker-compose.yaml $ENV/docker-compose.yaml
cat docker-compose.yaml.template |  \
     sed "s|\[\[ENV_DIR\]\]|$ENV_DIR|g" | \
     sed "s|\[\[ROOT_DIR\]\]|$ROOT_DIR|g" | \
     sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
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
     #Ips
     sed "s/\[\[PROXY_IP\]\]/$PROXY_IP/g" | \
     sed "s/\[\[TEST_CLIENT_IP\]\]/$TEST_CLIENT_IP/g" | \
     sed "s/\[\[MASSBIT_CHAIN_IP\]\]/$MASSBIT_CHAIN_IP/g" | \
     sed "s/\[\[STAKING_IP\]\]/$STAKING_IP/g" | \
     sed "s/\[\[FISHERMAN_SCHEDULER_IP\]\]/$FISHERMAN_SCHEDULER_IP/g" | \
     sed "s/\[\[FISHERMAN_WORKER01_IP\]\]/$FISHERMAN_WORKER01_IP/g" | \
     sed "s/\[\[FISHERMAN_WORKER02_IP\]\]/$FISHERMAN_WORKER02_IP/g" | \
     sed "s/\[\[PORTAL_IP\]\]/$PORTAL_IP/g" | \
     sed "s/\[\[CHAIN_IP\]\]/$CHAIN_IP/g" | \
     sed "s/\[\[WEB_IP\]\]/$WEB_IP/g" | \
     sed "s/\[\[GWMAN_IP\]\]/$GWMAN_IP/g" | \
     sed "s/\[\[POSTGRES_IP\]\]/$POSTGRES_IP/g" | \
     sed "s/\[\[REDIS_IP\]\]/$REDIS_IP/g" | \
     sed "s/\[\[GIT_IP\]\]/$GIT_IP/g" | \
     sed "s/\[\[API_IP\]\]/$API_IP/g" | \
     sed "s/\[\[SCHEDULER_AUTHORIZATION\]\]/$SCHEDULER_AUTHORIZATION/g" | \
     sed "s|\[\[MASSBIT_ROUTE_SID\]\]|$MASSBIT_ROUTE_SID|g" | \
     sed "s|\[\[MASSBIT_ROUTE_PARTNER_ID\]\]|$MASSBIT_ROUTE_PARTNER_ID|g" \
    > $ENV_DIR/docker-compose.yaml.template
cat $ENV_DIR/docker-compose.yaml.template | sed "s|\[\[PRIVATE_GIT_READ\]\]|$PRIVATE_GIT_READ|g" > $ENV_DIR/docker-compose.yaml
docker-compose -f $ENV_DIR/docker-compose.yaml up -d --force-recreate

cat stat-docker-compose.yaml.template |  \
     sed "s|\[\[ENV_DIR\]\]|$ENV_DIR|g" | \
     sed "s|\[\[ROOT_DIR\]\]|$ROOT_DIR|g" | \
     sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
     #sed "s/\[\[RUN_ID\]\]/$network_number/g" | \
     sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
     sed "s/\[\[STAT_TAG\]\]/$STAT_TAG/g" | \
     sed "s/\[\[MONITOR_TAG\]\]/$MONITOR_TAG/g" | \
     #Ips
     sed "s/\[\[PROXY_IP\]\]/$PROXY_IP/g" | \
     sed "s/\[\[TEST_CLIENT_IP\]\]/$TEST_CLIENT_IP/g" | \
     sed "s/\[\[MASSBIT_CHAIN_IP\]\]/$MASSBIT_CHAIN_IP/g" | \
     sed "s/\[\[STAKING_IP\]\]/$STAKING_IP/g" | \
     sed "s/\[\[FISHERMAN_SCHEDULER_IP\]\]/$FISHERMAN_SCHEDULER_IP/g" | \
     sed "s/\[\[FISHERMAN_WORKER01_IP\]\]/$FISHERMAN_WORKER01_IP/g" | \
     sed "s/\[\[FISHERMAN_WORKER02_IP\]\]/$FISHERMAN_WORKER02_IP/g" | \
     sed "s/\[\[PORTAL_IP\]\]/$PORTAL_IP/g" | \
     sed "s/\[\[CHAIN_IP\]\]/$CHAIN_IP/g" | \
     sed "s/\[\[WEB_IP\]\]/$WEB_IP/g" | \
     sed "s/\[\[GWMAN_IP\]\]/$GWMAN_IP/g" | \
     sed "s/\[\[POSTGRES_IP\]\]/$POSTGRES_IP/g" | \
     sed "s/\[\[REDIS_IP\]\]/$REDIS_IP/g" | \
     sed "s/\[\[GIT_IP\]\]/$GIT_IP/g" | \
     sed "s/\[\[API_IP\]\]/$API_IP/g" | \

     sed "s/\[\[NODE_DOT_STAT_IP\]\]/$NODE_DOT_STAT_IP/g" | \
     sed "s/\[\[NODE_ETH_STAT_IP\]\]/$NODE_ETH_STAT_IP/g" | \
     sed "s/\[\[GATEWAY_DOT_STAT_IP\]\]/$GATEWAY_DOT_STAT_IP/g" | \
     sed "s/\[\[GATEWAY_ETH_STAT_IP\]\]/$GATEWAY_ETH_STAT_IP/g" | \

     sed "s|\[\[MASSBIT_ROUTE_SID\]\]|$MASSBIT_ROUTE_SID|g" | \
     sed "s|\[\[MASSBIT_ROUTE_PARTNER_ID\]\]|$MASSBIT_ROUTE_PARTNER_ID|g" \
    > $ENV_DIR/stat-docker-compose.yaml.template
cat $ENV_DIR/stat-docker-compose.yaml.template | sed "s|\[\[PRIVATE_GIT_READ\]\]|$PRIVATE_GIT_READ|g" > $ENV_DIR/stat-docker-compose.yaml
docker-compose -f $ENV_DIR/stat-docker-compose.yaml up -d --force-recreate

sleep 30
#docker exec mbr_api bash -c '/massbit/massbitroute/app/src/sites/services/api/cmd_server start nginx'
docker exec mbr_portal_api_$network_number bash -c 'cd /app; npm run dbm:init'
docker exec mbr_db_$network_number bash -c 'bash /docker-entrypoint-initdb.d/3_init_user.sh'
docker exec mbr_db_$network_number bash -c 'bash /docker-entrypoint-initdb.d/2_clean_node.sh'
