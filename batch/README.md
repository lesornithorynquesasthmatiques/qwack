# Overview of HDF5

http://www.hdfgroup.org/HDF5/doc/H5.intro.html

# Documentation - HDF and Java

http://www.hdfgroup.org/hdf-java-html/
Examples by API: http://www.hdfgroup.org/ftp/HDF5/examples/examples-by-api/api18-java.html
Examples: http://www.hdfgroup.org/ftp/HDF5/hdf-java/src/hdf-java/examples/
Datatypes: http://www.hdfgroup.org/HDF5/Tutor/datatypes.html
Compound types: http://www.hdfgroup.org/hdf-java-html/JNI/jhi5/compound.html
CoumpoundDS: http://www.hdfgroup.org/hdf-java-html/javadocs/ncsa/hdf/object/h5/H5CompoundDS.html

# Installation

## Download

Download HDF-JAVA 2.9:
binary: http://www.hdfgroup.org/ftp/HDF5/hdf-java/bin
source: http://www.hdfgroup.org/ftp/HDF5/hdf-java/src/hdf-java-2.9-src.tar

## Building & Running with Maven

You need to install 3 artifacts manually, they are locate inside the binary distribution under "lib" directory or use install_hdf5_libs.sh:

    mvn install:install-file -Dfile=/path/to/hdf-java/lib/jhdf5.jar -DgroupId=org.hdfgroup -DartifactId=jhdf5 -Dversion=2.9 -Dpackaging=jar
    mvn install:install-file -Dfile=/path/to/hdf-java/lib/jhdf5obj.jar -DgroupId=org.hdfgroup -DartifactId=jhdf5obj -Dversion=2.9 -Dpackaging=jar
    mvn install:install-file -Dfile=/path/to/hdf-java/lib/jhdfobj.jar -DgroupId=org.hdfgroup -DartifactId=jhdfobj -Dversion=2.9 -Dpackaging=jar

Tests need to be launched with the appropriate native libraries (.so or .dll files), located inside the binary distribution under "lib" or "lib/win" directory:
To run tests:

    mvn test -DargLine="-Djava.library.path=/path/to/hdf-java/lib/win"

## Building & Running with Eclipse

Tests must include either:

1. a PATH environment variable pointing to the native libraries (see above).
2. The system property "-Djava.library.path=xxx" pointing to the native libraries (see above).

## Deployment

Change environment variable PATH to include HDF native libraries (see above).

For further info about HDF5 native libraries, see:
http://www.hdfgroup.org/hdf-java-html/hdf-object/use.html
