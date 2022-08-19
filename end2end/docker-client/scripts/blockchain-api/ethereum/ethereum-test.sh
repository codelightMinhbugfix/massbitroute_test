#! /bin/bash

ethereum_api=$(cat ./ethereum/input/ethereum-api.json)
all_test_case="[]"

_generate_test_case() {
  while [[ $(echo $all_test_case | jq length) < $num_of_test_case ]]; do
    latest_block_info=$(
      curl $infura \
        --silent \
        --header 'Content-Type: application/json' \
        --request POST \
        --data '{
        "id": 1, 
        "jsonrpc": "2.0",
        "method": "eth_getBlockByNumber",
        "params": [ "latest", true ]
      }'
    )

    data=$(echo $latest_block_info | jq '.result.sha3Uncles')
    block_hash=$(echo $latest_block_info | jq '.result.transactions[0].blockHash')
    block_number=$(echo $latest_block_info | jq '.result.transactions[0].blockNumber')
    transaction=$(echo $latest_block_info | jq '.result.transactions[0] | del(.input, .accessList, .r, .s, .v, .chainId, .maxFeePerGas, .maxPriorityFeePerGas, .type, .nonce, .blockHash, .blockNumber)')
    is_full_data=false

    if ! [[ -n "$transaction" ]]; then
      continue
    fi

    test_case="{
      \"data\": $data, 
      \"blockHash\": $block_hash, 
      \"blockNumber\": $block_number, 
      \"object\": $transaction, 
      \"isFullData\": $is_full_data
    }"

    all_test_case=$(echo "$all_test_case" | jq ". += [$test_case]")
    all_test_case=$(echo "$all_test_case" | jq ". | unique")
  done
}

_generate_test_case

echo $all_test_case >./ethereum/input/ethereum-testcase.json

report="[]"
sum_both_error=0
sum_error=0
sum_passed=0
sum_failed=0

for k in $(seq 0 $(($(jq length <<<$all_test_case) - 1))); do
  both_error=0
  error=0
  passed=0
  failed=0

  both_error_report="[]"
  error_report="[]"
  failed_report="[]"
  passed_report="[]"
  test_case=$(jq .[$k] <<<$all_test_case)

  for i in $(seq 0 $(($(jq length <<<$ethereum_api) - 1))); do
    method=$(echo $ethereum_api | jq .[$i].method)
    params=$(echo $ethereum_api | jq .[$i].params)
    expect=$(echo $ethereum_api | jq .[$i].expectMatch)
    new_params="[]"

    for j in $(seq 0 $(($(jq length <<<$params) - 1))); do
      key=$(echo $params | jq -r .[$j])
      data=$(echo $test_case | jq .$key)
      new_params=$(echo "$new_params" | jq ". += [$data]")
    done

    body="{\"jsonrpc\": \"2.0\", \"method\": $method, \"params\": $new_params, \"id\": 67}"

    response=$(curl $data_source_mainnet \
      --silent \
      --header "Content-Type: application/json" \
      --request POST \
      --data "$body" | jq -S 'del(.jsonrpc, .id)')
    expected_response=$(curl $infura \
      --silent \
      --header "Content-Type: application/json" \
      --request POST \
      --data "$body" | jq -S 'del(.jsonrpc, .id)')

    if [[ $response != *"result"* && $expected_response != *"result"* ]]; then
      both_error=$((both_error + 1))
      both_error_report=$(echo $both_error_report | jq ". += [$method]")

      echo "==================== BOTH ERROR ==================="
      echo "method : $method"
      echo "response : $response"
      echo "expected_response : $expected_response"
    elif [[ $response != *"result"* ]]; then
      error=$((error + 1))
      error_report=$(echo $error_report | jq ". += [$method]")

      echo "==================== ERROR ===================="
      echo "method : $method"
      echo "response : $response"
      echo "expected_response : $expected_response"
    elif [[ "$response" != "$expected_response" ]]; then
      if [[ $expect == true ]]; then
        failed=$((failed + 1))
        failed_report=$(echo $failed_report | jq ". += [$method]")

        echo "==================== OUTPUT DIFF ===================="
        echo "method : $method"
        diff --color <(echo "$response") <(echo "$expected_response")
      else
        passed=$((passed + 1))
        passed_report=$(echo $passed_report | jq ". += [$method]")
      fi
    else
      passed=$((passed + 1))
      passed_report=$(echo $passed_report | jq ". += [$method]")
    fi
  done

  sum=$(($both_error + $error + $failed + $passed))
  test_case_report="{
    \"passed\": {
      \"ratio\": \"$passed/$sum\",
      \"method\": $passed_report
    },
    \"failed\": {
      \"ratio\": \"$failed/$sum\",
      \"method\": $failed_report
    },
    \"error\": {
      \"ratio\": \"$error/$sum\",
      \"method\": $error_report
    },
    \"bothError\": {
      \"ratio\": \"$both_error/$sum\",
      \"method\": $both_error_report
    },
  }"
  report=$(echo $report | jq ". += [$test_case_report]")
  sum_both_error=$(($sum_both_error + $both_error))
  sum_error=$(($sum_error + $error))
  sum_passed=$(($sum_passed + $passed))
  sum_failed=$(($sum_failed + $failed))
done

sum=$(($sum_both_error + $sum_error + $sum_passed + $sum_failed))
report="{
  \"date\": \"$(date)\",
  \"loopCount\": $num_of_test_case,
  \"passed\": \"$sum_passed/$sum\",
  \"failed\": \"$sum_failed/$sum\",
  \"error\": \"$sum_error/$sum\",
  \"bothError\": \"$sum_both_error/$sum\",
  \"result\": $report
}"

if ! [[ -f "$report_dir/ethereum-report.json" ]]; then
  touch "$report_dir/ethereum-report.json"
fi

echo "[ $report ]" >temp.json
merge_report=$(jq -s add temp.json "$report_dir/ethereum-report.json")
echo $merge_report | jq . >$report_dir/ethereum-report.json
rm temp.json
