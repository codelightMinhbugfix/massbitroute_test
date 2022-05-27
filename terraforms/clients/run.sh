#! /bin/bash
ROOT=$(realpath $(dirname $(realpath $0))/)
PROVIDER_DIR=$ROOT/../providers
CREDENTIALS_PATH=$ROOT/../../credentials
client_thread=5
client_connection=5
test_duration=30s
test_rates="(10 100 300)"

#login
#-------------------------------------------
# Log into Portal
#-------------------------------------------
_login() {
  bearer=$(curl -s --location --request POST "https://portal.$domain/auth/login" --header 'Content-Type: application/json' \
          --data-raw "{\"username\": \"$TEST_USERNAME\", \"password\": \"$TEST_PASSWORD\"}"| jq  -r ". | .accessToken")

  if [[ "$bearer" == "null" ]]; then
    echo "Getting JWT token: Failed"
    exit 1
  fi

  userId=$(curl -s --location --request GET "https://portal.$domain/user/info" \
  --header "Authorization: Bearer $bearer" \
  --header 'Content-Type: application/json' | jq  -r ". | .id")
  echo "User ID $userId"
}
_prepare_dapis() {
    #-------------------------------------------
    # Create dAPI
    #-------------------------------------------
    dApis=$(curl -s --location --request GET "https://portal.$domain/mbr/d-apis/list/$projectId?limit=100" \
      --header "Authorization: Bearer $bearer" | jq  -r ". | .dApis")
    len=$(echo $dApis | jq length)
    if [ $len -lt 10 ]; then
      for i in $( seq $len 9 );
      do
        random=$(echo $RANDOM | md5sum | head -c 3)
        create_dapi_response=$(curl -s --location --request POST "https://portal.$domain/mbr/d-apis" \
          --header "Authorization: Bearer $bearer" \
          --header 'Content-Type: application/json' \
          --data-raw "{
            \"name\": \"$projectName-$random\",
            \"projectId\": \"$projectId\"
          }")
        create_dapi_status=$(echo $create_dapi_response | jq .status)
        apiId=$(echo $create_dapi_response | jq -r '. | .entrypoints[0].apiId')
        appKey=$(echo $create_dapi_response | jq -r '. | .appKey')
        dapiURL="https:\/\/$apiId.${blockchain}-mainnet.$domain\/$appKey"
        if [[ "$create_dapi_status" != "1" ]]; then
          echo "Create new dAPI: Failed"
          exit 1
        else
          echo "Create new dAPI: Passed"
        fi
      done
    fi
}
_clean_dapis() {
  _prepare_env $1 $2
  _login
  dApis=$(curl -s --location --request GET "https://portal.$domain/mbr/d-apis/list/$projectId?limit=100" \
    --header "Authorization: Bearer $bearer" | jq  -r ". | .dApis")
  len=$(echo $dApis | jq length)
  ((len=len-1))
  for i in $( seq 0 $len );
  do
    dApi=$(echo "$dApis" | jq ".[$i]" | jq ". | .appId" | sed -z "s/\"//g")
    urlL="https://portal.$domain/mbr/d-apis/$dApi"
    status=$(curl -s --location --request DELETE "https://portal.$domain/mbr/d-apis/$dApi" \
      --header "Authorization: Bearer $bearer" | jq -r ".status")
    echo "Delete dapi $dApi with response status $status";
  done
}
_prepare_terraform() {
  outtf="$1/client.tf"
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

  while IFS="," read -r region cloudZone zone counter
  do
    random=$(echo $RANDOM | md5sum | head -c 3)
    for i in $( seq 1 $counter )
      do
        cat init.tpl | sed "s/\[\[USERNAME\]\]/$TEST_USERNAME/g" \
                  | sed "s/\[\[PASSWORD\]\]/$TEST_PASSWORD/g" \
                  | sed "s/\[\[BEARER\]\]/$bearer/g" \
                  | sed "s/\[\[BEARERADMIN\]\]/$bearerAdmin/g" \
                  | sed "s/\[\[CLIENT\]\]/$random-${cloudZone,,}-$i/g" \
                  | sed "s/\[\[ZONE\]\]/$zone/g" \
                  | sed "s/\[\[PROJECT_ID\]\]/$projectId/g" \
                  | sed "s/\[\[PROJECT_NAME\]\]/${projectName}/g" \
                  | sed "s/\[\[DOMAIN\]\]/$domain/g" \
                  | sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
                  | sed "s/\[\[NETWORK\]\]/$network/g" \
                  | sed "s/\[\[DAPI_URL\]\]/$_dapiURL/g" \
                  | sed "s/\[\[THREAD\]\]/$client_thread/g" \
                  | sed "s/\[\[CONNECTION\]\]/$client_connection/g" \
                  | sed "s/\[\[DURATION\]\]/$test_duration/g" \
                  | sed "s/\[\[REQUEST_RATES\]\]/$test_rates/g" > "$1/init_${random}_${i}.sh"
        cat client.template | sed "s/\[\[REGION\]\]/$region/g" \
                            | sed  "s/\[\[ZONE\]\]/$cloudZone/g" \
                            | sed  "s/\[\[EMAIL\]\]/$email/g" \
                            | sed "s/\[\[PREFIX\]\]/$random/g" \
                            | sed "s/\[\[INDEX\]\]/$i/g" >> $outtf
      done
  done < <(tail "$CREDENTIALS_PATH/zonelist-test.csv")
}
_prepare_env() {
  if [ "x$2" == "" ]; then
    envname=dev
    if [ "x$1" == "" ]; then
      envdir=$random
    else
      envdir=$1
    fi
    source ../../credentials/.env
  else
    envname=$2
    envdir=$envname-$1
    source ../../credentials/.env.$2
  fi

  echo "Create test in dir $envdir"
  if [ ! -d "$ROOT/$envdir" ]; then
    mkdir "$ROOT/$envdir"
  fi
  cp ./terraform.tfvars $ROOT/$envdir
  cat provider.tf |  sed "s/\[\[CREDENTIALS_PATH\]\]/$credentialsPath/g"  > "$ROOT/$envdir/provider.tf"
}
_create_vms() {
  cd $ROOT/$1
  sudo terraform init
  sudo terraform plan -var-file=./terraform.tfvars -out=client.plan
  sudo terraform apply client.plan
}

