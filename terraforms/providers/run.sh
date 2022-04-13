#!/bin/bash
#bearer="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4MTFjMzc4Mi0yNzk0LTQ0MjMtYWEwMi0zYjAwOGZkYTQzOGQiLCJpYXQiOjE2NDg2MTM3ODAsImV4cCI6MTY0ODcwMDE4MH0.hZQak__--HUYsXTz3YSDfDYmIK0r85a-jaiDSb0CqV16AqeXd5i1oXy0J5JAd03ZgMVOepSn_pCtjZlbmk82i8l7Tcxc2pjvdKuj2GmCWIyNKrKFokkp8NSsRkogAuCk8cZymNJVv0LXjiQOXFk-6hJ-mJMyT1hlJUIkuECYp-zihCp5QkK2q2QYbVCN1YLYsHLuWTrsKjH48v_NPJtfg6D8GHXvZT_SAen24o3reLV1247B_4yZ9ohcICiP3lQp-qSGIC3o_iMaOCHznEuPziA7puoxxsOPdyaE-JSXCYc80pljnUFEdiNH1ArJo6JspUzm1MTWeiwRPJs9fp2T8Q"
source .env
nodePrefix=$random
staking_amount=100
#login
#-------------------------------------------
# Log into Portal
#-------------------------------------------
_login() {
  bearer=$(curl --location --request POST 'https://portal.massbitroute.dev/auth/login' --header 'Content-Type: application/json' \
          --data-raw "{\"username\": \"$TEST_USERNAME\", \"password\": \"$TEST_PASSWORD\"}"| jq  -r ". | .accessToken")

  if [[ "$bearer" == "null" ]]; then
    echo "Getting JWT token: Failed"
    exit 1
  fi

  userId=$(curl --location --request GET 'https://portal.massbitroute.dev/user/info' \
  --header "Authorization: Bearer $bearer" \
  --header 'Content-Type: application/json' | jq  -r ". | .id")
  echo "User ID $userId"
}

_create_nodes() {
  echo -n > nodelist.csv
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
  done < <(tail ../../credentials/eth-sources.csv)
}
_create_gateways() {
  echo -n > gatewaylist.csv
  while IFS="," read -r id region zone zoneCode dataSource
    do
      # curl  node/gw
      curl --location --request POST 'https://portal.massbitroute.dev/mbr/gateway' \
        --header "Authorization: Bearer  $bearer" \
        --header 'Content-Type: application/json' \
        --data-raw "{
          \"name\":\"mbr-gw-$nodePrefix-$id-$zone\",
          \"blockchain\":\"$blockchain\",
          \"zone\":\"$zoneCode\",
          \"network\":\"mainnet\"}" | jq -r '. | .id, .appKey, .name, .blockchain, .zone' | sed -z -z "s/\n/,/g;s/,$/,$zone\n/" >> gatewaylist.csv

    done < <(tail ../../credentials/eth-sources.csv)
}

