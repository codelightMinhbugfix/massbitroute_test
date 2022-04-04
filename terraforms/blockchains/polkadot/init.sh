#!/bin/bash
# setup new user
BLOCKCHAIN=polkadot
USER=polkadot
HOME=/home/${USER}/
SERVICE=/etc/systemd/system/${BLOCKCHAIN}.service
RUN_SCRIPT=/nodes/${BLOCKCHAIN}/run.sh
VERSION=v0.9.16
ROOT_DIR=/nodes/${BLOCKCHAIN}
mkdir -p $ROOT_DIR
sudo curl -sL https://github.com/paritytech/polkadot/releases/download/$VERSION/polkadot -o $ROOT_DIR/polkadot
chmod +x $ROOT_DIR/polkadot
chown ${USER}:${USER} -R $ROOT_DIR
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -batch -subj "/C=VN/ST=HCM/L=HCM/O=CodeLight/OU=Massbit/CN=massbitroute.dev/emailAddress=product@massbit.io" -passout pass:polkadot -passin pass:polkadot -keyout /etc/ssl/private/nginx-polkadot.key -out /etc/ssl/certs/nginx-polkadot.crt
sudo openssl dhparam -out /etc/ssl/certs/dhparam.pem 2048
sudo apt-get update
sudo apt install -y nginx
IPV4=$(curl ipv4.icanhazip.com)
sudo cat > /etc/nginx/sites-available/polkadot <<EOL
server {
        server_name ${IPV4};
        root /var/www/html;
        index index.html;
        location /websocket {
          rewrite /websocket/(.*) /\$1  break;
          try_files \$uri $uri/ =404;

          proxy_buffering off;
          proxy_pass http://localhost:9944;
          proxy_set_header X-Real-IP \$remote_addr;
          proxy_set_header Host \$host;
          proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;

          proxy_http_version 1.1;
          proxy_set_header Upgrade \$http_upgrade;
          proxy_set_header Connection "upgrade";
          proxy_read_timeout 86400s;
          proxy_send_timeout 86400s;
        }

        location / {
          try_files \$uri \$uri/ =404;

          proxy_buffering off;
          proxy_pass http://localhost:9933;
          proxy_set_header X-Real-IP \$remote_addr;
          proxy_set_header Host \$host;
          proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;

          proxy_http_version 1.1;
          proxy_set_header Upgrade \$http_upgrade;
          proxy_set_header Connection "upgrade";
          proxy_read_timeout 86400s;
          proxy_send_timeout 86400s;
        }

        listen [::]:443 ssl ipv6only=on;
        listen 443 ssl;

        ssl_certificate /etc/ssl/certs/nginx-polkadot.crt;
        ssl_certificate_key /etc/ssl/private/nginx-polkadot.key;

        ssl_session_cache shared:cache_nginx_SSL:1m;
        ssl_session_timeout 1440m;

        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;

        ssl_ciphers "ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-ECDSA-AES128-SHA:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA:ECDHE-ECDSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA:ECDHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA256:DHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA256:DHE-RSA-AES256-SHA:ECDHE-ECDSA-DES-CBC3-SHA:ECDHE-RSA-DES-CBC3-SHA:EDH-RSA-DES-CBC3-SHA:AES128-GCM-SHA256:AES256-GCM-SHA384:AES128-SHA256:AES256-SHA256:AES128-SHA:AES256-SHA:DES-CBC3-SHA:!DSS";

        ssl_dhparam /etc/ssl/certs/dhparam.pem;
}
EOL
ln -sf /etc/nginx/sites-available/polkadot /etc/nginx/sites-enabled/
sudo nginx -s reload
sudo mkdir "${HOME}"
sudo chmod -R 757 "${HOME}"
sudo chmod -R 757 /nodes/${BLOCKCHAIN}/
sudo adduser --disabled-password --gecos "" --home "${HOME}" "${USER}"
# create systemd
sudo cat >${SERVICE} <<EOL
[Unit]
    Description=Geth Node
    After=network.target
[Service]
    LimitNOFILE=700000
    LogRateLimitIntervalSec=0
    User=${USER}
    Group=${USER}
    WorkingDirectory=/nodes/${BLOCKCHAIN}/
    Type=simple
    ExecStart=/nodes/${BLOCKCHAIN}/run.sh
    StandardOutput=file:/home/${USER}/console.log
    StandardError=file:/home/${USER}/error.log
    Restart=always
    RestartSec=10
[Install]
    WantedBy=multi-user.target
EOL
sudo cat >${RUN_SCRIPT} <<EOL
#!/usr/bin/bash
/nodes/polkadot/polkadot --name "massbit" --rpc-cors all
EOL
chmod +x $RUN_SCRIPT
chown ${USER}:${USER} -R /nodes/${BLOCKCHAIN}
systemctl enable ${BLOCKCHAIN}.service
systemctl start ${BLOCKCHAIN}.service
