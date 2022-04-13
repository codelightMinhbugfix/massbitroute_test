#!/bin/bash
sudo apt update
sudo apt install -y jq
sudo mkdir /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_monitor/raw/master/scripts/benchmark/wrk -P /opt/benchmark
sudo wget https://github.com/massbitprotocol/massbitroute_monitor/raw/master/scripts/benchmark/massbit.lua -P /opt/benchmark
#sudo wget https://raw.githubusercontent.com/massbitprotocol/massbitroute_monitor/master/scripts/benchmark/massbit.lua -P /opt/benchmark
chmod +x /opt/benchmark/wrk

random=$(echo $RANDOM | md5sum | head -c 5)
#-------------------------------------------
# Create dAPI
#-------------------------------------------
create_dapi_response=$(curl -s --location --request POST 'https://portal.massbitroute.dev/mbr/d-apis' \
  --header "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2ODAwNTQwMC0xNjdhLTRmMGMtODYzMS00ZGI2MDM5Y2M2MDYiLCJpYXQiOjE2NDkzMTk1NzUsImV4cCI6MTY0OTQwNTk3NX0.KhagooMMw8TLkw-KCsJZyOJXH675CN08DFBwp1fsbhWedxumefXHMs_le_goujB5K3U03JCiFUkGt8qpjnYQcwEVTAqx9JpkhC_hYuwtAAUfPov12eDgTzVeMi-Gayl1nPad0Djsi-vJaOxDxUyJj_aTvx-fM2U1OvXTYNGkPcGLimZVoIk8cFASRCPhJHTfMriC__qOJVdNh1u0xhQxII4Y1bmrirMYGXmSOzmUnaMxZZsYnwZ9gtAKKaMqW6azzxBC6N4nGBGfab9-jrKzXPkweKleMI2EjtdFibHcn4QarP1x40089TKdhm74DzFeIgjwJUMIdAmScOXe70knwA" \
  --header 'Content-Type: application/json' \
  --data-raw "{
    \"name\": \"mbr-dev-prj-eth01-$random\",
    \"projectId\": \"434e45dc-fbbb-4008-b647-40eaa4c112e5\"
}")
create_dapi_status=$(echo $create_dapi_response | jq .status)
if [[ "$create_dapi_status" != "1" ]]; then
  echo "Create new dAPI: Failed"
  exit 1
fi
echo "Create new dAPI: Passed"

sleep 30
#-------------------------------------------
# Test call dAPI
#-------------------------------------------
apiId=$(echo $create_dapi_response | jq -r '. | .entrypoints[0].apiId')
appKey=$(echo $create_dapi_response | jq -r '. | .appKey')
dapiURL="https://$apiId.eth-mainnet.massbitroute.dev/$appKey"

dapi_response_code=$(curl -o /dev/null -s -w "%{http_code}\n" --request POST $dapiURL \
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
if [[ "$dapi_response_code" != "200" ]]; then
  echo "Calling dAPI: Failed"
  exit 1
fi
echo "Calling dAPI: Pass"

thread=100
connection=100
duration=160s
rate=10000

response=$(/opt/benchmark/wrk -t$thread -c$connection -d$duration -R$rate -s /tmp/benchmark/massbit.lua $dapiURL)
echo $response

exit 0

curl 'https://docs.google.com/forms/d/1gzn6skD5MH7D3cyIsv8qcbkbox6QRcxzhkT9AomXE8o/formResponse' --data 'entry.1089136036=Some value' --silent >/dev/null