package org.lesornithorynquesasthmatiques.helper;

import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoHelper {

	public static Mongo connect(String mongoHost, Integer mongoPort, String mongoUser, String mongoPassword) throws UnknownHostException, MongoException {
		// TODO authentication
		return new Mongo(mongoHost, mongoPort);
	}

}
