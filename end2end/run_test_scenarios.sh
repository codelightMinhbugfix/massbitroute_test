#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
export ENV_DIR=$ROOT_DIR
export network_number=[[NETWORK_NUMBER]]

for file in scenarios-enable/*;
do
  if [  -f $file ];then
    bash $file
  fi
done

#WORKING_DIR=scenarios
#bash -x $WORKING_DIR/create_node.sh
#bash -x $WORKING_DIR/create_node.sh

#ls scenarios/*.sh |  while file;do
#  if [  -f $file ];then
#  bash -x $file
#  fi
#done
