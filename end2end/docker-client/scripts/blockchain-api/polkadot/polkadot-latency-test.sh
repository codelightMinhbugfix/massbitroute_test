#! /bin/bash

polkadot_api=$(jq . <./polkadot/input/polkadot-latency-api.json)

failed=0
passed=0

for i in $(seq 1 $num_of_test_case); do
  for j in $(seq 0 $(($(jq length <<<$polkadot_api) - 1))); do
    method=$(echo $polkadot_api | jq .[$j].method)
    params=$(echo $polkadot_api | jq .[$j].params)

    body="{\"jsonrpc\": \"2.0\", \"method\": $method, \"params\": $params, \"id\": 1}"

    response=$(curl $data_source_polkadot \
      --silent \
      --header "Content-Type: application/json" \
      --request POST \
      --data "$body" | jq -S 'del(.jsonrpc, .id)')
    expected_response=$(curl $polka \
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
  \"loopCount\": $num_of_test_case,
  \"passed\": \"$passed/$(($passed + $failed))\",
  \"failed\": \"$failed/$(($passed + $failed))\"
}"

if ! [[ -f "$report_dir/polkadot-latency-report.json" ]]; then
  touch "$report_dir/polkadot-latency-report.json"
fi

echo "[ $report ]" >temp.json
merge_report=$(jq -s add temp.json "$report_dir/polkadot-latency-report.json")
echo $merge_report >$report_dir/polkadot-latency-report.json
rm temp.json
