package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;

import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.City;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsHelper;

public class MainTest {

	private static final String FILENAME = "src/test/resources/FR-small.h5";

	private static String DATASET_PATH = "GEONAMES/FR";

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	@Test
	public void should_read_and_write_4_objects() throws Exception {
		//Given
		String[] args = new String[]{
			"--file"       , FILENAME, 
			"--dataset"    , DATASET_PATH, 
			"--host"       , MongoTestsHelper.getMongoHost(), 
			"--port"       , Integer.toString(MongoTestsHelper.getMongoPort()), 
			"--chunk-size" , "2", 
			"--database"   , MongoTestsHelper.getDb().getName(), 
			"--collection" , "cities"
		};
		//When
		Main main = new Main();
		int status = main.run(args);
		//Then
		assertThat(status).isEqualTo(0);
		MongoCollection cities = MongoTestsHelper.getJongo().getCollection("cities");
		Iterator<City> it = cities.find().sort("{name:1}").as(City.class).iterator();
		assertThat(it.next().getName()).isEqualTo("Lille");
		assertThat(it.next().getName()).isEqualTo("Lyon");
		assertThat(it.next().getName()).isEqualTo("Neuilly-sur-Seine");
		assertThat(it.next().getName()).isEqualTo("Paris");
		assertThat(it.hasNext()).isFalse();
	}

}
