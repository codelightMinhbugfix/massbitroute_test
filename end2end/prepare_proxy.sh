#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
cat $ROOT_DIR/hosts.template | sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" > $ENV_DIR/hosts
PROXY_DIR=$ENV_DIR/proxy
declare -A hosts
#git clone http://massbit:DaTR__SGr89IjgvcwBtJyg0v_DFySDwI@git.massbitroute.net/massbitroute/ssl.git -b shamu ssl
_IFS=$IFS
while IFS=":" read -r server_name ip
do
  hosts[$server_name]=$ip
done < <(cat $ENV_DIR/hosts)
IFS=$_IFS
cat $ROOT_DIR/docker-proxy/common.conf | sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" > $PROXY_DIR/nginx.conf
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
