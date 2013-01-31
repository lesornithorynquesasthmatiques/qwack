#!/bin/bash

if [ -z "$MONGO_PATH" ]; then
  MONGO_PATH=/mongo/qwack-mongo
fi

mongod --dbpath=$MONGO_PATH
