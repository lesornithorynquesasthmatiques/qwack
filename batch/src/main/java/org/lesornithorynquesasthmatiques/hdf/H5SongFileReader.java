package org.lesornithorynquesasthmatiques.hdf;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

/**
 * @author Alexandre Dutra
 *
 */
@NotThreadSafe
public class H5SongFileReader {

	private static final String ANALYZIS_DATASET_PATH = "/analysis/songs";
	private static final String METADATA_DATASET_PATH = "/metadata/songs";
	private static final String MUSICBRAINZ_DATASET_PATH = "/musicbrainz/songs";
	
	private final Path file;

	// internal state
	
	private H5File h5file;

	public H5SongFileReader(Path file) {
		this.file = file;
	}

	public Path getFile() {
		return file;
	}

	public void open() throws Exception {
		this.h5file = new H5File(this.file.toString(), FileFormat.READ);
        h5file.open();
	}
	
	public void close() throws HDF5Exception {
		h5file.close();
	}
	
	public DataSubset readAnalyzis() throws Exception {
		return readDataset(ANALYZIS_DATASET_PATH);
	}

	public DataSubset readMetadata() throws Exception {
		return readDataset(METADATA_DATASET_PATH);
	}

	public DataSubset readMusicBrainz() throws Exception {
		return readDataset(MUSICBRAINZ_DATASET_PATH);
	}

	private DataSubset readDataset(String datasetPath) throws Exception, OutOfMemoryError {
		H5CompoundDS dataset = (H5CompoundDS) h5file.get(datasetPath);
		dataset.open();
		// Retrieves datatype and dataspace information from h5file and sets the
		// dataset in memory.
		dataset.init();
		@SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) dataset.getData();
		// it is VITAL to create a defensive copy of this list
		// because the dataset instance holds a pointer to it.
		return new DataSubset(new ArrayList<Object>(data));
	}

}
