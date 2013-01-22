package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;
import java.io.File;
import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.Sensor;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsSupport;

public class RunnerTest {

	private static final String FILENAME = "src/test/resources/sensors2.h5";

	private static String DATASET_PATH = "Sensors/SENSORS";

	@Rule
	public MongoTestsSupport mongoTestsSupport = new MongoTestsSupport();
	
	@Test
	public void should_read_and_write_4_objects() throws Exception {
		Options options = new Options();
		options.h5file = new File(FILENAME);
		options.datasetPath = DATASET_PATH;
		options.chunkSize = 2;
		options.mongoHost = MongoTestsSupport.getMongoHost();
		options.mongoPort = MongoTestsSupport.getMongoPort();
		options.mongoUser = "";
		options.mongoPassword = "";
		options.mongoDatabaseName = MongoTestsSupport.getDb().getName();
		options.mongoCollectionName = "sensors";
		Runner runner = new Runner(options);
		runner.run();
		MongoCollection sensors = MongoTestsSupport.getJongo().getCollection("sensors");
		Iterator<Sensor> it = sensors.find().sort("{_id:1}").as(Sensor.class).iterator();
		assertThat(it.next().getSerial_no()).isEqualTo(-2130444288);
		assertThat(it.next().getSerial_no()).isEqualTo(-1610350592);
		assertThat(it.next().getSerial_no()).isEqualTo(50593792);
		assertThat(it.next().getSerial_no()).isEqualTo(553975808);
		assertThat(it.hasNext()).isFalse();
	}

}
