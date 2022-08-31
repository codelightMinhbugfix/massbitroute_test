#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
LOOP=${1:-1}

#NETWORK defined in base
source $ROOT_DIR/base.sh

bash -x prepare_runtime.sh
#Prepare nginx config for proxy
bash -x prepare_proxy.sh
#create git docker
bash -x create_git.sh
#create other dockers: core + portal + admin
bash -x create_docker_compose.sh
bash -x create_stat_docker_compose.sh
bash -x run_component_api_test.sh

bash $ENV_DIR/run_test_scenarios.sh

touch $ENV_DIR/.deletable
#clean up test environment
#bash -x cleanup.sh $network_number