# $1 test name
# $2 environment: net or dev

_setup() {
  _prepare_env $1 $2
  _login
  _prepare_dapis $2
  _prepare_terraform $envdir
  _create_vms $envdir
#  _clean_init_files
}
# $1 test environment
_clean() {
  envdir=$1
  echo "Cleaning up VMs: In Progress"
  cd $envdir
  sudo terraform destroy
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform destroy "; exit 1; fi
  echo "Cleaning up VMs: Passed"
  cd ..
  sudo rm -rf $envdir
}
_get_list_dapis() {
  _login
  dApis=$(curl -s --location --request GET "https://portal.$domain/mbr/d-apis/list/$projectId?limit=100" \
    --header "Authorization: Bearer $bearer" | jq  -r ". | .dApis")
  len=$(echo $dApis | jq length)
  min=0
  randomInd=$(($RANDOM % $len + $min))
  dApi=$(echo "$dApis" | jq ".[$randomInd]" | jq ". | .appId, .appKey" | sed -z "s/\"//g; s/\n/,/g; s/,$//g;s/,/.eth-mainnet.$domain\//g")
  _dapiURL="https://$dApi"
  echo $_dapiURL
  #((len=len - 1))
  #for i in $( seq 0 $len); do
  # dApi=$(echo "$dApis" | jq ".[$i]" | jq ". | .appId, .appKey" | sed -z "s/\"//g; s/\n/,/g; s/,$//g;s/,/.eth-mainnet.massbitroute.dev\//g")
  #  echo "https://$dApi"
  #done
}
$@
