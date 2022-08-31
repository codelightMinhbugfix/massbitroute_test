#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))

#Get PRIVATE_GIT_READ
#PRIVATE_GIT_READ=$(docker exec -it mbr_git_$network_number cat /massbit/massbitroute/app/src/sites/services/git/data/env/git.env  | grep GIT_PRIVATE_READ_URL  | cut -d "=" -f 2 | sed "s/'//g" | sed "s|http://||g")
#PRIVATE_GIT_READ=$(docker exec mbr_git_$network_number cat /massbit/massbitroute/app/src/sites/services/git/data/env/git.env  | grep GIT_PRIVATE_READ_URL  | cut -d "=" -f 2 | sed "s/'//g")
#echo $PRIVATE_GIT_READ

mkdir -p $ENV_DIR/stat/docker-compose/
mkdir -p $ENV_DIR/stat/tpl/
cat network-docker-compose.yaml.template | \
  sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" > $ENV_DIR/stat/docker-compose/network.yaml
declare -A blockchains=()
blockchains["eth"]="mainnet rinkerby"
blockchains["dot"]="mainnet"
STAT_IP=20
docker_compose_files=" -f $ENV_DIR/stat/docker-compose/network.yaml"
types="gateway node"
for PROVIDER_TYPE in $types
do
  for BLOCKCHAIN in ${!blockchains[@]}
  do
     networks=(${blockchains[$BLOCKCHAIN]});
     for NETWORK in ${networks[@]}
     do
       STAT_IP=$(( $STAT_IP + 1 ))
       cat stat-docker-compose.yaml.template |  \
            sed "s|\[\[ENV_DIR\]\]|$ENV_DIR|g" | \
            sed "s|\[\[ROOT_DIR\]\]|$ROOT_DIR|g" | \
            sed "s|\[\[PROVIDER_TYPE\]\]|$PROVIDER_TYPE|g" | \
            sed "s|\[\[BLOCKCHAIN\]\]|$BLOCKCHAIN|g" | \
            sed "s|\[\[NETWORK\]\]|$NETWORK|g" | \
            sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
            sed "s|\[\[GIT_PRIVATE_BRANCH\]\]|$GIT_PRIVATE_BRANCH|g" | \
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

            sed "s/\[\[STAT_IP\]\]/$STAT_IP/g" | \

            sed "s|\[\[MASSBIT_ROUTE_SID\]\]|$MASSBIT_ROUTE_SID|g" | \
            sed "s|\[\[MASSBIT_ROUTE_PARTNER_ID\]\]|$MASSBIT_ROUTE_PARTNER_ID|g" \
           > $ENV_DIR/stat/tpl/${PROVIDER_TYPE}_${BLOCKCHAIN}_${NETWORK}.yaml.template
           cat $ENV_DIR/stat/tpl/${PROVIDER_TYPE}_${BLOCKCHAIN}_${NETWORK}.yaml.template | sed "s|\[\[PRIVATE_GIT_READ\]\]|$PRIVATE_GIT_READ|g" > $ENV_DIR/stat/docker-compose/${PROVIDER_TYPE}_${BLOCKCHAIN}_${NETWORK}.yaml
           docker_compose_files="$docker_compose_files -f $ENV_DIR/stat/docker-compose/${PROVIDER_TYPE}_${BLOCKCHAIN}_${NETWORK}.yaml"
     done
     #echo ${networks[@]}
     #echo "["$key"]:["${blockchains[$key]}"]"
  done
done

docker-compose $docker_compose_files up -d --force-recreate
