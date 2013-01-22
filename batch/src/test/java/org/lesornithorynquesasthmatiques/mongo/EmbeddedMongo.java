package org.lesornithorynquesasthmatiques.mongo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.io.IStreamProcessor;
import de.flapdoodle.embed.process.io.NamedOutputStreamProcessor;
import de.flapdoodle.embed.process.io.StreamToLineProcessor;
import de.flapdoodle.embed.process.io.progress.IProgressListener;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Special subclass of {@link Mongo} that
 * launches an embedded Mongod server instance in a separate process.
 * 
 * @author Alexandre Dutra
 */
public class EmbeddedMongo extends Mongo {

	private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongo.class);

	public static final String DEFAULT_HOST = "127.0.0.1";

	public static final int DEFAULT_PORT = 27017;

	public static final WriteConcern DEFAULT_WRITE_CONCERN = WriteConcern.FSYNC_SAFE;
	
	public static final Version DEFAULT_VERSION = Version.V2_0_5;

	public EmbeddedMongo() throws MongoException, IOException {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public EmbeddedMongo(int port) throws MongoException, IOException {
		this(DEFAULT_HOST, port);
	}

	public EmbeddedMongo(String host, int port) throws MongoException, IOException {
		this(host, port, DEFAULT_WRITE_CONCERN);
	}

	public EmbeddedMongo(String host, int port, WriteConcern writeConcern) throws MongoException, IOException {
		this(host, port, writeConcern, DEFAULT_VERSION);
	}
	
	public EmbeddedMongo(String host, int port, WriteConcern writeConcern, Version version) throws MongoException, IOException {
		super(host, port);

		this.setWriteConcern(writeConcern);
		RuntimeConfig runtimeConfig = new RuntimeConfig();
		IStreamProcessor mongodOutput =
				new NamedOutputStreamProcessor("[mongod out]",
						new StreamToLineProcessor(
								new Slf4jStreamProcessor(LOG, Slf4jLevel.INFO)));
		IStreamProcessor mongodError =
				new NamedOutputStreamProcessor("[mongod err]",
						new StreamToLineProcessor(
								new Slf4jStreamProcessor(LOG, Slf4jLevel.WARN)));
		IStreamProcessor commandsOutput =
				new NamedOutputStreamProcessor("[mongod kill]",
						new StreamToLineProcessor(
								new Slf4jStreamProcessor(LOG, Slf4jLevel.INFO)));
		
		runtimeConfig.setProcessOutput(new ProcessOutput(mongodOutput, mongodError, commandsOutput));

		runtimeConfig.getDownloadConfig().setProgressListener(new IProgressListener() {
			
			@Override
			public void start(String label) {
				LOG.info("{} starting...", label);
			}
			
			@Override
			public void progress(String label, int percent) {
				LOG.info("{} : {}% achieved.", label, percent);
			}
			
			@Override
			public void info(String label, String message) {
				LOG.info("{} : {}", label, message);
			}
			
			@Override
			public void done(String label) {
				LOG.info("{} achieved successfully.", label);
			}
		});
				
		MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
		MongodConfig config = new MongodConfig(version, port, Network.localhostIsIPv6());
		MongodExecutable mongodExe = runtime.prepare(config);
		mongodExe.start();

	}
}
