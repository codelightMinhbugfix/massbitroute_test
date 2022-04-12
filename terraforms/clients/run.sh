#! /bin/bash
source .env
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

_prepare_terraform() {
  testDapis=()
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
  }' > client.tf

  while IFS="," read -r region zone
  do
    #create dapi for each client
    random=$(echo $RANDOM | md5sum | head -c 5)
    testDapis+=( $random )
    #-------------------------------------------
    # Create dAPI
    #-------------------------------------------
    create_dapi_response=$(curl -s --location --request POST 'https://portal.massbitroute.dev/mbr/d-apis' \
      --header "Authorization: Bearer $bearer" \
      --header 'Content-Type: application/json' \
      --data-raw "{
        \"name\": \"$projectName-$random\",
        \"projectId\": \"$projectId\"
    }")
    apiId=$(echo $create_dapi_response | jq -r '. | .entrypoints[0].apiId')
    appKey=$(echo $create_dapi_response | jq -r '. | .appKey')
    dapiURL="https:\/\/$apiId.${blockchain}-mainnet.$DOMAIN\/$appKey"
    create_dapi_status=$(echo $create_dapi_response | jq .status)
    if [[ "$create_dapi_status" != "1" ]]; then
      echo "Create new dAPI: Failed"
      exit 1
    else
      echo "Create new dAPI: Passed"
    fi
    cat init.tpl | sed "s/\[\[BEARER\]\]/$bearer/g" \
              | sed "s/\[\[ZONE\]\]/$zone/g" \
              | sed "s/\[\[PROJECT_ID\]\]/$projectId/g" \
              | sed "s/\[\[PROJECT_NAME\]\]/${projectName}/g" \
              | sed "s/\[\[BLOCKCHAIN\]\]/$blockchain/g" \
              | sed "s/\[\[DAPI_URL\]\]/$dapiURL/g"> init_$random.sh
    cat client.template |  sed "s/\[\[REGION\]\]/$region/g" \
                        | sed  "s/\[\[ZONE\]\]/$zone/g" \
                        | sed  "s/\[\[EMAIL\]\]/$email/g" \
                        | sed "s/\[\[INIT_FILE\]\]/$random/g" >> client.tf
  done < <(tail zonelist.csv)
}

_create_vms() {
  terraform init
  terraform plan -var-file=./terraform.tfvars -out=client.plan
  terraform apply client.plan
}
_clean_init_files() {
  for file in ${testDapis[@]}; do
    rm init_${file}.sh
  done
}
_setup() {
  _login
  _prepare_terraform
  _create_vms
#  _clean_init_files
}
_clean() {
  _login
  echo "Cleaning up VMs: In Progress"
  sudo terraform destroy
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform destroy "; exit 1; fi
  echo "Cleaning up VMs: Passed"
}

$@
