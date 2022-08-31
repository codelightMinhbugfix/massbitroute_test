#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))

echo "--------------------------------------------"
echo "Creating network 172.24.$network_number.0/24"
echo "--------------------------------------------"
docker network create -d bridge --gateway "172.24.$network_number.1" --subnet "172.24.$network_number.0/24"   ${NETWORK_PREFIX}_$network_number

rsync -avz migrations $ENV_DIR/
rsync -avz scheduler $ENV_DIR/
rsync -avz fisherman $ENV_DIR/

#Create node template
cat $ROOT_DIR/templates/node-docker-compose.yaml.template | \
    sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
    sed "s|\[\[GIT_PRIVATE_BRANCH\]\]|$GIT_PRIVATE_BRANCH|g" | \
    sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
		sed "s/\[\[NODE_TAG\]\]/$NODE_TAG/g" | \
    sed "s/\[\[CHAIN_IP\]\]/$CHAIN_IP/g" | \
    sed "s/\[\[PROXY_IP\]\]/$PROXY_IP/g" \
    > $ENV_DIR/node-docker-compose.yaml.template


#Create gateway template
cat $ROOT_DIR/templates/gateway-docker-compose.yaml.template | \
    sed "s|\[\[PROTOCOL\]\]|$PROTOCOL|g" | \
    sed "s|\[\[GIT_PRIVATE_BRANCH\]\]|$GIT_PRIVATE_BRANCH|g" | \
    sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
		sed "s/\[\[GATEWAY_TAG\]\]/$GATEWAY_TAG/g" | \
    sed "s/\[\[CHAIN_IP\]\]/$CHAIN_IP/g" | \
    sed "s/\[\[PROXY_IP\]\]/$PROXY_IP/g" \
	 	> $ENV_DIR/gateway-docker-compose.yaml.template


#Prepare runtime start
cat $ROOT_DIR/runtime_start.sh | \
  sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
  sed "s/\[\[NETWORK_PREFIX\]\]/$NETWORK_PREFIX/g" | \
  sed "s|\[\[MASSBIT_ROUTE_SID\]\]|$MASSBIT_ROUTE_SID|g" | \
  sed "s|\[\[MASSBIT_ROUTE_PARTNER_ID\]\]|$MASSBIT_ROUTE_PARTNER_ID|g" \
	> $ENV_DIR/runtime_start.sh
chmod +x $ENV_DIR/runtime_start.sh

cat $ROOT_DIR/provider_flow.sh | \
  sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" \
  > $ENV_DIR/provider_flow.sh
chmod +x $ENV_DIR/provider_flow.sh

cat $ROOT_DIR/run_test_scenarios.sh | \
  sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" \
  > $ENV_DIR/run_test_scenarios.sh
chmod +x $ENV_DIR/run_test_scenarios.sh
