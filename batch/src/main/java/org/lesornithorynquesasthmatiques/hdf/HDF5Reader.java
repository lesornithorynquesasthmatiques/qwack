package org.lesornithorynquesasthmatiques.hdf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic reader for H5 files. Reads one single dataset by chunks.
 * 
 * @author Alexandre Dutra
 *
 */
public class HDF5Reader {

	private static final Logger LOG = LoggerFactory.getLogger(HDF5Reader.class);

	private final File file;

	private final String datasetPath;

	private final int chunkSize;
	
	// internal state
	
	private H5File h5file;

	private H5CompoundDS dataset;

	private long rows;

	private int currentRow;

	public HDF5Reader(File file, String datasetPath, int chunkSize) {
		this.file = file;
		this.datasetPath = datasetPath;
		this.chunkSize = chunkSize;
	}

	public void init() throws Exception {
		openFile();
		initDataset();
		LOG.info("DataSet initialized, size: {}", getDataSetSize());
	}

	public long getDataSetSize(){
		return dataset.getDims()[0];
	}
	
	public boolean hasMoreChunks() {
		return currentRow < rows;
	}

	public DataSubset readNextChunk() throws Exception {
		//dunno why, but this needs to be called every time we want to getData()
		//let's hope it doesn't affect performance...
		dataset.init();
		long[] starts = dataset.getStartDims();
		long[] selecteds = dataset.getSelectedDims();
		starts[0] = currentRow;
		if(currentRow + chunkSize > rows) {
			selecteds[0] = rows - currentRow;
		} else {
			selecteds[0] = chunkSize;
		}
		//It is actually a Vector of arrays of different types
		@SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) dataset.getData();
		currentRow += selecteds[0];
		if(LOG.isInfoEnabled()) {
			LOG.info("Read progress: {}%", (int) ((double) currentRow / (double) getDataSetSize() * 100d));
		}
		//it is VITAL to create a defensive copy of this list
		//because the dataset instance holds a pointer to it.
		return new DataSubset(new ArrayList<Object>(data));
	}

	public int itemsRead() {
		return currentRow;
	}

	public void close() throws HDF5Exception {
		h5file.close();
	}
	
	private void openFile() throws Exception {
		this.h5file = new H5File(this.file.toString(), FileFormat.READ);
        h5file.open();
	}

	private void initDataset() throws Exception {
		this.dataset = (H5CompoundDS) h5file.get(datasetPath);
        dataset.open();
        // Retrieves datatype and dataspace information from h5file and sets the dataset in memory.
        dataset.init();
        long[] dims = dataset.getDims(); // the dimension sizes of the dataset
        rows = dims[0];
        currentRow = 0;
	}

}
