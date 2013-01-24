package org.lesornithorynquesasthmatiques.mongo;

import java.util.logging.LogManager;

import org.jongo.Jongo;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

/**
 * A specialized {@link MongoHelper} for Mongo unit tests.
 * <ul>
 * <li>Launches an embedded version of Mongo.</li>
 * <li>The Mongo port is random.</li>
 * <li>Everything is stored in a single database.</li>
 * <li>Each test gets its own database (to avoid data collisions).</li>
 * </ul>
 */
public class MongoTestsHelper extends MongoHelper implements TestRule {

	private static final Logger LOG = LoggerFactory.getLogger(MongoTestsHelper.class);

	/**
	 * This field is static mainly to
	 * allow two different instances of this class to share the same db;
	 * this happens specially when unit testing Spring contexts
	 * that declare a {@link MongoTestsHelper} bean.
	 */
	private static DB db;

	static {

		// this enables JUL-to-SLF4J bridge
		// see http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		// java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.INFO);

		//only one instance per JVM
	    try {
			int port = RandomPortNumberGenerator.pickAvailableRandomEphemeralPortNumber();
			LOG.info("Starting Mongo on port {}...", port);			
			/*
			 * If EmbeddedMongo can't download the Mongo server distribution for your machine, debug
			 * de.flapdoodle.embed.process.store.LocalArtifactStore#getArtifact(IDownloadConfig,  Distribution)
			 * to get the full path of the file to download, then download it manually and place it (and if necessary rename it) there.
			 */
			Mongo mongo = new EmbeddedMongo(port);
			mongo.setWriteConcern(WriteConcern.FSYNC_SAFE);
			LOG.info("Mongo successfully started on port {}", port);
			MongoHelper.setMongo(mongo);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Embedded Mongo instance: " + e, e);
		}
	}
	
	public static String getMongoHost() {
		return getMongo().getAddress().getHost();
	}
	
	public static int getMongoPort() {
		return getMongo().getAddress().getPort();
	}

	public static DB getDb() {
		return db;
	}

	public static Jongo getJongo() {
		return getJongo(db);
	}
	
	public MongoTestsHelper() {
		setUpTestDatabase();
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before(description);
				try {
					base.evaluate();
				} finally {
					after();
				}
			}
		};
	}

	protected void before(Description description) throws Exception {
		setUpTestDatabase();
	}

	protected void after() {
		tearDownTestDatabase();
	}
	
	public void setUpTestDatabase(){
		if(db == null) {
			db = getMongo().getDB("test_" + System.nanoTime());
			LOG.info("Starting test with DB [{}]", db.getName());
		}
	}
	
	public void tearDownTestDatabase(){
		if (db != null) {
			LOG.debug("Dropping DB [{}]", db.getName());
			db.dropDatabase();
			db = null;
		}
	}

}
