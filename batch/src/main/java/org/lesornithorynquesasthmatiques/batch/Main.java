/**
 * 
 */
package org.lesornithorynquesasthmatiques.batch;

import java.util.Locale;

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

/**
 * @author Alexandre Dutra
 *
 */
public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	private Options options;

	private long start;

	private long end;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		int status = main.run(args);
		System.exit(status);
	}

	public int run(String[] args) throws Exception {
		this.options = new Options();
		options.populate(args);
		int status = -1;
		if (options.isHelp()) {
			options.printUsage(System.out);
			status = 0;
		} else {
			logStart();
			try {
				HDF5Reader reader = new HDF5Reader(
					options.getH5file(), 
					options.getDatasetPath(), 
					options.getChunkSize());
				CityConverter converter = new CityConverter();
				MongoWriter<City> writer = new MongoWriter<City>(
					options.getMongoHost(), 
					options.getMongoPort(), 
					options.getMongoUser(), 
					options.getMongoPassword(), 
					options.getMongoDatabaseName(), 
					options.getMongoCollectionName());
				Runner<City> runner = new Runner<City>();
				runner.setReader(reader);
				runner.setConverter(converter);
				runner.setWriter(writer);
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

	private void logStart() {
		this.start = System.currentTimeMillis();
		if (LOG.isInfoEnabled()) {
			LOG.info("Starting Batch");
			LOG.info("File: {}", options.getH5file());
			LOG.info("Dataset Path: {}", options.getDatasetPath());
			LOG.info("Chunk Size: {}", options.getChunkSize());
			LOG.info("Mongo host: {}", options.getMongoHost());
			LOG.info("Mongo port: {}", options.getMongoPort());
			LOG.info("Mongo user: {}", options.getMongoUser());
			LOG.info("Mongo DB: {}", options.getMongoDatabaseName());
			LOG.info("Mongo Collection: {}", options.getMongoCollectionName());
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
