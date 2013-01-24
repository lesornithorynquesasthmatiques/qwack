package org.lesornithorynquesasthmatiques.batch;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.lesornithorynquesasthmatiques.converter.Converter;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.hdf.HDF5Reader;
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
	
	private int concurrencyLevel = Runtime.getRuntime().availableProcessors();

	private int queueSize = 1000;

	private int shutdownTimeoutInSeconds = 60;

	private ExecutorService pool;
	
	public void setReader(HDF5Reader reader) {
		this.reader = reader;
	}

	public void setConverter(Converter<T> converter) {
		this.converter = converter;
	}

	public void setWriter(MongoWriter<T> writer) {
		this.writer = writer;
	}

	public void init() throws Exception {
		BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(queueSize);
	    RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
	    this.pool = new ThreadPoolExecutor(concurrencyLevel, concurrencyLevel, 0L, TimeUnit.MILLISECONDS, blockingQueue, rejectedExecutionHandler);
		reader.init();
		writer.init();
	}
	
	public void run() throws Exception {
		final TaskSynchronizer taskSynchronizer = new TaskSynchronizer();
		while(reader.hasMoreChunks()) {
			final DataSubset ds = reader.readNextChunk();
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try {
						List<T> objects = converter.convert(ds);
						writer.write(objects);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					} finally {
						taskSynchronizer.onAfterTaskCompleted();
					}
				}
			};
			taskSynchronizer.onBeforeTaskSubmitted();
			pool.submit(task);
		}
		reader.close();
		LOG.info("Items read: {}", reader.itemsRead());
		taskSynchronizer.awaitUntilAllPendingTasksCompleted();
		pool.shutdown();
		if( ! pool.awaitTermination(shutdownTimeoutInSeconds, TimeUnit.SECONDS)) {
			throw new IllegalStateException("Thread pool timeout");
		}
		
	}

}
