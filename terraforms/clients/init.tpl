#!/bin/bash
sudo apt update
sudo apt install -y jq
sudo mkdir /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/scripts/benchmark/wrk -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/scripts/benchmark/benchmark.lua -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/scripts/benchmark/benchmark.sh -P /opt/benchmark
#sudo wget https://raw.githubusercontent.com/massbitprotocol/massbitroute_monitor/master/scripts/benchmark/massbit.lua -P /opt/benchmark
sudo chmod +x /opt/benchmark/wrk
sudo chmod +x /opt/*.sh


sudo cat > /opt/params.sh <<EOL
#!/bin/bash
zone=[[ZONE]]
domain=[[DOMAIN]]
nodeId=[[NODE_ID]]
nodeIp=[[NODE_IP]]
nodeKey=[[NODE_KEY]]
gatewayId=[[GATEWAY_ID]]
gatewayIp=[[GATEWAY_IP]]
gatewayKey=[[GATEWAY_KEY]]
dapiURL=[[DAPI_URL]]
thread=[[THREAD]]
connection=[[CONNECTION]]
duration=[[DURATION]]
rates=[[REQUEST_RATES]]
timeout=3
output=/opt/benchmark/summary.txt
wrk_dir=/opt/benchmark

EOL

sudo chmod +x /opt/benchmark.sh
/opt/benchmark.sh _run
