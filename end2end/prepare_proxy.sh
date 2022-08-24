#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
cat $ROOT_DIR/hosts.template | \
  sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
  sed "s/\[\[PORXY_IP\]\]/$PORXY_IP/g" | \
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
  sed "s/\[\[GIT_IP\]\]/$GIT_IP/g" | \
  sed "s/\[\[API_IP\]\]/$API_IP/g" | \
  sed "s/\[\[STAT_IP\]\]/$STAT_IP/g" | \
  sed "s/\[\[MONITOR_IP\]\]/$MONITOR_IP/g" \
  > $ENV_DIR/hosts
PROXY_DIR=$ENV_DIR/proxy
declare -A hosts
#git clone http://massbit:DaTR__SGr89IjgvcwBtJyg0v_DFySDwI@git.massbitroute.net/massbitroute/ssl.git -b shamu ssl
_IFS=$IFS
while IFS=":" read -r server_name ip
do
  hosts[$server_name]=$ip
done < <(cat $ENV_DIR/hosts)
IFS=$_IFS
cat $ROOT_DIR/docker-proxy/common.conf | \
  sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
  sed "s/\[\[PORXY_IP\]\]/$PORXY_IP/g" | \
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
  sed "s/\[\[GIT_IP\]\]/$GIT_IP/g" | \
  sed "s/\[\[API_IP\]\]/$API_IP/g" | \
  sed "s/\[\[STAT_IP\]\]/$STAT_IP/g" | \
  sed "s/\[\[MONITOR_IP\]\]/$MONITOR_IP/g" \
  > $PROXY_DIR/nginx.conf
domain=massbitroute.net
servers=("api" "portal" "admin-api" "dapi" "staking" "hostmaster" "ns1" "ns2")
for server in ${servers[@]}; do
  server_name=$server.$domain
  echo "Generate server block for $server_name with ip ${hosts[$server_name]}"
  #openssl req -x509 -nodes -days 7300 -newkey rsa:2048 -subj "/C=PE/ST=Lima/L=Lima/O=Acme Inc. /OU=IT Department/CN=$server_name" -keyout $ROOT/docker-proxy/ssl/selfsigned/${server_name}.key -out $ROOT/docker-proxy/ssl/selfsigned/${server_name}.cert
  cat $ROOT_DIR/docker-proxy/server.template | sed "s/\[\[SERVER_NAME\]\]/$server_name/g" | sed "s/\[\[DOMAIN\]\]/$domain/g" |  sed "s/\[\[IP\]\]/${hosts[$server_name]}/g" >> $PROXY_DIR/nginx.conf
done

#domain=fisherman.massbitroute.net
#server_name=scheduler.$domain
#echo "Generate server block for $server_name with ip ${hosts[$server_name]}"
#openssl req -x509 -nodes -days 7300 -newkey rsa:2048 -subj "/C=PE/ST=Lima/L=Lima/O=Acme Inc. /OU=IT Department/CN=$server_name" -keyout $ROOT/docker-proxy/ssl/selfsigned/${server_name}.key -out $ROOT/docker-proxy/ssl/selfsigned/${server_name}.cert
#cat $ROOT_DIR/docker-proxy/server.template | sed "s/\[\[SERVER_NAME\]\]/$server_name/g" | sed "s/\[\[DOMAIN\]\]/$domain/g" |  sed "s/\[\[IP\]\]/${hosts[$server_name]}/g" >> $PROXY_DIR/nginx.conf
