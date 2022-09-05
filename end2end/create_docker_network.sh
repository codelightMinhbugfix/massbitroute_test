#!/bin/bash
export RUNTIME_DIR=/massbit/test_runtime
while docker network ls | grep "$find_string"
do
    export network_number=$(shuf -i 0-255 -n 1)
    find_string="\"Subnet\": \"172.24.$network_number.0/24\","
    echo $find_string
done
