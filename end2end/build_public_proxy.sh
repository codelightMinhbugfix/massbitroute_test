#!/bin/bash
domain=massbitroute.net
RUNTIME_DIR=/massbit/test_runtime/
network=${1:-100}
port=${2:-80}
ENV_DIR=$RUNTIME_DIR/$network

server_name=chain.$domain
ip=172.24.$network.205
cat public_proxy/chain.conf.template | sed "s/\[\[SERVER_NAME\]\]/$server_name/g" | sed "s/\[\[DOMAIN\]\]/$domain/g" |  sed "s/\[\[IP\]\]/$ip/g" > $ENV_DIR/public_proxy.conf
hosts=("portal" "dapi")
ips=(201 200)
for i in ${!hosts[@]}; do
  server_name=${hosts[$i]}.$domain
  ip="172.24.$network.${ips[$i]}"
  echo "Generate server block for $server_name with ip $ip"
  #openssl req -x509 -nodes -days 7300 -newkey rsa:2048 -subj "/C=PE/ST=Lima/L=Lima/O=Acme Inc. /OU=IT Department/CN=$server_name" -keyout $ROOT/docker-proxy/ssl/selfsigned/${server_name}.key -out $ROOT/docker-proxy/ssl/selfsigned/${server_name}.cert
  cat public_proxy/public_proxy.template | \
  sed "s/\[\[SERVER_NAME\]\]/$server_name/g" | \
  sed "s/\[\[DOMAIN\]\]/$domain/g" | \
  sed "s/\[\[PORT\]\]/$port/g" | \
  sed "s/\[\[IP\]\]/$ip/g" >> $ENV_DIR/public_proxy.conf
done
