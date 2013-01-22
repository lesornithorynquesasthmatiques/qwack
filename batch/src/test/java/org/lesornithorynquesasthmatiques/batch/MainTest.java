package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;

import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.Sensor;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsSupport;

public class MainTest {

	private static final String FILENAME = "src/test/resources/sensors.h5";

	private static String DATASET_PATH = "Sensors/SENSORS";

	@Rule
	public MongoTestsSupport mongoTestsSupport = new MongoTestsSupport();
	
	@Test
	public void should_read_and_write_4_objects() throws Exception {
		//Given
		String[] args = new String[]{
			"--file"       , FILENAME, 
			"--dataset"    , DATASET_PATH, 
			"--host"       , MongoTestsSupport.getMongoHost(), 
			"--port"       , Integer.toString(MongoTestsSupport.getMongoPort()), 
			"--chunk-size" , "2", 
			"--database"   , MongoTestsSupport.getDb().getName(), 
			"--collection" , "sensors"
		};
		//When
		Main main = new Main();
		int status = main.run(args);
		//Then
		assertThat(status).isEqualTo(0);
		MongoCollection sensors = MongoTestsSupport.getJongo().getCollection("sensors");
		Iterator<Sensor> it = sensors.find().sort("{_id:1}").as(Sensor.class).iterator();
		assertThat(it.next().getSerial_no()).isEqualTo(-2130444288);
		assertThat(it.next().getSerial_no()).isEqualTo(-1610350592);
		assertThat(it.next().getSerial_no()).isEqualTo(50593792);
		assertThat(it.next().getSerial_no()).isEqualTo(553975808);
		assertThat(it.hasNext()).isFalse();
	}

}
