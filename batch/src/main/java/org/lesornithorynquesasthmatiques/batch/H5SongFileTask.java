package org.lesornithorynquesasthmatiques.batch;

import java.util.concurrent.atomic.AtomicInteger;

import org.lesornithorynquesasthmatiques.converter.SongConverter;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.hdf.H5SongFileReader;
import org.lesornithorynquesasthmatiques.model.Song;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;
import org.lesornithorynquesasthmatiques.solr.SolrWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexandre Dutra
 *
 */
public class H5SongFileTask implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(H5SongFileTask.class);

	private H5SongFileReader reader;
	
	private SongConverter converter;
	
	private MongoWriter<Song> mongoWriter;

	private SolrWriter<Song> solrWriter;
	
	private TaskSynchronizer taskSynchronizer;

	private AtomicInteger successFiles;

	private AtomicInteger errorFiles;

	public void setReader(H5SongFileReader reader) {
		this.reader = reader;
	}

	public void setConverter(SongConverter converter) {
		this.converter = converter;
	}

	public void setMongoWriter(MongoWriter<Song> mongoWriter) {
		this.mongoWriter = mongoWriter;
	}

	public void setSolrWriter(SolrWriter<Song> solrWriter) {
		this.solrWriter = solrWriter;
	}

	public void setTaskSynchronizer(TaskSynchronizer taskSynchronizer) {
		this.taskSynchronizer = taskSynchronizer;
	}

	public void setSuccessFiles(AtomicInteger successFiles) {
		this.successFiles = successFiles;
	}

	public void setErrorFiles(AtomicInteger errorFiles) {
		this.errorFiles = errorFiles;
	}

	public void run() {
		try {
			LOG.debug("Reading file: {}", reader.getFile());
			//read
			reader.open();
			DataSubset analyzis = reader.readAnalyzis();
			DataSubset metadata = reader.readMetadata();
			DataSubset musicBrainz = reader.readMusicBrainz();
			//convert
			Song song = converter.convert(analyzis, metadata, musicBrainz);
			//write
			mongoWriter.write(song);
			solrWriter.write(song);
			LOG.debug("File read successfully: {}", reader.getFile());
			successFiles.incrementAndGet();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			errorFiles.incrementAndGet();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			taskSynchronizer.onAfterTaskCompleted();
		}

	}

}
