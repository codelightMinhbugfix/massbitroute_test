#!/bin/bash
BACKUP_DIR=./configs/
JENKINS_URL=http://localhost:8080/
#
# $1 - config dir
#
_backup_configs() {
  CONFIG_DIR=$1
  echo "Backup config from dir $CONFIG_DIR to $BACKUP_DIR"
  rm -rf $BACKUP_DIR/*
  mkdir -p $BACKUP_DIR
  cp $CONFIG_DIR/* $BACKUP_DIR
}

#
# $1 - config dir
#
_generate_configs() {
  CONFIG_DIR=$1
  python3 ./build_pipeline.py
  java -jar /home/massbit/jenkins/workspace/jenkins-cli.jar -s $JENKINS_URL -auth ${AUTH_CLI} reload-configuration
}

#
# $1 - config dir
#
_restore_configs() {
  cp $BACKUP_DIR/* $CONFIG_DIR/
  java -jar /home/massbit/jenkins/workspace/jenkins-cli.jar -s $JENKINS_URL -auth ${AUTH_CLI} reload-configuration
}
$@
