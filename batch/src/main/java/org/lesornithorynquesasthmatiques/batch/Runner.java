package org.lesornithorynquesasthmatiques.batch;

import java.util.List;

import org.lesornithorynquesasthmatiques.model.Sensor;

/**
 * @author Alexandre Dutra
 *
 */
public class Runner {

	private final HDF5Reader reader;
	
	private final Converter converter;
	
	private final MongoWriter<Sensor> writer;
	
	public Runner(Options options) {
		this.reader = new HDF5Reader(
				options.getH5file(), 
				options.getDatasetPath(), 
				options.getChunkSize());
		this.converter = new Converter();
		this.writer = new MongoWriter<Sensor>(
				options.getMongoHost(), 
				options.getMongoPort(), 
				options.getMongoUser(), 
				options.getMongoPassword(), 
				options.getMongoDatabaseName(), 
				options.getMongoCollectionName());
	}

	public void run() throws Exception {
		
		reader.init();
		writer.init();
		
		while(reader.hasMoreChunks()) {
			List<Object> data = reader.readNextChunk();
			List<Sensor> sensors = converter.convertData(data);
			writer.write(sensors);
		}
		
		reader.close();
		
	}

}
