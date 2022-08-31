#! /bin/bash

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &>/dev/null && pwd)

# ==============================
# Ethereum network name (eg. rinkeby, mainnet)
# ==============================
export ETHEREUM_NETWORK="rinkeby"
# ==============================
# Ethereum mainnet datasource
# ==============================
# export MASSBIT_ROUTE_ETHEREUM="http://34.81.232.186:8545"
# ==============================
# Ethereum rinkeby datasource
# ==============================
export MASSBIT_ROUTE_ETHEREUM="http://35.240.241.166:8545"
# ==============================
# Polkadot datasource
# ==============================
export MASSBIT_ROUTE_POLKADOT="http://172.104.56.238:9933"
# ==============================
# Infura provider url
# ==============================
export ANOTHER_ETHEREUM_PROVIDER="https://rinkeby.infura.io/v3/2b9f6488f50f4e3b95d8aa375ce146d1"
# ==============================
# Polkadot provider url
# ==============================
export ANOTHER_POLKADOT_PROVIDER="https://rpc.polkadot.io"
# ==============================
# Limit test run
# ==============================
export NUMBER_OF_TESTS=2
# ==============================
# Directory store report
# ==============================
export REPORT_DIR=$SCRIPT_DIR
# ==============================
# Your account's private key
# ==============================
export ETHEREUM_PRIVATE_KEY="ETHEREUM_PRIVATE_KEY"
# ==============================
# EOA address for receiving ETH
# ==============================
export ETHEREUM_EOA_ADDRESS="ETHEREUM_EOA_ADDRESS (eg. 0x80143CBe15fbC4ff9CaDaD378418C20659A2E919)"

bash $SCRIPT_DIR/ethereum/ethereum-test.sh
bash $SCRIPT_DIR/ethereum/ethereum-latency-test.sh
bash $SCRIPT_DIR/polkadot/polkadot-test.sh
bash $SCRIPT_DIR/polkadot/polkadot-latency-test.sh
cd $SCRIPT_DIR/ethereum/flow-test && npm install && node index.js $NUMBER_OF_TESTS $MASSBIT_ROUTE_ETHEREUM $ANOTHER_ETHEREUM_PROVIDER $ETHEREUM_NETWORK $REPORT_DIR $ETHEREUM_PRIVATE_KEY $ETHEREUM_EOA_ADDRESS
