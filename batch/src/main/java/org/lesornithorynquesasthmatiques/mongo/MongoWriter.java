package org.lesornithorynquesasthmatiques.mongo;

import java.net.UnknownHostException;

import javax.annotation.concurrent.ThreadSafe;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.lesornithorynquesasthmatiques.model.Song;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
@ThreadSafe
public class MongoWriter {

	private Jongo jongo;

	private MongoCollection songs;

	private MongoCollection artists;

	public MongoWriter(String mongoHost, Integer mongoPort, String mongoUser,
			String mongoPassword, String mongoWriteConcern, String mongoDatabaseName,
			String songsCollectionName, String artistsCollectionName) throws UnknownHostException, MongoException {
		MongoHelper.initMongo(mongoHost, mongoPort, mongoUser, mongoPassword, mongoWriteConcern);
		Mongo mongo = MongoHelper.getMongo();
		DB db = mongo.getDB(mongoDatabaseName);
		jongo =  MongoHelper.getJongo(db);
		songs = jongo.getCollection(songsCollectionName);
		artists = jongo.getCollection(artistsCollectionName);
	}
	
	public void write(Song object) {
		songs.save(object);
		artists.save(object.getArtist());
	}
	
}
