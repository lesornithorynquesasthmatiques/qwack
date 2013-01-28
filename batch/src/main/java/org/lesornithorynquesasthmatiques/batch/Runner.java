package org.lesornithorynquesasthmatiques.batch;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.lesornithorynquesasthmatiques.converter.Converter;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.hdf.HDF5Reader;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;
import org.lesornithorynquesasthmatiques.solr.SolrWriter;
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
	
	private MongoWriter<T> mongoWriter;

	private SolrWriter<T> solrWriter;
	
	private int shutdownTimeoutInSeconds = 60;

	private ExecutorService conversionPool;

	private ExecutorService mongoPool;

	@SuppressWarnings("unused")
	private ExecutorService solrPool;

	private TaskSynchronizer taskSynchronizer;
	
	public void setReader(HDF5Reader reader) {
		this.reader = reader;
	}

	public void setConverter(Converter<T> converter) {
		this.converter = converter;
	}

	public void setMongoWriter(MongoWriter<T> mongoWriter) {
		this.mongoWriter = mongoWriter;
	}

	public void setSolrWriter(SolrWriter<T> solrWriter) {
		this.solrWriter = solrWriter;
	}

	public void setConversionPool(ExecutorService pool) {
		this.conversionPool = pool;
	}

	public void setMongoPool(ExecutorService pool) {
		this.mongoPool = pool;
	}

	public void setSolrPool(ExecutorService pool) {
		this.solrPool = pool;
	}

	public void init() throws Exception {
		reader.init();
		taskSynchronizer = new TaskSynchronizer();
	}
	
	public void run() throws Exception {
		while(reader.hasMoreChunks()) {
			DataSubset ds = reader.readNextChunk();
			Runnable conversionTask = newConversionTask(ds);
			taskSynchronizer.onBeforeTaskSubmitted();
			conversionPool.submit(conversionTask);
		}
		reader.close();
		LOG.info("Items read: {}", reader.itemsRead());
		taskSynchronizer.awaitUntilAllPendingTasksCompleted();
		shutdownPools();
	}

	private Runnable newConversionTask(final DataSubset ds) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					final List<T> objects = converter.convert(ds);
					Runnable mongoTask = newMongoTask(objects);
					taskSynchronizer.onBeforeTaskSubmitted();
					mongoPool.submit(mongoTask);
//					Runnable solrTask = newSolrTask(objects);
//					taskSynchronizer.onBeforeTaskSubmitted();
//					solrPool.submit(solrTask);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				} finally {
					taskSynchronizer.onAfterTaskCompleted();
				}
			}

		};
	}

	private Runnable newMongoTask(final List<T> objects) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					mongoWriter.write(objects);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				} finally {
					taskSynchronizer.onAfterTaskCompleted();
				}
			}
		};
	}

	@SuppressWarnings("unused")
	private Runnable newSolrTask(final List<T> objects) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					solrWriter.write(objects);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				} finally {
					taskSynchronizer.onAfterTaskCompleted();
				}
			}
		};
	}

	private void shutdownPools() throws InterruptedException {
		conversionPool.shutdown();
		mongoPool.shutdown();
//		solrPool.shutdown();
		if( ! conversionPool.awaitTermination(shutdownTimeoutInSeconds, TimeUnit.SECONDS)) {
			throw new IllegalStateException("Conversion thread pool timeout");
		}
		if( ! mongoPool.awaitTermination(shutdownTimeoutInSeconds, TimeUnit.SECONDS)) {
			throw new IllegalStateException("Mongo thread pool timeout");
		}
	}

}
