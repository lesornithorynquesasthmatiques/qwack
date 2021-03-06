#!/bin/sh

HDF5_DIR=$PWD/hdf-java/lib

MAVEN_HDF5_GROUPID="org.hdfgroup"
MAVEN_HDF5_VERSION="2.9"

function mvnInstall(){
    mvn install:install-file -Dfile="$HDF5_DIR/$1.jar" -DgroupId="$MAVEN_HDF5_GROUPID" -DartifactId=$1 -Dversion=$MAVEN_HDF5_VERSION -Dpackaging=jar
}

mvnInstall jhdf
mvnInstall jhdf4obj
mvnInstall jhdf5
mvnInstall jhdf5obj
mvnInstall jhdfobj
