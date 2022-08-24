#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
LENGTH=${1:-1}
OFFSET_IP=${2:-30}
END=$(( $LENGTH+$OFFSET_IP ))
for (( i=$OFFSET_IP; i<$END; i++ ));
do
  printf "Create node with ip %d\n" $i
  #State2: Create node
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _create_node
  NODE_ID=$(cat $ENV_DIR/docker-client/vars/NODE_ID)
  NODE_APP_KEY=$(cat $ENV_DIR/docker-client/vars/NODE_APP_KEY)
  NODE_DATASOURCE=$(cat $ENV_DIR/docker-client/vars/NODE_DATASOURCE)
  USER_ID=$(cat $ENV_DIR/docker-client/vars/USER_ID)
  cat $ENV_DIR/node-docker-compose.yaml.template | \
      sed "s|\[\[NODE_IP\]\]|$i|g" | \
      sed "s/\[\[APP_KEY\]\]/$NODE_APP_KEY/g" | \
      sed "s/\[\[NODE_ID\]\]/$NODE_ID/g" \
      > $ENV_DIR/node-docker-compose-${i}.yaml
  #State3: Create docker node
  docker-compose -f $ENV_DIR/node-docker-compose-${i}.yaml up -d --force-recreate
  #State4 waiting for node approval
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _check_provider_status node approved
  #State4: Stake node
  docker exec mbr_test_client_$network_number /test/scripts/test_main_flow.sh _stake_provider node
done
