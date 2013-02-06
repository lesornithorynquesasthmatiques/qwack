/**
 * 
 */
package org.lesornithorynquesasthmatiques.batch;

import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.lesornithorynquesasthmatiques.converter.SongConverter;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;
import org.lesornithorynquesasthmatiques.solr.SolrHelper;
import org.lesornithorynquesasthmatiques.solr.SolrWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class SongBatch {

	private static final Logger LOG = LoggerFactory.getLogger(SongBatch.class);

	private SongOptions options;

	private long start;

	private long end;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		SongBatch main = new SongBatch();
		int status = main.run(args);
		System.exit(status);
	}

	public int run(String[] args) throws Exception {
		this.options = new SongOptions();
		options.populate(args);
		int status = -1;
		if (options.isHelp()) {
			options.printUsage(System.out);
			status = 0;
		} else {
			logStart();
			try {
				Path directory = options.getDirectory().toPath();
				SongFileScanner scanner = new SongFileScanner();
				scanner.setConverter(new SongConverter());
				scanner.setMongoWriter(newMongoWriter());
				if( ! options.isDisableSolr()){
					scanner.setSolrWriter(newSolrWriter());
				}
				TaskSynchronizer taskSynchronizer = new TaskSynchronizer();
				scanner.setTaskSynchronizer(taskSynchronizer);
				ThreadPoolExecutor pool = newThreadPool();
				scanner.setTaskPool(pool);
				Files.walkFileTree(directory, scanner);
				LOG.info("Directory successfully scanned: {}", directory);
				LOG.info("Shutting down thread pool.");
				taskSynchronizer.awaitUntilAllPendingTasksCompleted();
				pool.shutdown();
				if( ! pool.awaitTermination(60, TimeUnit.SECONDS)) {
					throw new IllegalStateException("Thread pool timeout");
				}
				if( ! options.isDisableSolr()){
					LOG.info("Committing Solr documents.");
					SolrHelper.getSongsCore().commit();
					SolrHelper.getArtistsCore().commit();
					SolrHelper.getSuggestionsCore().commit();
				}
				LOG.info("Total files processed: {}", scanner.getTotalFiles());
				LOG.info("Files successfully processed: {}", scanner.getSuccessFiles());
				LOG.info("Files in error: {}", scanner.getErrorFiles());
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

	private MongoWriter newMongoWriter() throws UnknownHostException, MongoException {
		MongoWriter writer = new MongoWriter(
			options.getMongoHost(), 
			options.getMongoPort(), 
			options.getMongoUser(), 
			options.getMongoPassword(),
			options.getMongoWriteConcern(),
			options.getMongoDatabaseName(), 
			options.getMongoSongsCollectionName(),
			options.getMongoArtistsCollectionName()
		);
		return writer;
	}

	private SolrWriter newSolrWriter() throws UnknownHostException, MongoException {
		SolrWriter writer = new SolrWriter(
			options.getSolrSongsCoreUrl(), 
			options.getSolrSuggestionsCoreUrl(), 
			options.getSolrArtistsCoreUrl()
		);
		return writer;
	}

	private ThreadPoolExecutor newThreadPool() {
		return new ThreadPoolExecutor(
			options.getPoolSize(), 
			options.getPoolSize(), 
			0L, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(options.getQueueSize()), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	}

	private void logStart() {
		this.start = System.currentTimeMillis();
		if (LOG.isInfoEnabled()) {
			LOG.info("Starting Batch");
			LOG.info("Directory: {}", options.getDirectory());
			LOG.info("Mongo host: {}", options.getMongoHost());
			LOG.info("Mongo port: {}", options.getMongoPort());
			LOG.info("Mongo user: {}", options.getMongoUser());
			LOG.info("Mongo DB: {}", options.getMongoDatabaseName());
			LOG.info("Mongo Songs Collection: {}", options.getMongoSongsCollectionName());
			LOG.info("Mongo Artists Collection: {}", options.getMongoArtistsCollectionName());
			LOG.info("Solr Songs Core URL: {}", options.getSolrSongsCoreUrl());
			LOG.info("Solr Suggestions Core URL: {}", options.getSolrSuggestionsCoreUrl());
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
