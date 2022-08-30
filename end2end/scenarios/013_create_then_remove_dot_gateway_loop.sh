#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
SCENARIO_ID="$(echo $RANDOM | md5sum | head -c 5)"
echo '-------------------------------------------------------------'
echo "Run scenario ${BASH_SOURCE[0]} with ID $SCENARIO_ID ---------"
echo '-------------------------------------------------------------'
LOOP=10
for (( c=1; c<=$LOOP; c++ ))
do
   $ROOT_DIR/013_create_then_remove_dot_gateway.sh
done
