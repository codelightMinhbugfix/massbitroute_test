#!/usr/bin/env bash

_check_gateway() {
  number=$(curl -s --location --request POST 'https://34.142.136.135' \
  --header 'Content-Type: application/json' \
  --header 'Host: 47729577-1eb5-4013-9f08-df2c64b4c597.gw.mbr.massbitroute.dev' \
  --header 'x-api-key: eCMlJmdz9eS9QCai2ZjALg' \
  --data-raw '{
    "jsonrpc": "2.0",
    "method": "eth_getBlockByNumber",
    "params": [
        "latest",
        false
    ],
    "id": 1
  }' -k | jq -r ". | .result.number")
  echo "Block number at gateway $number"
}

_check_node() {
  number=$(curl -s --location --request POST 'https://35.240.191.117' --header 'Content-Type: application/json' --header 'Host: 8293ea9a-836d-4797-9c36-1cb9f895c06a.node.mbr.massbitroute.dev' --header 'x-api-key: rFuv9u8L2HSpRRg6pZnJjg' --data-raw '{
    "jsonrpc": "2.0",
    "method": "eth_getBlockByNumber",
    "params": [
        "latest",
        false
    ],
    "id": 1
  }' -k | jq -r ". | .result.number")
  echo "Block number at node $number"
}
_check_datasource() {
  number=$(curl -s --location --request POST 'http://34.101.81.225:8545' --header 'Content-Type: application/json' --data-raw '{
    "jsonrpc": "2.0",
    "method": "eth_getBlockByNumber",
    "params": [
        "latest",
        false
    ],
    "id": 1
  }' | jq -r ". | .result.number")
  echo "Block number at datasource $number"
}

_run() {
  while true;
  do
  _check_datasource
  _check_node
  _check_gateway
  echo "-------------------------"
  sleep 4
  done
}

$@