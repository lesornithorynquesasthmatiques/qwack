package org.lesornithorynquesasthmatiques.batch;

import java.net.UnknownHostException;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.lesornithorynquesasthmatiques.helper.MongoHelper;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoWriter<T> {
	
	private final String mongoHost; 
	
	private final Integer mongoPort; 
	
	private final String mongoUser;
	
	private final String mongoPassword; 
	
	private final String mongoDatabaseName;
	
	private final String mongoCollectionName;
	
	private Jongo jongo;

	private MongoCollection collection;

	public MongoWriter(String mongoHost, Integer mongoPort, String mongoUser,
			String mongoPassword, String mongoDatabaseName,
			String mongoCollectionName) {
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
		this.mongoUser = mongoUser;
		this.mongoPassword = mongoPassword;
		this.mongoDatabaseName = mongoDatabaseName;
		this.mongoCollectionName = mongoCollectionName;
	}

	public void init() throws UnknownHostException, MongoException {
		Mongo mongo = MongoHelper.connect(mongoHost, mongoPort, mongoUser, mongoPassword);
		DB db = mongo.getDB(mongoDatabaseName);
		jongo = new Jongo(db);
		collection = jongo.getCollection(mongoCollectionName);
	}
	
	public void write(List<T> objects) {
		//FIXME Can't Jongo optimize this??
		for (T o : objects) {
			collection.save(o);
		}
	}

}
