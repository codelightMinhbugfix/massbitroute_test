#! /bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
polkadot_api=$(jq . < $ROOT_DIR/input/polkadot-latency-api.json)

failed=0
passed=0

for i in $(seq 1 $NUMBER_OF_TESTS); do
  for j in $(seq 0 $(($(jq length <<<$polkadot_api) - 1))); do
    method=$(echo $polkadot_api | jq .[$j].method)
    params=$(echo $polkadot_api | jq .[$j].params)

    body="{\"jsonrpc\": \"2.0\", \"method\": $method, \"params\": $params, \"id\": 1}"

    response=$(curl $MASSBIT_ROUTE_POLKADOT \
      --silent \
      --header "Content-Type: application/json" \
      --request POST \
      --data "$body" | jq -S 'del(.jsonrpc, .id)')
    expected_response=$(curl $ANOTHER_POLKADOT_PROVIDER \
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

echo "====================== RESULT ======================"
echo "Polkadot JSON RPC API Latency Test"
echo "Failed: $failed"
echo "Passed: $passed"

report="{
  \"date\": \"$(date)\",
  \"loopCount\": $NUMBER_OF_TESTS,
  \"passed\": \"$passed/$(($passed + $failed))\",
  \"failed\": \"$failed/$(($passed + $failed))\"
}"

if ! [[ -f "$REPORT_DIR/polkadot-latency-report.json" ]]; then
  touch "$REPORT_DIR/polkadot-latency-report.json"
fi

echo "[ $report ]" >temp.json
merge_report=$(jq -s add temp.json "$REPORT_DIR/polkadot-latency-report.json")
echo $merge_report | jq '.' >$REPORT_DIR/polkadot-latency-report.json
rm temp.json
