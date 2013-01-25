#!/bin/bash

cd "`dirname $0`/.."
BATCH_HOME=$PWD

. $BATCH_HOME/bin/functions.sh

BATCH_DESCRIPTION="Qwack H5 File Ingestion Batch"
BATCH_MAIN_CLASS="org.lesornithorynquesasthmatiques.batch.Main"
PID_FILE=`basename $0`.pid

launchJavaBatch \
	$BATCH_HOME \
	$BATCH_MAIN_CLASS \
	"$BATCH_DESCRIPTION" \
	$PID_FILE \
	$*

exit $RETVAL
