#! /bin/bash
source ../../credentials/.env
prefix=$random
ROOT=$(realpath $(dirname $(realpath $0))/)
PROVIDER_DIR=$ROOT/../providers
CREDENTIALS_PATH=$ROOT/../../credentials
#login
#-------------------------------------------
# Log into Portal
#-------------------------------------------
_login() {
  bearer=$(curl -s --location --request POST 'https://portal.massbitroute.dev/auth/login' --header 'Content-Type: application/json' \
          --data-raw "{\"username\": \"$TEST_USERNAME\", \"password\": \"$TEST_PASSWORD\"}"| jq  -r ". | .accessToken")

  if [[ "$bearer" == "null" ]]; then
    echo "Getting JWT token: Failed"
    exit 1
  fi

  userId=$(curl -s --location --request GET 'https://portal.massbitroute.dev/user/info' \
  --header "Authorization: Bearer $bearer" \
  --header 'Content-Type: application/json' | jq  -r ". | .id")
  echo "User ID $userId"
}

_prepare_terraform() {
  testDapis=()
  thread=5
  connection=5
  duration=30s
  outtf="$2/client.tf"
  request_rates="(50 100 200)"
  echo 'variable "project_prefix" {
    type        = string
    description = "The project prefix (mbr)."
  }
  variable "environment" {
    type        = string
    description = "Environment: dev, test..."
  }
  variable "network_interface" {
    type = string
  }
  variable "client_machine_type" {
     type = string
   }' > "$outtf"
  declare -A nodeIds
  declare -A nodeIps
  declare -A nodeKeys
  while IFS="," read -r nodeId appId name bcName zoneCode status ip
    do
      nodeIds[${zoneCode,,}]=$nodeId
      nodeIps[${zoneCode,,}]=$ip
      nodeKeys[${zoneCode,,}]=$appId
    done < <(tail "$PROVIDER_DIR/$1/nodestatus.csv")
  declare -A gwIds
  declare -A gwIps
  declare -A gwKeys
  while IFS="," read -r gatewayId appId name bcName zoneCode status ip
    do
      gwIds[${zoneCode,,}]=$gatewayId
      gwIps[${zoneCode,,}]=$ip
      gwKeys[${zoneCode,,}]=$appId
    done < <(tail "$PROVIDER_DIR/$1/gatewaystatus.csv")

  while IFS="," read -r region cloudZone zone
  do
#    #create dapi for each client
    random=$(echo $RANDOM | md5sum | head -c 2)
    testDapis+=( $random )
    #-------------------------------------------
    # Create dAPI
    #-------------------------------------------
    if [ "x$dapiURL" == "x" ]; then
      create_dapi_response=$(curl -s --location --request POST "https://portal.$domain/mbr/d-apis" \
        --header "Authorization: Bearer $bearer" \
        --header 'Content-Type: application/json' \
        --data-raw "{
          \"name\": \"$projectName-$random\",
          \"projectId\": \"$projectId\"
      }")
      apiId=$(echo $create_dapi_response | jq -r '. | .entrypoints[0].apiId')
      appKey=$(echo $create_dapi_response | jq -r '. | .appKey')
      dapiURL="https:\/\/$apiId.${blockchain}-mainnet.$domain\/$appKey"
      create_dapi_status=$(echo $create_dapi_response | jq .status)
      if [[ "$create_dapi_status" != "1" ]]; then
        echo "Create new dAPI: Failed"
        exit 1
      else
        echo "Create new dAPI: Passed"
      fi
    else
      echo "dapiURL is defined"
    fi
    _dapiURL=$(echo $dapiURL | sed "s|\/|\\\/|g")
    cat init.tpl | sed "s/\[\[BEARER\]\]/$bearer/g" \
              | sed "s/\[\[ZONE\]\]/$random-${cloudZone,,}/g" \
              | sed "s/\[\[PROJECT_ID\]\]/$projectId/g" \
              | sed "s/\[\[PROJECT_NAME\]\]/${projectName}/g" \
              | sed "s/\[\[DOMAIN\]\]/$domain/g" \
              | sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
              | sed "s/\[\[DAPI_URL\]\]/$_dapiURL/g" \
              | sed "s/\[\[NODE_ID\]\]/${nodeIds[${zone,,}]}/g" \
              | sed "s/\[\[NODE_KEY\]\]/${nodeKeys[${zone,,}]}/g" \
              | sed "s/\[\[NODE_IP\]\]/${nodeIps[${zone,,}]}/g" \
              | sed "s/\[\[GATEWAY_ID\]\]/${gwIds[${zone,,}]}/g" \
              | sed "s/\[\[GATEWAY_KEY\]\]/${gwKeys[${zone,,}]}/g" \
              | sed "s/\[\[GATEWAY_IP\]\]/${gwIps[${zone,,}]}/g" \
              | sed "s/\[\[THREAD\]\]/$thread/g" \
              | sed "s/\[\[CONNECTION\]\]/$connection/g" \
              | sed "s/\[\[DURATION\]\]/$duration/g" \
              | sed "s/\[\[REQUEST_RATES\]\]/$request_rates/g" > "$2/init_$random.sh"
    cat client.template |  sed "s/\[\[REGION\]\]/$region/g" \
                        | sed  "s/\[\[ZONE\]\]/$cloudZone/g" \
                        | sed  "s/\[\[EMAIL\]\]/$email/g" \
                        | sed "s/\[\[INIT_FILE\]\]/$random/g" \
                        | sed "s/\[\[PREFIX\]\]/$random/g" >> $outtf
  done < <(tail "$CREDENTIALS_PATH/zonelist-test.csv")
}
_prepare_env() {
  if [ "x$1" == "x" ]; then
    echo "Please enter test dirname"
    exit 1
   fi
  echo "Create test in dir $1"
  if [ ! -d "$ROOT/$1" ]; then
    mkdir "$ROOT/$1"
  else
    rm $ROOT/$1/init_*.sh
  fi
  cp ./terraform.tfvars $ROOT/$1
  cat provider.tf |  sed "s/\[\[CREDENTIALS_PATH\]\]/$credentialsPath/g"  > "$ROOT/$1/provider.tf"
}
_create_vms() {
  cd $ROOT/$1
  sudo terraform init
  sudo terraform plan -var-file=./terraform.tfvars -out=client.plan
  sudo terraform apply client.plan
}
#_clean_init_files() {
#  for file in ${testDapis[@]}; do
#    rm init_${file}.sh
#  done
#}
#
# $1 provider environment
# $2 test environment
_setup() {
  if [ "x$1" == "x" ]; then
    envdir=$prefix
  else
    envdir=$2
  fi
  _prepare_env $envdir
  _login
  _prepare_terraform $1 $envdir
  _create_vms $envdir
#  _clean_init_files
}
_clean() {
  _login
  echo "Cleaning up VMs: In Progress"
  cd $1
  sudo terraform destroy
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform destroy "; exit 1; fi
  echo "Cleaning up VMs: Passed"
  cd ..
  sudo rm -rf $1
}

$@
