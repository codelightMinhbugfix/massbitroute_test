#!/bin/bash
export RUNTIME_DIR=/massbit/test_runtime
mkdir -p ${RUNTIME_DIR}/${network_number}/tags/
#random=$(echo $RANDOM | md5sum | head -c 5)
_read_latest_git_tags(){
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
  session=$(git ls-remote --tags --sort='v:refname' git@github.com:massbitprotocol/massbitroute_session.git | tail -n1 | cut -d/ -f3 )
  echo "API - $api"
  echo "Gateway - $gateway"
  echo "Node - $node"
  echo "Stat - $stat"
  echo "Monitor - $monitor"
  echo "Git - $git"
  echo "Gwman - $gwman"
  echo "Chain - $chain"
  echo "fisherman - $fisherman"
  echo "Staking - $staking"
  echo "Portal - $portal"
  echo "Web - $web"

  echo $gateway > $RUNTIME_DIR/${network_number}/tags/GATEWAY
  echo $node > $RUNTIME_DIR/${network_number}/tags/NODE
  echo $stat >  $RUNTIME_DIR/${network_number}/tags/STAT
  echo $git > $RUNTIME_DIR/${network_number}/tags/GIT
  echo $chain > $RUNTIME_DIR/${network_number}/tags/CHAIN
  echo $fisherman >  $RUNTIME_DIR/${network_number}/tags/FISHERMAN
  echo $staking > $RUNTIME_DIR/${network_number}/tags/STAKING
  echo $portal > $RUNTIME_DIR/${network_number}/tags/PORTAL
  echo $web >  $RUNTIME_DIR/${network_number}/tags/WEB
  echo $api > $RUNTIME_DIR/${network_number}/tags/API
  echo $gwman > $RUNTIME_DIR/${network_number}/tags/GWMAN
  echo $session >  $RUNTIME_DIR/${network_number}/tags/SESSION
}

$@
