package org.lesornithorynquesasthmatiques.mongo;
import static org.fest.assertions.api.Assertions.*;

import java.net.UnknownHostException;
import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.City;

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
public class CitiesGeoSpatialTest {
	
	/**
	 * Earth radius in km.
	 */
	private static final double EARTH_RADIUS = 6378.137d;

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	private MongoCollection cities;
	
	@Before
	public void setUp(){
		cities = mongoHelper.getJongo().getCollection("cities");
		cities.insert("{name: 'Paris', location: [2.3486, 48.8534]}");
		cities.insert("{name: 'Neuilly-sur-Seine', location: [2.26965, 48.8846]}");
		cities.insert("{name: 'Lille', location: [3.05858, 50.63297]}");
		cities.insert("{name: 'Lyon', location: [4.84139, 45.75889]}");
		DBCollection coll = mongoHelper.getDb().getCollection("cities");
		coll.ensureIndex(new BasicDBObject("location", "2d"));
	}
	
	@Test
	public void should_find_all_cities_ordered_by_distance_from_paris() throws UnknownHostException, MongoException {
		//returns all documents near a point, calculating distances using spherical geometry,
		//ordered by distance, one doc per location found (can lead to duplicates)
		Iterator<City> it = cities.find("{ location: { $nearSphere: [2.3486, 48.8534] } }").as(City.class).iterator();
		assertThat(it.next().getName()).isEqualTo("Paris");
		assertThat(it.next().getName()).isEqualTo("Neuilly-sur-Seine");
		assertThat(it.next().getName()).isEqualTo("Lille");
		assertThat(it.next().getName()).isEqualTo("Lyon");
		assertThat(it.hasNext()).isFalse();
	}
	
	@Test
	public void should_find_3_cities_within_300km_from_paris() throws UnknownHostException, MongoException {
		//http://docs.mongodb.org/manual/reference/command/geoNear/#geoNear
		//All cities within 300km from Paris ordered by distance, one doc per location found (can lead to duplicates)
		BasicDBList position = new BasicDBList();
		position.add(2.3486);
		position.add(48.8534);
		DBObject query = QueryBuilder.
			start("geoNear").is("cities").
			and("spherical").is("true").
			and("near").is(position).
			and("maxDistance").is(300/EARTH_RADIUS).
			and("distanceMultiplier").is(EARTH_RADIUS).get();
		CommandResult cr = mongoHelper.getDb().command(query);
		BasicDBList results = (BasicDBList) cr.get("results");
		assertThat(results.size()).isEqualTo(3);
	}

	@Test
	public void should_find_2_cities_within_10km_from_paris() throws UnknownHostException, MongoException {
		//http://docs.mongodb.org/manual/reference/operator/centerSphere/#_S_centerSphere
		//Uses spherical geometry to calculate distances in a circle specified by a point and radius.
		//No ordering, unique documents
		//All cities within 10km from Paris
		//unparsable with JSON.parse() :(
//		Iterator<City> it = cities.find("{ location: { $within: { $centerSphere : [ [2.3486, 48.8534], 10/6378.137 ] } } }").
//			sort("{_id:1}").	
//			as(City.class).iterator();
		BasicDBList position = new BasicDBList();
		position.add(2.3486);
		position.add(48.8534);
		BasicDBList positionAndRadius = new BasicDBList();
		positionAndRadius.add(position);
		positionAndRadius.add(10d/EARTH_RADIUS);
		DBObject centerSphere = QueryBuilder.start("$centerSphere").is(positionAndRadius).get();
		DBObject within = QueryBuilder.start("$within").is(centerSphere).get();
		DBObject query = QueryBuilder.start("location").is(within).get();
		DBCursor cursor = cities.getDBCollection().find(query);
		cursor.sort(QueryBuilder.start("_id").is(1).get());
		assertThat(cursor.next().get("name")).isEqualTo("Paris");
		assertThat(cursor.next().get("name")).isEqualTo("Neuilly-sur-Seine");
		assertThat(cursor.hasNext()).isFalse();
	}

}
