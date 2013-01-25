#!/bin/bash

# Usage:
# start=`date +%s`
# ...some work
# end=`date +%s`
# elapsed=`elapsedTime $start $end`
# formatted=`formatElapsedTime $elapsed`
# echo $formatted
# Return the elapsed time in seconds between two timestamps
function elapsedTime {
        start=$1
        end=$2
        elapsed=`expr $end - $start`
        echo $elapsed
}

# Formats a duration in seconds
function formatElapsedTime {
        elapsed=$1
        if [[ $elapsed -lt 60 ]]
        then
                echo "$elapsed seconds"
        else
                if [[ $elapsed -lt 3600 ]]
                then
                        minutes=`expr $elapsed / 60`
                        seconds=`expr $elapsed % 60`
                        echo "$minutes minutes $seconds seconds"
                else
                        if [[ $elapsed -lt 86400 ]]
                        then
                                hours=`expr $elapsed / 3600`
                                remaining=`expr $elapsed % 3600`
                                minutes=`expr $remaining / 60`
                                seconds=`expr $remaining % 60`
                                echo "$hours hours $minutes minutes $seconds seconds"
                        else
                                days=`expr $elapsed / 86400`
                                remaininghours=`expr $elapsed % 86400`
                                hours=`expr $remaininghours / 3600`
                                remainingminutes=`expr $remaininghours % 3600`
                                minutes=`expr $remainingminutes / 60`
                                seconds=`expr $remainingminutes % 60`
                                echo "$days days $hours hours $minutes minutes $seconds seconds"
                        fi

                fi
        fi
}

function setupClasspath {
	BATCH_HOME=$1
	CLASSPATH="$BATCH_HOME/config"
	for i in $BATCH_HOME/lib/*.jar; do
	    CLASSPATH="$CLASSPATH:$i"
	done
	
	# convert from Windows to Unix style path.
	case "`uname -s`" in
	    CYGWIN*)
	        CLASSPATH=`cygpath -wp "$CLASSPATH"`
	    ;;
	esac
}

function setupLibraryPath {
	BATCH_HOME=$1
	export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$BATCH_HOME/jni/linux64"
}

function addCommonJavaOptions {
	[[ ! $QWACK_BATCH_JAVA_OPTS == *-Djava.awt.headless=* ]]         && QWACK_BATCH_JAVA_OPTS="$QWACK_BATCH_JAVA_OPTS -Djava.awt.headless=true"
	[[ ! $QWACK_BATCH_JAVA_OPTS == *-Dfile.encoding=* ]]             && QWACK_BATCH_JAVA_OPTS="$QWACK_BATCH_JAVA_OPTS -Dfile.encoding=UTF-8"
	[[ ! $QWACK_BATCH_JAVA_OPTS == *-Dlogback.configurationFile=* ]] && QWACK_BATCH_JAVA_OPTS="$QWACK_BATCH_JAVA_OPTS -Dlogback.configurationFile=$BATCH_HOME/config/logback.xml"
	[[ ! $QWACK_BATCH_JAVA_OPTS == *-Duser.timezone=* ]]             && QWACK_BATCH_JAVA_OPTS="$QWACK_BATCH_JAVA_OPTS -Duser.timezone=UTC"
}

SEPARATOR="********************************************************************************"

function printStartBanner {
	BATCH_NAME=$1
	
	START_TIME=`date +%s`
	
	echo
	echo "$SEPARATOR"
	echo "Launching $BATCH_NAME..."
	echo "Start Time                    = `date '+%Y-%m-%d %H:%M:%S'`"
	echo "JAVA_HOME                     = $JAVA_HOME"
	echo "QWACK_BATCH_JVM_ARGS          = $QWACK_BATCH_JVM_ARGS"
	echo "QWACK_BATCH_JAVA_OPTS         = $QWACK_BATCH_JAVA_OPTS"
	echo "LD_LIBRARY_PATH               = $LD_LIBRARY_PATH"
}

function printEndBanner {
	BATCH_NAME=$1
	RETVAL=$2
	
	END_TIME=`date +%s`
	ELAPSED=`elapsedTime $START_TIME $END_TIME`
	FORMATTED_TIME=`formatElapsedTime $ELAPSED`
	
	echo
	echo "$SEPARATOR"
	echo "$BATCH_NAME finished, status = $RETVAL."
	echo "End Time = `date '+%Y-%m-%d %H:%M:%S'`"
	echo "Total Elapsed Time = $FORMATTED_TIME"
	echo "$SEPARATOR"
}

function launchJavaBatch {
	BATCH_HOME=$1        ; shift
	BATCH_MAIN_CLASS=$1  ; shift
	BATCH_DESCRIPTION=$1 ; shift
	PID_FILE=$1          ; shift
	
	setupClasspath $BATCH_HOME
	setupLibraryPath $BATCH_HOME
	addCommonJavaOptions
	
	printStartBanner "$BATCH_DESCRIPTION"

	$JAVA_HOME/bin/java \
		$QWACK_BATCH_JVM_ARGS \
		-classpath "$CLASSPATH" \
		$QWACK_BATCH_JAVA_OPTS \
		$BATCH_MAIN_CLASS \
		$* \
		&
		
	PID=$!
	echo $PID >$BATCH_HOME/$PID_FILE 
	trap "kill -9 $PID" INT   # Make sure Ctrl+C also stops the Java process 
	
	wait $PID
	RETVAL=$?
	rm -rf $PID_FILE
	
	printEndBanner "$BATCH_DESCRIPTION" $RETVAL
}