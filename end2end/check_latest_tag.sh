#!/bin/bash
export RUNTIME_DIR="/massbit/test_runtime"
mkdir -p $RUNTIME_DIR/test_runtime 
mkdir "latest_tag"
gateway=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_gateway.git | tail -n1 | cut -d/ -f3 )
node=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_node.git | tail -n1 | cut -d/ -f3 )
stat=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_stat.git | tail -n1 | cut -d/ -f3 )
monitor=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_monitor.git | tail -n1 | cut -d/ -f3 )
git=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_git.git | tail -n1 | cut -d/ -f3 )
chain=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitchain.git | tail -n1 | cut -d/ -f3 )
fisherman=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_fisherman.git| tail -n1 | cut -d/ -f3 )
staking=$(git ls-remote --tags --sort='v:refname' git@github.com:mison201/test-massbit-staking.git | tail -n1 | cut -d/ -f3 )
portal=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/user-management.git | tail -n1 | cut -d/ -f3 ) 
web=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/mbr-app.git | tail -n1 | cut -d/ -f3 )
api=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute.git | tail -n1 | cut -d/ -f3 )
gwman=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_gwman.git | tail -n1 | cut -d/ -f3 )

echo $api > latest_tag/API_TAG
echo $gateway > latest_tag/GATEWAY_TAG
echo $node > latest_tag/NODE_TAG
echo $stat > latest_tag/STAT_TAG
echo $monitor > latest_tag/MONITOR_TAG
echo $git > latest_tag/GIT_TAG
echo $gwman > latest_tag/GWMAN_TAG
echo $chain > latest_tag/MASSBIT_CHAIN_TAG
echo $fisherman > latest_tag/FISHERMAN_TAG
echo $staking > latest_tag/STAKING_TAG
echo $portal > latest_tag/PORTAL_TAG
echo $web > latest_tag/WEB_TAG
