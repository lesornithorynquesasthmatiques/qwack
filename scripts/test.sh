#!/bin/bash

SCRIPT_DIR=`dirname $0`

mocha --watch "$SCRIPT_DIR/../test"

