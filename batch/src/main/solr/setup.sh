#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: setup.sh SOLR_DESTINATION_DIR"
  exit 1
fi

START_DIR=`pwd`
SCRIPT_DIR=`pwd $(dirname $0)`
SOLR_PARENT_DIR=$1
SOLR_DIR="$SOLR_PARENT_DIR/qwack-solr"

unzip "$SCRIPT_DIR/qwack-solr.zip" -d $SOLR_PARENT_DIR

cd "$SOLR_DIR"

cp "$SCRIPT_DIR/solr.xml" solr
cp "$SCRIPT_DIR/solrconfig.xml" solr/songs/conf
cp "$SCRIPT_DIR/schema.xml" solr/songs/conf

java -Djava.util.logging.config.file=etc/logging.properties -jar start.jar &

cd $START_DIR

