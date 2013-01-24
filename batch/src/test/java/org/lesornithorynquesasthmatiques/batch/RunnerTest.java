package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;
import java.io.File;
import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.converter.SensorConverter;
import org.lesornithorynquesasthmatiques.hdf.HDF5Reader;
import org.lesornithorynquesasthmatiques.model.Sensor;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsHelper;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;

public class RunnerTest {

	private static final String FILENAME = "src/test/resources/sensors.h5";

	private static String DATASET_PATH = "Sensors/SENSORS";

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	@Test
	public void should_read_and_write_4_objects() throws Exception {
		//Given
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 2);
		SensorConverter converter = new SensorConverter();
		MongoWriter<Sensor> writer = new MongoWriter<Sensor>(
			MongoTestsHelper.getMongoHost(), 
			MongoTestsHelper.getMongoPort(), 
			"", 
			"", 
			MongoTestsHelper.getDb().getName(), 
			"sensors");
		Runner<Sensor> runner = new Runner<Sensor>();
		runner.setReader(reader);
		runner.setConverter(converter);
		runner.setWriter(writer);
		//When
		runner.run();
		//Then
		MongoCollection sensors = MongoTestsHelper.getJongo().getCollection("sensors");
		long count = sensors.count();
		assertThat(count).isEqualTo(4);
		Iterator<Sensor> it = sensors.find().sort("{_id:1}").as(Sensor.class).iterator();
		assertThat(it.next().getSerial_no()).isEqualTo(-2130444288);
		assertThat(it.next().getSerial_no()).isEqualTo(-1610350592);
		assertThat(it.next().getSerial_no()).isEqualTo(50593792);
		assertThat(it.next().getSerial_no()).isEqualTo(553975808);
		assertThat(it.hasNext()).isFalse();
	}

}
