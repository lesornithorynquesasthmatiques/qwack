package org.lesornithorynquesasthmatiques.mongo;

import java.net.UnknownHostException;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoWriter<T> {

	private static final Logger LOG = LoggerFactory.getLogger(MongoWriter.class);

	private final String mongoHost; 
	
	private final Integer mongoPort; 
	
	private final String mongoUser;
	
	private final String mongoPassword; 

	private final String mongoWriteConcern; 
	
	private final String mongoDatabaseName;
	
	private final String mongoCollectionName;
	
	private Jongo jongo;

	private MongoCollection collection;

	public MongoWriter(String mongoHost, Integer mongoPort, String mongoUser,
			String mongoPassword, String mongoWriteConcern, String mongoDatabaseName,
			String mongoCollectionName) {
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
		this.mongoUser = mongoUser;
		this.mongoPassword = mongoPassword;
		this.mongoWriteConcern = mongoWriteConcern;
		this.mongoDatabaseName = mongoDatabaseName;
		this.mongoCollectionName = mongoCollectionName;
	}

	public void init() throws UnknownHostException, MongoException {
		MongoHelper.initMongo(mongoHost, mongoPort, mongoUser, mongoPassword, mongoWriteConcern);
		Mongo mongo = MongoHelper.getMongo();
		DB db = mongo.getDB(mongoDatabaseName);
		jongo =  MongoHelper.getJongo(db);
		collection = jongo.getCollection(mongoCollectionName);
	}
	
	public void write(List<T> objects) {
		LOG.debug("Writing {} objects", objects.size());
		//FIXME Can't Jongo optimize this??
		for (T o : objects) {
			collection.save(o);
		}
	}

}
