#!/bin/bash
sudo apt update
sudo apt install -y jq atop
sudo echo 2000000 >/proc/sys/fs/nr_open
sudo echo 2000000 >/proc/sys/fs/file-max
sudo ulimit -n 2000000
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/tools/wrk/wrk -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/tools/wrk/benchmark.lua -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/tools/wrk/benchmark.sh -P /opt/benchmark
#sudo wget https://raw.githubusercontent.com/massbitprotocol/massbitroute_monitor/master/tools/wrk/massbit.lua -P /opt/benchmark
sudo chmod +x /opt/benchmark/wrk

sudo cat > /opt/benchmark/params.sh <<EOL
#!/bin/bash
bearer=[[BEARER]]
zone=[[ZONE]]
blockchain=[[BLOCKCHAIN]]
domain=[[DOMAIN]]
nodeId=[[NODE_ID]]
nodeIp=[[NODE_IP]]
nodeKey=[[NODE_KEY]]
gatewayId=[[GATEWAY_ID]]
gatewayIp=[[GATEWAY_IP]]
gatewayKey=[[GATEWAY_KEY]]
projectId=[[PROJECT_ID]]
dapiURL=[[DAPI_URL]]
thread=[[THREAD]]
connection=[[CONNECTION]]
duration=[[DURATION]]
rates=[[REQUEST_RATES]]
timeout=3
output=/opt/benchmark/summary.txt
wrk_dir=/opt/benchmark

EOL
sudo chmod +x /opt/benchmark/*.sh
while true;
do
/opt/benchmark/benchmark.sh _run >> /opt/benchmark/benchmark.log
done
