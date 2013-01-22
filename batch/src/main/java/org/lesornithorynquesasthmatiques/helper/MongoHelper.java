package org.lesornithorynquesasthmatiques.helper;

import java.net.UnknownHostException;

import com.google.common.base.Strings;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoHelper {

	public static Mongo connect(String mongoHost, Integer mongoPort, String mongoUser, String mongoPassword) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo(mongoHost, mongoPort);
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
		return mongo;
	}

}
