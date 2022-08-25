#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
source $ROOT_DIR/base.sh
dapi_counter=10
#-------------------------------------------
# Create project
#-------------------------------------------

_create_project() {
  now=$(date)
  bearer=$(cat /vars/BEARER)
  projectName=project_$projectPrefix
  projectId=$(curl -k --location --request POST "https://portal.$domain/mbr/d-apis/project" \
    --header "Authorization: Bearer  $bearer" \
    --header 'Content-Type: application/json' \
    --data-raw "{
      \"name\":\"$projectName\",
      \"blockchain\":\"$blockchain\",
      \"network\":\"$network\"}" | jq -r '. | .id');

  echo "----------------------------"
  echo "Project ID: $projectId"
  echo "----------------------------"
  echo $projectId > /vars/PROJECT_ID
  echo $projectName > /vars/PROJECT_NAME
}

_stake_project() {
  # stake gateway
  now=$(date)
  echo "Wait a minute for staking project. Current time $now ..."
  projectId=$(cat /vars/PROJECT_ID)

  staking_response=$(curl --location --request POST "http://staking.$domain/massbit/staking-project" \
    --header 'Content-Type: application/json' --data-raw "{
      \"memonic\": \"$MEMONIC\",
      \"projectId\": \"$projectId\",
      \"blockchain\": \"$blockchain\",
      \"network\": \"$network\",
      \"amount\": \"100\"
  }")
  echo "Staking response $staking_response";
  staking_status=$(echo $staking_response | jq -r ". | .status");

  if [[ "$staking_status" != "success" ]]; then
    echo "Project staking status: Failed "
    exit 1
  fi

  #provider_status=$(curl -k --location --request GET "https://portal.$domain/mbr/$providerType/$providerId" \
  #  --header "Authorization: Bearer $bearer" | jq -r ". | .status")
  #
  #now=$(date)
  #echo "---------------------------------"
  #echo "$providerType status at $now is $provider_status, expected status staked"
  #echo "---------------------------------"

  now=$(date)
  echo "Project staked: Passed at $now"
}
_create_dapi() {
  now=$(date)
  echo "Create dapi at $now ..."
  bearer=$(cat /vars/BEARER)
  projectId=$(cat /vars/PROJECT_ID)
  projectName=$(cat /vars/PROJECT_NAME)
  random=$(echo $RANDOM | md5sum | head -c 3)
  create_dapi_response=$(curl --location --request POST "$protocol://portal.$domain/mbr/d-apis" \
    --header "Authorization: Bearer $bearer" \
    --header 'Content-Type: application/json' \
    --data-raw "{
      \"name\": \"$projectName-$random\",
      \"projectId\": \"$projectId\"
    }")
  create_dapi_status=$(echo $create_dapi_response | jq .status)
  apiId=$(echo $create_dapi_response | jq -r '. | .entrypoints[0].apiId')
  appKey=$(echo $create_dapi_response | jq -r '. | .appKey')
  dapiURL="$protocol://$apiId.${blockchain}-$network.$domain/$appKey";
  echo $dapiURL > /vars/DAPI_URL

  echo "---------dAPIUrl-----------------"
  echo "$dapiURL"
  echo "---------------------------------"
}

_execute_apis_testing() {

}
_prepare_dapis() {
    #-------------------------------------------
    # Create dAPI
    #-------------------------------------------
    now=$(date)
    echo "Prepare dapis at $now ..."
    bearer=$(cat /vars/BEARER)
    projectId=$(cat /vars/PROJECT_ID)
    projectName=$(cat /vars/PROJECT_NAME)
    dApis=$(curl -s --location --request GET "https://portal.$domain/mbr/d-apis/list/$projectId?limit=100" \
      --header "Authorization: Bearer $bearer" | jq  -r ". | .dApis")
    len=$(echo $dApis | jq length)
    end=$(( $dapi_counter - 1 ))
    if [ $len -lt $dapi_counter ]; then
      for i in $( seq $len $end );
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
        dapiURL="$protocol:\/\/$apiId.${blockchain}-$network.$domain\/$appKey"
        if [[ "$create_dapi_status" != "1" ]]; then
          echo "Create new dAPI: Failed"
          exit 1
        else
          echo "Create new dAPI: Passed"
        fi

      done
    fi
}

$@
