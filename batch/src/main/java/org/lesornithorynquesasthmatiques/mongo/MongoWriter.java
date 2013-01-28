package org.lesornithorynquesasthmatiques.mongo;

import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
@ThreadSafe
public class MongoWriter<T> {

	private Jongo jongo;

	private MongoCollection collection;

	public MongoWriter(String mongoHost, Integer mongoPort, String mongoUser,
			String mongoPassword, String mongoWriteConcern, String mongoDatabaseName,
			String mongoCollectionName) throws UnknownHostException, MongoException {
		MongoHelper.initMongo(mongoHost, mongoPort, mongoUser, mongoPassword, mongoWriteConcern);
		Mongo mongo = MongoHelper.getMongo();
		DB db = mongo.getDB(mongoDatabaseName);
		jongo =  MongoHelper.getJongo(db);
		collection = jongo.getCollection(mongoCollectionName);
	}
	
	public void write(T object) {
		collection.save(object);
	}
	
	public void write(List<T> objects) {
		//FIXME Can't Jongo optimize this??
		for (T o : objects) {
			write(o);
		}
	}

}
