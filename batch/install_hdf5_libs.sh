#!/bin/sh

if [ $# -ne 1 ]
then
  echo "Specify the hdf5 lib directory"
  exit 1
fi

HDF5_DIR=$1

MAVEN_HDF5_GROUPID="org.hdfgroup"
MAVEN_HDF5_VERSION="2.9"

function mvnInstall(){
    mvn install:install-file -Dfile="$HDF5_DIR/$1.jar" -DgroupId="MAVEN_HDF5_GROUPID" -DartifactId=$1 -Dversion=$MAVEN_HDF5_VERSION -Dpackaging=jar
}

mvnInstall jhdf
mvnInstall jhdf4obj
mvnInstall jhdf5
mvnInstall jhdf5obj
mvnInstall jhdfobj