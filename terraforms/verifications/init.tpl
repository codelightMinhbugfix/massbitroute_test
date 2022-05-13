#!/bin/bash

#-------------------------------------------
#  Install RUST
#-------------------------------------------
sudo apt update -y
curl https://sh.rustup.rs -sSf | sh -s -- -y
source $HOME/.cargo/env
rustup update nightly
rustup update stable
rustup target add wasm32-unknown-unknown --toolchain nightly
sudo apt install build-essential pkg-config libssl-dev supervisor -y

#-------------------------------------------
#  Set up supervisor
#-------------------------------------------
echo "[program:verification]
command=bash /opt/verification/run.sh
autostart=true
autorestart=true
stderr_logfile=/var/log/verification.err.log
stdout_logfile=/var/log/verification.out.log
user=root
stopasgroup=true" > /etc/supervisor/conf.d/verification.conf

echo "[program:fisherman]
command=bash /opt/fisherman/run.sh
autostart=true
autorestart=true
stderr_logfile=/var/log/fisherman.err.log
stdout_logfile=/var/log/fisherman.out.log
user=root
stopasgroup=true" > /etc/supervisor/conf.d/fisherman.conf

#-------------------------------------------
#  Install NGINX
#-------------------------------------------
sudo apt install curl nginx -y
sudo apt install software-properties-common
sudo cat > /etc/nginx/sites-available/verification <<EOL
server {
    server_name verify-[[ZONE]].[[DOMAIN]];
    listen 80;
    location / {
      #try_files \$uri \$uri/ =404;
      #proxy_buffering off;
      proxy_pass http://localhost:3030;
      #proxy_set_header X-Real-IP \$remote_addr;
      proxy_set_header Host \$host;
      proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
      #proxy_http_version 1.1;
      #proxy_set_header Upgrade \$http_upgrade;
      #proxy_set_header Connection "upgrade";
      #proxy_read_timeout 86400s;
      #proxy_send_timeout 86400s;
    }
}
EOL

sudo ln -sf /etc/nginx/sites-available/verification /etc/nginx/sites-enabled/

#sudo curl https://raw.githubusercontent.com/hoanito/hosts/main/verification.nginx > /etc/nginx/sites-enabled/verification

sudo service nginx restart

#-------------------------------------------
#  Install VERIFICATION (RUST)
#-------------------------------------------
sudo mkdir -p /massbit/massbitroute/app/src/sites/services/monitor
sudo git clone https://github.com/massbitprotocol/massbitroute_monitor.git -b dev /massbit/massbitroute/app/src/sites/services/monitor
cd /massbit/massbitroute/app/src/sites/services/monitor/check_component
/root/.cargo/bin/cargo build --release >> /home/verification.log
mkdir -p /opt/verification
sudo cp target/release/mbr-check-component /opt/verification/mbr-check-component
sudo cp src/archive/check-flow.json /opt/verification/check-flow.json
sudo cp config_check_component.json /opt/verification/config_check_component.json

sudo cat > /opt/verification/base-endpoint.json <<EOL
{
  "eth": [
      {
          "url":"[[ETH_BASE_URL01]]"
      },
      {
          "url":"[[ETH_BASE_URL02]]"
      },
      {
          "url":"[[ETH_SERVICE_URL]]",
          "X-Api-Key": "[[ETH_SERVICE_KEY]]"
      }
  ],
  "dot": [
      {
          "url":"[[DOT_BASE_URL01]]"
      },
      {
          "url":"[[DOT_SERVICE_URL]]",
          "X-Api-Key":"[[ETH_SERVICE_KEY]]"
      }
  ]
}
EOL

sudo cat > /opt/verification/run.sh <<EOL
#!/bin/bash
cd /opt/verification
ZONE=[[ZONE]] RUST_LOG=info RUST_LOG_TYPE=file
./mbr-check-component check-kind -n https://portal.[[DOMAIN]]/mbr/node/list/verify \
    -g https://portal.[[DOMAIN]]/mbr/gateway/list/verify \
    -b base-endpoint.json -c check-flow.json --domain [[DOMAIN]]
EOL

sudo cat > /opt/verification/.env <<EOL
export PORTAL_AUTHORIZATION="[[PORTAL_AUTHORIZATION]]"
export SIGNER_PHRASE="[[SIGNER_PHRASE]]"
EOL

sudo chmod 770 /opt/verification/run.sh
#-------------------------------------------
#  Install FISHERMAN (RUST)
#-------------------------------------------
cd /massbit/massbitroute/app/src/sites/services/monitor/fisherman
# /root/.cargo/bin/cargo build --release >> /home/fisherman.log

mkdir -p /opt/fisherman
/root/.cargo/bin/cargo build --release
sudo cp target/release/mbr-fisherman /opt/fisherman/mbr-fisherman
sudo cp ../check_component/src/archive/check-flow.json      /opt/fisherman/check-flow.json
sudo cp config_check_component.json                         /opt/fisherman/config_check_component.json
sudo cp config_fisherman.json                               /opt/fisherman/config_fisherman.json

sudo cp /opt/verification/base-endpoint.json /opt/fisherman

sudo cat > /opt/fisherman/run.sh  <<EOL
#!/bin/bash
cd /opt/fisherman
ZONE=[[ZONE]] RUST_LOG=info RUST_LOG_TYPE=file
./mbr-fisherman run-fisherman -n https://portal.[[DOMAIN]]/mbr/node/list/verify \
    -g https://portal.[[DOMAIN]]/mbr/gateway/list/verify \
    -b base-endpoint.json -c check-flow.json \
    -m  wss://[[BLOCKCHAIN_ENDPOINT]]  --domain [[DOMAIN]]
EOL

sudo cat > /opt/fisherman/.env <<EOL
export PORTAL_AUTHORIZATION="[[PORTAL_AUTHORIZATION]]"
export SIGNER_PHRASE="[[SIGNER_PHRASE]]"
EOL

sudo chmod 770 /opt/fisherman/run.sh
sudo supervisorctl reread
sudo supervisorctl update
sudo supervisorctl start verification
sudo supervisorctl start fisherman