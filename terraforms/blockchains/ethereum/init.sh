#!/bin/bash
add-apt-repository ppa:ethereum/ethereum
apt install -y ethereum pip unzip
# setup new user
FILENAME=ethereum.zip
BC_HOME=/home/ethereum/
BC_USER=ethereum
SERVICE=/etc/systemd/system/ethereum.service
RUN_SCRIPT=/nodes/ethereum/run.sh
mkdir -p /nodes/ethereum
chown ${BC_USER}:${BC_USER} -R /nodes/ethereum

sudo mkdir "${BC_HOME}"
sudo chmod -R 757 "${BC_HOME}"
sudo chmod -R 757 /nodes/ethereum/
sudo adduser --disabled-password --gecos "" --home "${BC_HOME}" "${BC_USER}"
sudo pip install gdown
cd ${BC_HOME}
gdown --id 1Ul8HTNC6Eq7gr3Ac8ZyZWyGaJb-iZNby
sudo unzip ${FILENAME} -d $BC_HOME
sudo chown -R $BC_USER:$BC_USER ${BC_HOME}
#fileid="1Ul8HTNC6Eq7gr3Ac8ZyZWyGaJb-iZNby"
#filename="ethereum.zip"
#curl -c ./cookie -s -L "https://drive.google.com/uc?export=download&id=${fileid}" > /dev/null
#curl -Lb ./cookie "https://drive.google.com/uc?export=download&confirm=`awk '/download/ {print $NF}' ./cookie`&id=${fileid}" -o ${filename}
#wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=1Ul8HTNC6Eq7gr3Ac8ZyZWyGaJb-iZNby' -O ethereum.zip

# create systemd
sudo cat >${SERVICE} <<EOL
[Unit]
      Description=Geth Node
      After=network.target
[Service]
      LimitNOFILE=700000
      LogRateLimitIntervalSec=0
      User=ethereum
      Group=ethereum
      WorkingDirectory=/nodes/ethereum/
      Type=simple
      ExecStart=/nodes/ethereum/run.sh
      StandardOutput=file:/home/ethereum/console.log
      StandardError=file:/home/ethereum/error.log
      Restart=always
      RestartSec=10
[Install]
      WantedBy=multi-user.target
EOL
sudo cat >${RUN_SCRIPT} <<EOL
#!/usr/bin/bash
/usr/bin/geth --syncmode "light" --nousb --http --http.addr 0.0.0.0 --http.api db,eth,net,web3,personal,shh --http.vhosts "*" --http.corsdomain "*" --ws --ws.addr 0.0.0.0 --ws.origins "*" --ws.api db,eth,net,web3,personal,shh 2>&1  >> /nodes/ethereum/eth.log
EOL
chmod +x $RUN_SCRIPT
chown ${BC_USER}:${BC_USER} -R /nodes/ethereum
systemctl enable ethereum.service
systemctl start ethereum.service
