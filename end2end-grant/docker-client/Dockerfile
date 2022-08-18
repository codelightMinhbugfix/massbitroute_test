FROM nginx:latest
RUN rm /etc/nginx/conf.d/default.conf
RUN apt update && apt install jq vim nodejs npm -y
RUN npm install -g newman
COPY scripts /test/scripts
COPY postman /test/postman
#COPY debug.sh /docker-entrypoint.d/
#COPY proxy.conf /etc/nginx/conf.d/default.conf
#COPY index.html /usr/share/nginx/html
#COPY ssl /etc/ssl/private
