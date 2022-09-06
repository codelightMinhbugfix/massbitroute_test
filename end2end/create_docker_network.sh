#!/bin/bash
mkdir -p .vars/
while docker network ls | grep "$find_string"
do
    network_number=$(shuf -i 0-255 -n 1)
    find_string="\"Subnet\": \"172.24.$network_number.0/24\","
    echo $find_string
    echo $network_number > .vars/NETWORK_NUMBER
done
