#bearer="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4MTFjMzc4Mi0yNzk0LTQ0MjMtYWEwMi0zYjAwOGZkYTQzOGQiLCJpYXQiOjE2NDg2MTM3ODAsImV4cCI6MTY0ODcwMDE4MH0.hZQak__--HUYsXTz3YSDfDYmIK0r85a-jaiDSb0CqV16AqeXd5i1oXy0J5JAd03ZgMVOepSn_pCtjZlbmk82i8l7Tcxc2pjvdKuj2GmCWIyNKrKFokkp8NSsRkogAuCk8cZymNJVv0LXjiQOXFk-6hJ-mJMyT1hlJUIkuECYp-zihCp5QkK2q2QYbVCN1YLYsHLuWTrsKjH48v_NPJtfg6D8GHXvZT_SAen24o3reLV1247B_4yZ9ohcICiP3lQp-qSGIC3o_iMaOCHznEuPziA7puoxxsOPdyaE-JSXCYc80pljnUFEdiNH1ArJo6JspUzm1MTWeiwRPJs9fp2T8Q"
source .env
GATEWAY_ID=""
NODE_ID=""

echo -n > gatewaylist.csv
echo -n > nodelist.csv

echo 'variable "project_prefix" {
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
}' > test-nodes.tf
echo "Create test with prefix $nodePrefix"
while IFS="," read -r id region zone zoneCode dataSource
do
  # curl  node/gw
  curl --location --request POST 'https://portal.massbitroute.dev/mbr/node' \
    --header "Authorization: Bearer  $bearer" \
    --header 'Content-Type: application/json' \
    --data-raw "{
        \"name\": \"mbr-node-$nodePrefix-$id-$zone\",
        \"blockchain\": \"$blockchain\",
        \"zone\": \"$zoneCode\",
        \"dataSource\": \"$dataSource\",
        \"network\": \"mainnet\"
    }" | jq -r '. | .id, .appKey, .name, .blockchain, .dataSource, .zone' | sed -z -z "s/\n/,/g;s/,$/,$zone\n/" >> nodelist.csv

  curl --location --request POST 'https://portal.massbitroute.dev/mbr/gateway' \
    --header "Authorization: Bearer  $bearer" \
    --header 'Content-Type: application/json' \
    --data-raw "{
      \"name\":\"mbr-gw-$nodePrefix-$id-$zone\",
      \"blockchain\":\"$blockchain\",
      \"zone\":\"$zoneCode\",
      \"network\":\"mainnet\"}" | jq -r '. | .id, .appKey, .name, .blockchain, .zone' | sed -z -z "s/\n/,/g;s/,$/,$zone\n/" >> gatewaylist.csv

done < <(tail eth-sources.csv)

# create  node
while IFS="," read -r nodeId appId name blockchain dataSource zone cloudZone
do
  NODE_ID="$nodeId"
  dataSource=$(echo $dataSource | sed "s|\/|\\\/|g")
  echo $ds
  cat node-template |  sed "s/\[\[GATEWAY_ID\]\]/$nodeId/g" | sed  "s/\[\[APP_KEY\]\]/$appId/g" \
    | sed  "s/\[\[ZONE\]\]/$zone/g" | sed  "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
    | sed  "s/\[\[DATASOURCE\]\]/$dataSource/g" | sed  "s/\[\[NAME\]\]/$name/g" \
    | sed  "s/\[\[CLOUD_ZONE\]\]/$cloudZone/g" | sed  "s/\[\[USER_ID\]\]/$userId/g"  >> test-nodes.tf
done < <(tail nodelist.csv)

#create gw
while IFS="," read -r nodeId appId name blockchain zone cloudZone
do
  GATEWAY_ID="$nodeId"
  cat gateway-template |  sed "s/\[\[GATEWAY_ID\]\]/$nodeId/g" | sed  "s/\[\[APP_KEY\]\]/$appId/g" \
    | sed  "s/\[\[ZONE\]\]/$zone/g" | sed  "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
    | sed  "s/\[\[NAME\]\]/$name/g" | sed  "s/\[\[CLOUD_ZONE\]\]/$cloudZone/g" \
    | sed  "s/\[\[USER_ID\]\]/$userId/g" >> test-nodes.tf
done < <(tail gatewaylist.csv)


# curl node info
echo "Create new node and gw in Portal: In Progress"
gateway_reponse_code=$(curl  -o /dev/null -s -w "%{http_code}\n" "https://portal.massbitroute.dev/mbr/gateway/$GATEWAY_ID" --header "Authorization: Bearer $bearer")
if [[ $gateway_reponse_code != 200 ]]; then
echo "Create new gw in Portal: Failed"
  exit 1
fi
echo "Create new gw in Portal: Passed"

node_reponse_code=$(curl  -o /dev/null -s -w "%{http_code}\n" "https://portal.massbitroute.dev/mbr/node/$NODE_ID" --header "Authorization: Bearer $bearer")
if [[ $node_reponse_code != 200 ]]; then
  echo "Create new node in Portal: Failed"
  exit 1
fi
echo "Create new node in Portal: Passed"

# wait for node to install script
echo "Create node VMs on GCE: In Progress"
sudo terraform init -input=false
if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform init "; exit 1; fi
sudo terraform plan -out=tfplan -input=false
if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform plan "; exit 1; fi
sudo terraform apply -input=false tfplan
if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform apply "; exit 1; fi
echo "Create node VMs on GCE: Passed"

# loop check status
echo "Waiting for nodes to set up"
sleep 120


while [[ "$gateway_status" != "verified" ]] && [[ "$node_status" != "verified" ]]
do
echo "Checking node status: In Progress"
echo "---------------------------------"
echo "Gateway status: $gateway_status"
echo "Node status: $node_status"
echo "---------------------------------"

  gateway_status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/gateway/$GATEWAY_ID" \
  --header "Authorization: Bearer $bearer" | jq -r ". | .status")

  node_status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/node/$NODE_ID" \
  --header "Authorization: Bearer $bearer"| jq -r ". | .status")

  sleep 5
done
echo "Checking node status: Passed"

# Cleaning up test VMs
echo "Cleaning up VMs: In Progress"
terraform destroy -auto-approve
if [[ "$?" != "0" ]]; then
  echo "Failed to execute: terraform destroy "
  exit 1
fi
echo "Cleaning up VMs: Passed"


exit 0


# if verify check query
