#!/bin/bash

bearer="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4MTFjMzc4Mi0yNzk0LTQ0MjMtYWEwMi0zYjAwOGZkYTQzOGQiLCJpYXQiOjE2NDg3ODg2NTgsImV4cCI6MTY0ODg3NTA1OH0.T-RNKU_x-hmnThiBaO9YA1K4T_yqdCOcrgFQs1GOFbo5992ifqUlfGE7k0vSC4lmgv2Rh4nvg090BhkhMTYfRyctJmGgByAa-ayvbf1slCBR1tpJLjWZmQA1M0rwQhY559UsGCmeEqMgEH8pE6BvWtIAVsbWfS_8gIDEx7RBHWxasEwAft1-rkedxu2oYDwLndzdW9ARNYAZZ9U7FrnbYltOStlFNpcnCDq-WxSo6pDkJG5EgkrRyVxY-kBT37oJyNhM8m9iQTI5E7FgGeCkqiH54siFl8nEohYg_M6nTDZeE1Hl3oMQfR67ab4Yudg9ZGNYCEFLQ0m6ZDYcPC6FLQ"
blockchain="eth"
dataSource="http:\/\/34.124.230.213:8545"
nodePrefix="$(echo $RANDOM | md5sum | head -c 5)"
GATEWAY_ID=""
NODE_ID=""
MEMONIC="peanut thank prevent burden erode welcome dust one develop code lamp rule"

sudo echo -n >gatewaylist.csv
sudo echo -n >nodelist.csv

sudo echo 'variable "project_prefix" {
  type        = string
  description = "The project prefix (mbr)."
}
variable "environment" {
  type        = string
  description = "Environment: dev, test..."
}
variable "default_zone" {
  type = string
}
variable "network_interface" {
  type = string
}
variable "email" {
  type = string
}
variable "map_machine_types" {
  type = map
}' >test-nodes.tf

#-------------------------------------------
# create  node/gw in Portal
#-------------------------------------------
echo "Create new node and gw in Portal: In Progress"
sudo curl -s --location --request POST 'https://portal.massbitroute.dev/mbr/node' \
  --header "Authorization: Bearer  $bearer" \
  --header 'Content-Type: application/json' \
  --data-raw "{
      \"name\": \"mb-dev-node-$nodePrefix\",
      \"blockchain\": \"$blockchain\",
      \"zone\": \"AS\",
      \"dataSource\": \"$dataSource\",
      \"network\": \"mainnet\"
  }" | jq -r '. | .id, .appKey' | sed -z -z 's/\n/,/g;s/,$/,AS\n/' >nodelist.csv

sudo curl -s --location --request POST 'https://portal.massbitroute.dev/mbr/gateway' \
  --header "Authorization: Bearer  $bearer" \
  --header 'Content-Type: application/json' \
  --data-raw "{
    \"name\":\"MB-dev-gateway-$nodePrefix\",
    \"blockchain\":\"$blockchain\",
    \"zone\":\"AS\",
    \"network\":\"mainnet\"}" | jq -r '. | .id, .appKey' | sed -z -z 's/\n/,/g;s/,$/,AS\n/' >gatewaylist.csv

#-------------------------------------------
# check if node/gw are created in Portal successfully
#-------------------------------------------
GATEWAY_ID=$(cut -d ',' -f 1 gatewaylist.csv)
NODE_ID=$(cut -d ',' -f 1 nodelist.csv)

# curl node info
gateway_reponse_code=$(curl -o /dev/null -s -w "%{http_code}\n" "https://portal.massbitroute.dev/mbr/gateway/$GATEWAY_ID" --header "Authorization: Bearer $bearer")
if [[ $gateway_reponse_code != 200 ]]; then
  echo "Create new gw in Portal: Failed"
  exit 1
fi
echo "Create new gw in Portal: Passed"

node_reponse_code=$(curl -o /dev/null -s -w "%{http_code}\n" "https://portal.massbitroute.dev/mbr/node/$NODE_ID" --header "Authorization: Bearer $bearer")
if [[ $node_reponse_code != 200 ]]; then
  echo "Create new node in Portal: Failed"
  exit 1
fi
echo "Create new node in Portal: Passed"


#-------------------------------------------
# create  node/gw tf files
#-------------------------------------------
while IFS="," read -r nodeId appId zone; do
  cat gateway-template-single | sed "s/\[\[GATEWAY_ID\]\]/$nodeId/g" | sed "s/\[\[APP_KEY\]\]/$appId/g" |
    sed "s/\[\[ZONE\]\]/$zone/g" | sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" >>test-nodes.tf
done < <(tail gatewaylist.csv)

while IFS="," read -r nodeId appId zone; do
  cat node-template-single | sed "s/\[\[GATEWAY_ID\]\]/$nodeId/g" | sed "s/\[\[APP_KEY\]\]/$appId/g" |
    sed "s/\[\[ZONE\]\]/$zone/g" | sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" | sed "s/\[\[DATASOURCE\]\]/$dataSource/g" >>test-nodes.tf
done < <(tail nodelist.csv)


