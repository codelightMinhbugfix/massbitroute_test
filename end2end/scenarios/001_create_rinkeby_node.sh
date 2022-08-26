#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
echo '---------------------------------------'
echo "Run scenario ${BASH_SOURCE[0]}---------"
echo '---------------------------------------'
blockchain=eth
network=rinkerby
zone=AS
dataSource="http://34.81.232.186:8545"
dataSourceWs="ws://34.81.232.186:8546"
SORT_ID=$(echo $RANDOM | md5sum | head -c 5)
#State2: Create node
docker exec mbr_proxy_$network_number /test/scripts/test_main_flow.sh _create_node $blockchain $network $dataSource $dataSourceWs $SORT_ID
NODE_ID=$(cat $ENV_DIR/proxy/vars/${SORT_ID}/NODE_ID)
NODE_APP_KEY=$(cat $ENV_DIR/proxy/vars/${SORT_ID}/NODE_APP_KEY)
NODE_DATASOURCE=$(cat $ENV_DIR/proxy/vars/${SORT_ID}/NODE_DATASOURCE)
USER_ID=$(cat $ENV_DIR/proxy/vars/USER_ID)
if [ $NODE_ID == "null" ]; then
  echo 'Test failed'
  exit 1
fi
cat $ENV_DIR/node-docker-compose.yaml.template | \
    sed "s|\[\[NODE_IP\]\]|$i|g" | \
    sed "s/\[\[APP_KEY\]\]/$NODE_APP_KEY/g" | \
    sed "s/\[\[NODE_ID\]\]/$NODE_ID/g" | \
    sed "s/\[\[SORT_ID\]\]/$SORT_ID/g" | \
    sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" | \
    sed "s/\[\[NETWORK\]\]/$network/g" | \
    sed "s/\[\[ZONE\]\]/$zone/g" \
    > $ENV_DIR/node-docker-compose-${SORT_ID}.yaml
#State3: Create docker node
docker-compose -f $ENV_DIR/node-docker-compose-${SORT_ID}.yaml up -d --force-recreate
#State4 waiting for node approval
docker exec mbr_proxy_$network_number /test/scripts/test_main_flow.sh _check_provider_status node approved $NODE_ID
#State4: Stake node
docker exec mbr_proxy_$network_number /test/scripts/test_main_flow.sh _stake_provider node $NODE_ID
