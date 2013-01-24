package org.lesornithorynquesasthmatiques.batch;

import java.util.List;

import org.lesornithorynquesasthmatiques.converter.Converter;
import org.lesornithorynquesasthmatiques.hdf.HDF5Reader;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexandre Dutra
 *
 */
public class Runner<T> {

	private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

	private HDF5Reader reader;
	
	private Converter<T> converter;
	
	private MongoWriter<T> writer;
	
	public void setReader(HDF5Reader reader) {
		this.reader = reader;
	}

	public void setConverter(Converter<T> converter) {
		this.converter = converter;
	}

	public void setWriter(MongoWriter<T> writer) {
		this.writer = writer;
	}

	public void run() throws Exception {
		
		reader.init();
		writer.init();
		
		while(reader.hasMoreChunks()) {
			DataSubset rs = reader.readNextChunk();
			List<T> sensors = converter.convert(rs);
			writer.write(sensors);
		}
		
		LOG.info("Items read: {}", reader.itemsRead());
		reader.close();
		
	}

}
