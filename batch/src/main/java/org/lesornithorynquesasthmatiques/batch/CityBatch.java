/**
 * 
 */
package org.lesornithorynquesasthmatiques.batch;

import java.net.UnknownHostException;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.lesornithorynquesasthmatiques.converter.CityConverter;
import org.lesornithorynquesasthmatiques.hdf.HDF5Reader;
import org.lesornithorynquesasthmatiques.model.City;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class CityBatch {

	private static final Logger LOG = LoggerFactory.getLogger(CityBatch.class);

	private CityOptions cityOptions;

	private long start;

	private long end;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		CityBatch cityBatch = new CityBatch();
		int status = cityBatch.run(args);
		System.exit(status);
	}

	public int run(String[] args) throws Exception {
		this.cityOptions = new CityOptions();
		cityOptions.populate(args);
		int status = -1;
		if (cityOptions.isHelp()) {
			cityOptions.printUsage(System.out);
			status = 0;
		} else {
			logStart();
			try {
				CityBatchRunner<City> runner = new CityBatchRunner<City>();
				runner.setReader(newHDF5Reader());
				runner.setConverter(new CityConverter());
				runner.setMongoWriter(newMongoWriter());
				runner.setConversionPool(newThreadPool());
				runner.setMongoPool(newThreadPool());
				runner.init();
				runner.run();
				status = 0;
			} catch (Exception e) {
				status = 1;
				LOG.error("Batch terminated unexpectedly", e);
			} finally {
				logEnd(status);
			}
		}
		return status;
	}

	private HDF5Reader newHDF5Reader() {
		HDF5Reader reader = new HDF5Reader(
			cityOptions.getH5file(), 
			cityOptions.getDatasetPath(), 
			cityOptions.getChunkSize());
		return reader;
	}

	private MongoWriter<City> newMongoWriter() throws UnknownHostException, MongoException {
		MongoWriter<City> writer = new MongoWriter<City>(
			cityOptions.getMongoHost(), 
			cityOptions.getMongoPort(), 
			cityOptions.getMongoUser(), 
			cityOptions.getMongoPassword(),
			cityOptions.getMongoWriteConcern(),
			cityOptions.getMongoDatabaseName(), 
			cityOptions.getMongoCollectionName());
		return writer;
	}

	private ThreadPoolExecutor newThreadPool() {
		return new ThreadPoolExecutor(
			cityOptions.getPoolSize(), 
			cityOptions.getPoolSize(), 
			0L, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(cityOptions.getQueueSize()), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	}

	private void logStart() {
		this.start = System.currentTimeMillis();
		if (LOG.isInfoEnabled()) {
			LOG.info("Starting Batch");
			LOG.info("File: {}", cityOptions.getH5file());
			LOG.info("Dataset Path: {}", cityOptions.getDatasetPath());
			LOG.info("Chunk Size: {}", cityOptions.getChunkSize());
			LOG.info("Mongo host: {}", cityOptions.getMongoHost());
			LOG.info("Mongo port: {}", cityOptions.getMongoPort());
			LOG.info("Mongo user: {}", cityOptions.getMongoUser());
			LOG.info("Mongo DB: {}", cityOptions.getMongoDatabaseName());
			LOG.info("Mongo Collection: {}", cityOptions.getMongoCollectionName());
		}
	}

	private void logEnd(int status) {
		this.end = System.currentTimeMillis();
		if (LOG.isInfoEnabled()) {
			PeriodFormatter pf = PeriodFormat.wordBased(Locale.ENGLISH);
			Period p = new Period(start, end);
			DateTimeFormatter dtf = DateTimeFormat.mediumDateTime().withLocale(Locale.ENGLISH);
			LOG.info("Batch finished. Status: {}.", status);
			LOG.info("Start time: {}.", dtf.print(start));
			LOG.info("End time: {}.", dtf.print(end));
			LOG.info("Total execution time: {}.", pf.print(p));
		}
	}

}
