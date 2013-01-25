package org.lesornithorynquesasthmatiques.mongo;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Strings;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * A helper that holds a singleton instance of {@link MongoClient}.
 * 
 * @author Alexandre Dutra
 *
 */
public class MongoHelper {

	private static MongoClient mongo;
	
	public static void initMongo(String mongoHost, Integer mongoPort, String mongoUser, String mongoPassword, String writeConcern) throws UnknownHostException, MongoException {
		if(mongo == null) {
			MongoClientOptions options = new MongoClientOptions.Builder()
				.writeConcern(WriteConcern.valueOf(writeConcern))
				.build()
			;
			ServerAddress serverAddress = new ServerAddress(mongoHost, mongoPort);
			mongo = new MongoClient(serverAddress, options);
			mongoUser = Strings.emptyToNull(mongoUser);
			if (mongoUser != null) {
				DB adminDb = mongo.getDB("admin");
				if ( ! adminDb.isAuthenticated()) {
					char[] password = Strings.isNullOrEmpty(mongoPassword) ? null : mongoPassword.toCharArray();
					boolean success = adminDb.authenticate(mongoUser, password);
					if (!success) throw new IllegalArgumentException(
						String.format("Could not authenticate with user '%s', check your configuration", mongoUser));
				}
			}
		}
	}

	public static MongoClient getMongo() {
		return mongo;
	}

	public static Jongo getJongo(DB db) {
		return new Jongo(db, new JacksonMapper.Builder()
			.registerModule(new JodaModule())
			.enable(MapperFeature.AUTO_DETECT_GETTERS)
	//		.withView(Views.Public.class)
			.build()
		);
	}

	/**
	 * Back-door for unit tests.
	 * @param mongo
	 */
	static void setMongo(MongoClient mongo) {
		MongoHelper.mongo = mongo;
	}

	
}