#-------------------------------------------
#  Spin up nodes VM on GCE
#-------------------------------------------
echo "Create node VMs on GCE: In Progress"
terraform init -input=false
if [[ "$?" != "0" ]]; then
  echo "terraform init: Failed "
  exit 1
fi
sudo terraform plan -out=tfplan -input=false
if [[ "$?" != "0" ]]; then
  echo "terraform plan: Failed "
  exit 1
fi
sudo terraform apply -input=false tfplan
if [[ "$?" != "0" ]]; then
  echo "terraform apply: Failed"
  exit 1
fi
echo "Create node VMs on GCE: Passed"

echo "Waiting for nodes to set up"
sleep 300

#-------------------------------------------
# Check if nodes are verified
#-------------------------------------------
while [[ "$gateway_status" != "verified" ]] && [[ "$node_status" != "verified" ]]; do
  echo "Checking node status: In Progress"


  gateway_status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/gateway/$GATEWAY_ID" \
    --header "Authorization: Bearer $bearer" | jq -r ". | .status")

  node_status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/node/$NODE_ID" \
    --header "Authorization: Bearer $bearer" | jq -r ". | .status")

  echo "---------------------------------"
  echo "Gateway status: $gateway_status"
  echo "Node status: $node_status"
  echo "---------------------------------"
  sleep 10
done
echo "Checking node status: Passed"


#-------------------------------------------
# Test staking
#-------------------------------------------
gateway_staking_response=$(curl -s --location --request POST 'https://staking.massbitroute.dev/massbit/staking' \
  --header 'Content-Type: application/json' --data-raw "{
    \"memonic\": \"$MEMONIC\",
    \"providerId\": \"$GATEWAY_ID\",
    \"providerType\": \"Gateway\",
    \"blockchain\": \"$blockchain\",
    \"network\": \"mainnet\"
}" | jq .status)
if [[ "$gateway_staking_status" != "success" ]]; then
  echo "Gateway staking status: Failed "
  exit 1
fi
echo "Gateway staking status: Passed"

node_staking_response=$(curl -s --location --request POST 'https://staking.massbitroute.dev/massbit/staking' \
  --header 'Content-Type: application/json' --data-raw "{
    \"memonic\": \"$MEMONIC\",
    \"providerId\": \"$NODE_ID\",
    \"providerType\": \"Node\",
    \"blockchain\": \"$blockchain\",
    \"network\": \"mainnet\"
}" | jq .status)
if [[ "$node_staking_status" != "success" ]]; then
  echo "Node staking: Failed"
  exit 1
fi
echo "Node staking: Passed"

#-------------------------------------------
# Check staking status
#-------------------------------------------
# Check GW Staking status
gateway_staking_status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/gateway/$GATEWAY_ID" \
  --header "Authorization: Bearer $bearer" | jq -r ". | .status")

if [[ "$gateway_staking_status" != "staked" ]]; then
  echo "Verify Gateway staking status: Failed "
  exit 1
fi
echo "Verify Gateway staking status: Passed"

# Check node Staking status
node_staking_status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/node/$NODE_ID" \
  --header "Authorization: Bearer $bearer" | jq -r ". | .status")

if [[ "$node_staking_status" != "staked" ]]; then
  echo "Verify Node staking status: Failed"
  exit 1
fi
echo "Verify Node staking status: Passed"

#-------------------------------------------
# Create dAPI
#-------------------------------------------
create_dapi_response=$(curl -s --location --request POST 'https://portal.massbitroute.dev/mbr/d-apis' \
  --header "Authorization: Bearer $bearer" \
  --header 'Content-Type: application/json' \
  --data-raw "{
    \"name\": \"test-dapi\",
    \"projectId\": \"591e5376-822e-4d76-b39c-1b2885138880\"
}")
create_dapi_status=$(echo $create_dapi_response | jq .status)
if [[ "$create_dapi_status" != "1" ]]; then
  echo "Create new dAPI: Failed"
  exit 1
fi
echo "Create new dAPI: Passed"


#-------------------------------------------
# Test call dAPI
#-------------------------------------------
apiId=$(echo $create_dapi_response | jq .entrypoints[0].apiId)
appKey=$(echo $create_dapi_response | jq .appKey)
dapiURL="https://$apiId.$blockchain-mainnet.massbitroute.dev/$appKey"

dapi_response_code=$(curl -o /dev/null -s -w "%{http_code}\n" --request POST $apiURL \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "jsonrpc": "2.0",
    "method": "eth_getBlockByNumber",
    "params": [
        "latest",
        true
    ],
    "id": 1
}')
if [[ "$dapi_response_code" == "200" ]]; then
  echo "Calling dAPI: Failed"
  exit 1
fi
echo "Calling dAPI: Pass"


#-------------------------------------------
# Cleaning up test VMs
#-------------------------------------------
echo "Cleaning up VMs: In Progress"
terraform destroy -auto-approve
if [[ "$?" != "0" ]]; then
  echo "Failed to execute: terraform destroy "
  exit 1
fi
echo "Cleaning up VMs: Passed"

exit 0

# if verify check query
