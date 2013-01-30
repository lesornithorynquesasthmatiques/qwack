package org.lesornithorynquesasthmatiques.hdf;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5ScalarDS;

/**
 * Reader for H5 song files.
 * 
 * @author Alexandre Dutra
 *
 */
@NotThreadSafe
public class SongFileReader {

	private static final String ANALYZIS_DATASET_PATH = "/analysis/songs";
	private static final String METADATA_DATASET_PATH = "/metadata/songs";
	private static final String METADATA_ARTIST_TERMS_DATASET_PATH = "/metadata/artist_terms";
	private static final String METADATA_SIMILAR_ARTISTS_DATASET_PATH = "/metadata/similar_artists";
	private static final String MUSICBRAINZ_DATASET_PATH = "/musicbrainz/songs";
	private static final String MUSICBRAINZ_TAGS_DATASET_PATH = "/musicbrainz/artist_mbtags";
	
	private final Path file;

	// internal state
	
	private H5File h5file;

	public SongFileReader(Path file) {
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
	
	public CompoundDataset readAnalyzis() throws Exception {
		return readCompoundDataset(ANALYZIS_DATASET_PATH);
	}

	public CompoundDataset readMetadata() throws Exception {
		return readCompoundDataset(METADATA_DATASET_PATH);
	}

	public CompoundDataset readMusicBrainz() throws Exception {
		return readCompoundDataset(MUSICBRAINZ_DATASET_PATH);
	}

	public String[] readMusicBrainzTags() throws Exception {
		return readScalarDataset(MUSICBRAINZ_TAGS_DATASET_PATH);
	}

	public String[] readMetadataArtistTerms() throws Exception {
		return readScalarDataset(METADATA_ARTIST_TERMS_DATASET_PATH);
	}

	public String[] readMetadataSimilarArtists() throws Exception {
		return readScalarDataset(METADATA_SIMILAR_ARTISTS_DATASET_PATH);
	}

	private CompoundDataset readCompoundDataset(String datasetPath) throws Exception, OutOfMemoryError {
		H5CompoundDS dataset = (H5CompoundDS) h5file.get(datasetPath);
		dataset.open();
		// Retrieves datatype and dataspace information from h5file and sets the
		// dataset in memory.
		dataset.init();
		@SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) dataset.getData();
		// it is VITAL to create a defensive copy of this list
		// because the dataset instance holds a pointer to it.
		return new CompoundDataset(new ArrayList<Object>(data));
	}

	@SuppressWarnings("unchecked")
	private <T> T readScalarDataset(String datasetPath) throws Exception, OutOfMemoryError {
		H5ScalarDS dataset = (H5ScalarDS) h5file.get(datasetPath);
		dataset.open();
		// Retrieves datatype and dataspace information from h5file and sets the
		// dataset in memory.
		dataset.init();
		long[] dims = dataset.getDims();
		if(dims[0] > 0) {
			return (T) dataset.getData();
		} else {
			//no data
			return null;
		}
	}

}
