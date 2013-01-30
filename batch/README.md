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

## Maven

You need to install 3 artifacts manually, they are locate inside the binary distribution under "lib" directory or use install_hdf5_libs.sh:

    mvn install:install-file -Dfile=/path/to/hdf-java/lib/jhdf5.jar -DgroupId=org.hdfgroup -DartifactId=jhdf5 -Dversion=2.9 -Dpackaging=jar
    mvn install:install-file -Dfile=/path/to/hdf-java/lib/jhdf5obj.jar -DgroupId=org.hdfgroup -DartifactId=jhdf5obj -Dversion=2.9 -Dpackaging=jar
    mvn install:install-file -Dfile=/path/to/hdf-java/lib/jhdfobj.jar -DgroupId=org.hdfgroup -DartifactId=jhdfobj -Dversion=2.9 -Dpackaging=jar

Or just launch the provided script install_hdf5_libs.sh:

	./install_hdf5_libs.sh
	
## HDF Native Libraries

HDF native libraries (.so or .dll files) are located inside the binary distribution under "lib/**" directory. They are also included in this project under ./hdf-java/lib/[osname].

They need to be specified as extensions when launching the JVM. To do so you either:

1. Copy the native libraries into the $JAVA_HOME/jre/lib/ext folder (on Mac OS, this is located under /Library/Java/Extensions).
2. Set up a PATH (Windows) or LD_LIBRARY_PATH (unix) environment variable pointing to the native libraries.
3. Specify the system property "-Djava.library.path=xxx" pointing to the native libraries when launching the JVM.

E.g. to run Maven tests on Windows:

    mvn test -DargLine="-Djava.library.path=/path/to/hdf-java/lib/win"

For further info about HDF5 native libraries, see:
http://www.hdfgroup.org/hdf-java-html/hdf-object/use.html

## Deployment

### Mongo

1. Donwload and Install Mongo 2.2 or higher
2. Run bin/mongod --dbpath /path/to/data/dir/
3. Ensure indexes by running the script ./src/mongo/indexes.js
4. Query:
	use main
	db.songs.count()
	db.songs.find({"artist.name" : "Rick Astley"},{"title":1,"release":1,year:1})

### Solr

1. Unzip ./src/main/solr/qwack-solr.zip
2. Copy ./src/main/solr/solr.xml to qwack-solr/solr
3. Copy ./src/main/solr/solrconfig.xml to qwack-solr/solr/songs/conf
4. Copy ./src/main/solr/schema.xml to qwack-solr/solr/songs/conf
5. Run java -Djava.util.logging.config.file=etc/logging.properties -jar start.jar
6. Browse http://localhost:8983/solr/

### Batch

Unzip the distribution file.
The main executable is ./bin/songs.sh.
Type ./bin/songs.sh -? for help.
Typical command (using defaults):

	bin/songs.sh -d /path/to/MillionSongSubset/data

To launch the batch without Solr indexation:

	bin/songs.sh -d /path/to/MillionSongSubset/data --disable-solr

