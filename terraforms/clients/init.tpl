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
USERNAME=[[USERNAME]]
PASSWORD=[[PASSWORD]]
bearer=[[BEARER]]
bearerAdmin=[[BEARERADMIN]]
client=[[CLIENT]]
zone=[[ZONE]]
blockchain=[[BLOCKCHAIN]]
network=[[NETWORK]]
domain=[[DOMAIN]]
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
while true;
do
rm /opt/benchmark/benchmark.lua
rm /opt/benchmark/benchmark.sh

sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/tools/wrk/benchmark.lua -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_test/raw/master/tools/wrk/benchmark.sh -P /opt/benchmark
sudo chmod +x /opt/benchmark/*.sh
/opt/benchmark/benchmark.sh _run >> /opt/benchmark/benchmark.log
sleep 60
done