_prepare_terraform() {
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
  variable "map_machine_types" {
    type = map
  }' > nodes.tf
  # create  node
  while IFS="," read -r nodeId appId name blockchain dataSource zone cloudZone
  do
    dataSource=$(echo $dataSource | sed "s|\/|\\\/|g")
    echo $ds
    cat node-template |  sed "s/\[\[GATEWAY_ID\]\]/$nodeId/g" | sed  "s/\[\[APP_KEY\]\]/$appId/g" \
      | sed  "s/\[\[ZONE\]\]/$zone/g" | sed  "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
      | sed  "s/\[\[DATASOURCE\]\]/$dataSource/g" | sed  "s/\[\[NAME\]\]/$name/g" \
      | sed  "s/\[\[EMAIL\]\]/$email/g" \
      | sed  "s/\[\[CLOUD_ZONE\]\]/$cloudZone/g" | sed  "s/\[\[USER_ID\]\]/$userId/g"  >> nodes.tf
  done < <(tail nodelist.csv)

  #create gw
  while IFS="," read -r nodeId appId name blockchain zone cloudZone
  do
    cat gateway-template |  sed "s/\[\[GATEWAY_ID\]\]/$nodeId/g" | sed  "s/\[\[APP_KEY\]\]/$appId/g" \
      | sed  "s/\[\[ZONE\]\]/$zone/g" | sed  "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
      | sed  "s/\[\[NAME\]\]/$name/g" | sed  "s/\[\[CLOUD_ZONE\]\]/$cloudZone/g" \
      | sed  "s/\[\[EMAIL\]\]/$email/g" \
      | sed  "s/\[\[USER_ID\]\]/$userId/g" >> nodes.tf
  done < <(tail gatewaylist.csv)
}
_check_created_nodes() {
  #Check node status: all is created
  echo "Check new node status in Portal"
  while IFS="," read -r nodeId appId name blockchain zone cloudZone
  do
    node_reponse_code=$(curl  -o /dev/null -s -w "%{http_code}\n" "https://portal.massbitroute.dev/mbr/node/$nodeId" --header "Authorization: Bearer $bearer")
    if [[ $node_reponse_code != 200 ]]; then
      echo "Create node $nodeId in Portal: Failed"
      exit 1
    fi
    echo "Create node $nodeId in Portal: Passed"
  done < <(tail nodelist.csv)
}
_check_created_gateways() {
  #Check gateway status: all is created
  while IFS="," read -r gatewayId appId name blockchain zone cloudZone
  do
    gateway_reponse_code=$(curl  -o /dev/null -s -w "%{http_code}\n" "https://portal.massbitroute.dev/mbr/gateway/$gatewayId" --header "Authorization: Bearer $bearer")
    if [[ $gateway_reponse_code != 200 ]]; then
    echo "Create gw $gatewayId in Portal: Failed"
      exit 1
    fi
    echo "Create gw $gatewayId in Portal: Passed"
  done < <(tail gatewaylist.csv)
}
_create_vms() {
  # wait for node to install script
  echo "Create node VMs on GCE: In Progress"
  sudo terraform init -input=false
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform init "; exit 1; fi
  sudo terraform plan -out=tfplan -input=false
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform plan "; exit 1; fi
  sudo terraform apply -input=false tfplan
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform apply "; exit 1; fi
  echo "Create node VMs on GCE: Passed"

  echo "Waiting for nodes to set up"
  sleep 120
}
_check_verified_nodes() {
  echo "Check if nodes are verified"
  # loop over node list to check status
  declare -A nodeStatuses
  while IFS="," read -r nodeId appId name blockchain zone cloudZone
  do
    nodeStatuses[$nodeId]='created'
  done < <(tail nodelist.csv)

  total=${#nodeStatuses[@]}
  verified_counter=0
  while [[ $verified_counter < $total ]]
  do
    verified_counter=0
    for key in "${!nodeStatuses[@]}"; do
      echo "Status of node $key: ${nodeStatuses[$key]}";
      if [ "${nodeStatuses[$key]}" != "verified" ]; then
        status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/node/$key" \
          --header "Authorization: Bearer $bearer"| jq -r ". | .status")
        nodeStatuses[$key]=$status
      else
        ((verified_counter=verified_counter+1))
      fi
    done
    sleep 20
  done
}
_stake_nodes() {
  while IFS="," read -r nodeId appId name blockchain zone cloudZone
    do
      staking_response=$(curl -s --location --request POST 'https://staking.massbitroute.dev/massbit/staking' \
         --header 'Content-Type: application/json' --data-raw "{
           \"memonic\": \"$MEMONIC\",
           \"providerId\": \"$nodeId\",
           \"providerType\": \"Node\",
           \"blockchain\": \"$blockchain\",
           \"network\": \"mainnet\",
           \"amount\": \"$staking_amount\"
       }" | jq .status)
       if [[ "$staking_response" != "success" ]]; then
         echo "Staking node $nodeId: Failed"
       else
         echo "Staking node $nodeId: Passed"
       fi
    done < <(tail nodelist.csv)
}
_stake_gateways() {
  while IFS="," read -r gatewayId appId name blockchain zone cloudZone
    do
      staking_response=$(curl -s --location --request POST 'https://staking.massbitroute.dev/massbit/staking' \
         --header 'Content-Type: application/json' --data-raw "{
           \"memonic\": \"$MEMONIC\",
           \"providerId\": \"$gatewayId\",
           \"providerType\": \"Gateway\",
           \"blockchain\": \"$blockchain\",
           \"network\": \"mainnet\",
           \"amount\": \"$staking_amount\"
       }" | jq .status)
       if [[ "$staking_response" != "success" ]]; then
         echo "Staking gateway $gatewayId: Failed"
       else
         echo "Staking gateway $gatewayId: Passed"
       fi
    done < <(tail gatewaylist.csv)
}
_check_verified_gateways() {
  _login
  echo "Check if gateways are verified"
  # loop over gateway list to check status
  declare -A gwStatuses
  while IFS="," read -r gatewayId appId name blockchain zone cloudZone
  do
    gwStatuses[$gatewayId]='created'
  done < <(tail gatewaylist.csv)

  total=${#gwStatuses[@]}
  verified_counter=0
  while [[ $verified_counter < $total ]]
  do
    verified_counter=0
    for key in "${!gwStatuses[@]}"; do
      echo "Status of gateway $key: ${gwStatuses[$key]}";
      if [ "${gwStatuses[$key]}" != "verified" ]; then
        status=$(curl -s --location --request GET "https://portal.massbitroute.dev/mbr/gateway/$key" \
          --header "Authorization: Bearer $bearer"| jq -r ". | .status")
        gwStatuses[$key]=$status
      else
        ((verified_counter=verified_counter+1))
      fi
    done
    sleep 20
  done
}
_setup() {
  _login
  echo "Create test with prefix $nodePrefix"
  _create_nodes
  _create_gateways
  _check_created_nodes
  _check_created_gateways
  _prepare_terraform
  _create_vms
  _check_verified_nodes
  _check_verified_gateways
  _stake_nodes
  _stake_gateways
}
_clean() {
  _login
  echo "Delete node from portal"
  while IFS="," read -r nodeId appId name blockchain zone cloudZone
     do
       status=$(curl -s --location --request DELETE "https://portal.massbitroute.dev/mbr/node/$nodeId" \
          --header "Authorization: Bearer $bearer" | jq .status)
        if [[ "$status" != "true" ]]; then
          echo "Delete node $nodeId: Failed"
        else
          echo "Delete node $nodeId: Passed"
        fi
     done < <(tail nodelist.csv)

  while IFS="," read -r gatewayId appId name blockchain zone cloudZone
     do
       status=$(curl -s --location --request DELETE "https://portal.massbitroute.dev/mbr/gateway/$gatewayId" \
          --header "Authorization: Bearer $bearer" | jq .status)
        if [[ "$status" != "true" ]]; then
          echo "Delete gateway $gatewayId: Failed"
        else
          echo "Delete gateway $gatewayId: Passed"
        fi

     done < <(tail gatewaylist.csv)
  echo "Cleaning up VMs: In Progress"
  terraform destroy
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform destroy "; exit 1; fi
  echo "Cleaning up VMs: Passed"
}

$@