#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))
BACKUP_DIR=$ROOT_DIR/../../configs/
JENKINS_URL=http://127.0.0.1:8080/
#
# $1 - config dir
#
_backup_configs() {
  export CONFIG_DIR=$1
  echo "Backup config from dir $CONFIG_DIR to $BACKUP_DIR"
  rm -rf $BACKUP_DIR/*
  mkdir -p $BACKUP_DIR
  cp -r $CONFIG_DIR/* $BACKUP_DIR
}

#
# $1 - config dir
#
_generate_configs() {
  export CONFIG_DIR=$1
  python3 ./build_pipeline.py
  java -jar /home/massbit/jenkins/jenkins-cli.jar -s $JENKINS_URL -auth ${AUTH_CLI} reload-configuration
}

#
# $1 - config dir
#
_restore_configs() {
  export CONFIG_DIR=$1
  cp -r $BACKUP_DIR/* $CONFIG_DIR/
  java -jar /home/massbit/jenkins/jenkins-cli.jar -s $JENKINS_URL -auth ${AUTH_CLI} reload-configuration
}
$@
