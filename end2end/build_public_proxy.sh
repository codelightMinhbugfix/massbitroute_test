#!/bin/bash
domain=massbitroute.net
RUNTIME_DIR=/massbit/test_runtime/
network=${1:-100}
ENV_DIR=$RUNTIME_DIR/$network

hosts=("portal" "dapi" "chain")
ips=(201 200 205)

echo '' > $ENV_DIR/public_proxy.conf

for i in ${!hosts[@]}; do
  server_name=${hosts[$i]}.$domain
  ip="172.24.$network.${ips[$i]}"
  echo "Generate server block for $server_name with ip $ip"
  #openssl req -x509 -nodes -days 7300 -newkey rsa:2048 -subj "/C=PE/ST=Lima/L=Lima/O=Acme Inc. /OU=IT Department/CN=$server_name" -keyout $ROOT/docker-proxy/ssl/selfsigned/${server_name}.key -out $ROOT/docker-proxy/ssl/selfsigned/${server_name}.cert
  cat public_proxy.template | sed "s/\[\[SERVER_NAME\]\]/$server_name/g" | sed "s/\[\[DOMAIN\]\]/$domain/g" |  sed "s/\[\[IP\]\]/$ip/g" >> $ENV_DIR/public_proxy.conf
done
