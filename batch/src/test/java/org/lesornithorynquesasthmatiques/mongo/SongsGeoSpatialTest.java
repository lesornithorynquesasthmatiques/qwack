package org.lesornithorynquesasthmatiques.mongo;
import static org.fest.assertions.api.Assertions.assertThat;

import java.net.UnknownHostException;
import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.Artist;
import org.lesornithorynquesasthmatiques.model.Location;
import org.lesornithorynquesasthmatiques.model.Song;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.QueryBuilder;

/**
 * @author Alexandre Dutra
 *
 */
public class SongsGeoSpatialTest {
	
	/**
	 * Earth radius in km.
	 */
	private static final double EARTH_RADIUS = 6378.137d;

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	private MongoCollection songs;
	
	@Before
	public void setUp(){
		songs = mongoHelper.getJongo().getCollection("songs");
		Song neverGonnaGiveYouUp = new Song();
		neverGonnaGiveYouUp.setTitle("Never Gonna Give You Up");
		Artist rickAstley = new Artist();
		neverGonnaGiveYouUp.setArtist(rickAstley);
		rickAstley.setName("Rick Astley");
		Location losAngeles = new Location();
		rickAstley.setLocation(losAngeles);
		losAngeles.setName("Los Angeles");
		losAngeles.setCoords(34.052337,-118.243680);
		songs.save(neverGonnaGiveYouUp);
		
		Song laVieEnRose = new Song();
		laVieEnRose.setTitle("La Vie En Rose");
		Artist edithPiaf = new Artist();
		laVieEnRose.setArtist(edithPiaf);
		edithPiaf.setName("Edith Piaf");
		Location paris = new Location();
		edithPiaf.setLocation(paris);
		paris.setName("Paris");
		paris.setCoords(48.8534, 2.3486);
		songs.save(laVieEnRose);

		DBCollection coll = mongoHelper.getDb().getCollection("songs");
		coll.ensureIndex(new BasicDBObject("artist.location.coords", "2d"));
	}
	
	@Test
	public void should_find_all_songs_ordered_by_distance_from_los_angeles() throws UnknownHostException, MongoException {
		//returns all documents near a point, calculating distances using spherical geometry,
		//ordered by distance, one doc per location found (can lead to duplicates)
		Iterator<Song> it = songs.find("{ artist.location.coords: { $nearSphere: [-118.243680, 34.052337] } }").as(Song.class).iterator();
		assertThat(it.next().getTitle()).isEqualTo("Never Gonna Give You Up");
		assertThat(it.next().getTitle()).isEqualTo("La Vie En Rose");
		assertThat(it.hasNext()).isFalse();
	}
	
	@Test
	public void should_find_1_song_within_300km_from_paris() throws UnknownHostException, MongoException {
		//http://docs.mongodb.org/manual/reference/command/geoNear/#geoNear
		//All cities within 300km from Paris ordered by distance, one doc per location found (can lead to duplicates)
		BasicDBList position = new BasicDBList();
		position.add(2.3486);
		position.add(48.8534);
		DBObject query = QueryBuilder.
			start("geoNear").is("songs").
			and("spherical").is("true").
			and("near").is(position).
			and("maxDistance").is(300/EARTH_RADIUS).
			and("distanceMultiplier").is(EARTH_RADIUS).get();
		CommandResult cr = mongoHelper.getDb().command(query);
		BasicDBList results = (BasicDBList) cr.get("results");
		assertThat(results.size()).isEqualTo(1);
	}

	@Test
	public void should_find_1_song_within_10km_from_paris() throws UnknownHostException, MongoException {
		//http://docs.mongodb.org/manual/reference/operator/centerSphere/#_S_centerSphere
		//Uses spherical geometry to calculate distances in a circle specified by a point and radius.
		//No ordering, unique documents
		//All cities within 10km from Paris
		//unparsable with JSON.parse() :(
//		Iterator<Song> it = cities.find("{ location: { $within: { $centerSphere : [ [2.3486, 48.8534], 10/6378.137 ] } } }").
//			sort("{_id:1}").	
//			as(Song.class).iterator();
		BasicDBList position = new BasicDBList();
		position.add(2.3486);
		position.add(48.8534);
		BasicDBList positionAndRadius = new BasicDBList();
		positionAndRadius.add(position);
		positionAndRadius.add(10d/EARTH_RADIUS);
		DBObject centerSphere = QueryBuilder.start("$centerSphere").is(positionAndRadius).get();
		DBObject within = QueryBuilder.start("$within").is(centerSphere).get();
		DBObject query = QueryBuilder.start("artist.location.coords").is(within).get();
		DBCursor cursor = songs.getDBCollection().find(query);
		assertThat(cursor.next().get("title")).isEqualTo("La Vie En Rose");
		assertThat(cursor.hasNext()).isFalse();
	}

}
