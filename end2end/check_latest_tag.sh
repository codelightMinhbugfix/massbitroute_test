#!/bin/bash
export RUNTIME_DIR=/massbit/test_runtime
mkdir -p .vars/
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

  echo $gateway > .vars/gateway
  echo $node > .vars/node
  echo $stat >  .vars/stat
  echo $git > .vars/git
  echo $chain > .vars/chain
  echo $fisherman >  .vars/fisherman
  echo $staking > .vars/staking
  echo $portal > .vars/portal
  echo $web >  .vars/web
  echo $api > .vars/api
  echo $gwman > .vars/gwman
  echo $session >  .vars/session
}

$@
