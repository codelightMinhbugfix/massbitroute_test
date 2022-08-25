#! /bin/bash

ethereum_api=$(cat ./ethereum/input/ethereum-latency-api.json)

failed=0
passed=0

for i in $(seq 1 $NUMBER_OF_TESTS); do
  for j in $(seq 0 $(($(jq length <<<$ethereum_api) - 1))); do
    method=$(echo $ethereum_api | jq .[$j].method)
    params=$(echo $ethereum_api | jq .[$j].params)

    body="{\"jsonrpc\": \"2.0\", \"method\": $method, \"params\": $params, \"id\": 67}"

    response=$(curl $MASSBIT_ROUTE_ETHEREUM \
      --silent \
      --header "Content-Type: application/json" \
      --request POST \
      --data "$body" | jq -S 'del(.jsonrpc, .id)')
    expected_response=$(curl $ANOTHER_ETHEREUM_PROVIDER \
      --silent \
      --header "Content-Type: application/json" \
      --request POST \
      --data "$body" | jq -S 'del(.jsonrpc, .id)')

    if [[ "$response" != "$expected_response" ]]; then
      failed=$(($failed + 1))
    else
      passed=$(($passed + 1))
    fi
  done
done

report="{
  \"date\": \"$(date)\",
  \"loopCount\": $NUMBER_OF_TESTS,
  \"passed\": \"$passed/$(($passed + $failed))\",
  \"failed\": \"$failed/$(($passed + $failed))\"
}"

if ! [[ -f "$REPORT_DIR/ethereum-latency-report.json" ]]; then
  touch "$REPORT_DIR/ethereum-latency-report.json"
fi

echo "[ $report ]" >temp.json
merge_report=$(jq -s add temp.json "$REPORT_DIR/ethereum-latency-report.json")
echo $merge_report | jq '.' >$REPORT_DIR/ethereum-latency-report.json
rm temp.json
