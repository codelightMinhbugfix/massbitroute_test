#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
SCENARIO_ID="$(echo $RANDOM | md5sum | head -c 5)"
echo '-------------------------------------------------------'
echo "Run scenario ${BASH_SOURCE[0]} with ID $SCENARIO_ID----";
echo '-------------------------------------------------------'
