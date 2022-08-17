#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
cat $ROOT_DIR/git-docker-compose.yaml.template |  \
     #sed "s/\[\[RUN_ID\]\]/$network_number/g" | \
     sed "s/\[\[NETWORK_NUMBER\]\]/$network_number/g" | \
     sed "s/\[\[GIT_TAG\]\]/$GIT_TAG/g"  \
    > $ENV_DIR/git-docker-compose.yaml
#cp git-docker-compose.yaml $ENV/git-docker-compose.yaml
#Init docker
docker-compose -f $ENV_DIR/git-docker-compose.yaml up -d --force-recreate
sleep 10
docker exec -it mbr_git_$network_number rm -rf /massbit/massbitroute/app/src/sites/services/git/data
docker exec -it mbr_git_$network_number rm -rf /massbit/massbitroute/app/src/sites/services/git/vars
docker exec -it mbr_git_$network_number /massbit/massbitroute/app/src/sites/services/git/scripts/run _repo_init
