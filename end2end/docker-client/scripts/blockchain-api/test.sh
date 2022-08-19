#! /bin/bash

# ==============================
# Massbit datasource
# ==============================
export data_source_polkadot="http://172.104.56.238:9933"
export data_source_mainnet="http://34.81.232.186:8545"
export data_source_rinkeby="http://35.240.241.166:8545"
# ==============================
# Infura provider url
# ==============================
export infura="https://mainnet.infura.io/v3/2b9f6488f50f4e3b95d8aa375ce146d1"
# ==============================
# Alchemy provider url
# ==============================
# export alchemy="https://eth-mainnet.g.alchemy.com/v2/NELeo7OvNIh36tD6SK51Dju0K-0CxKFh"
# ==============================
# Polka provider url
# ==============================
export polka="https://rpc.polkadot.io"
# ==============================
# Limit test run
# ==============================
export num_of_test_case=3
# ==============================
# Directory to save report file
# ==============================
export report_dir="."

# echo "===================================================="
# # read -p "Press enter to test ethereum..."
bash ./ethereum/ethereum-test.sh
# echo "===================================================="
# # read -p "Press enter to test ethereum latency..."
bash ./ethereum/ethereum-latency-test.sh
# echo "===================================================="
# read -p "Press enter to test polkadot..."
bash ./polkadot/polkadot-test.sh
# echo "===================================================="
# # read -p "Press enter to test polkadot latency..."
bash ./polkadot/polkadot-latency-test.sh
