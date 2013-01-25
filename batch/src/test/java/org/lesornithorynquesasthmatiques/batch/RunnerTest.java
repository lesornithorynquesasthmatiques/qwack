package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.converter.CityConverter;
import org.lesornithorynquesasthmatiques.converter.SensorConverter;
import org.lesornithorynquesasthmatiques.hdf.HDF5Reader;
import org.lesornithorynquesasthmatiques.model.City;
import org.lesornithorynquesasthmatiques.model.Sensor;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsHelper;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;

public class RunnerTest {

	private static final String SENSORS_FILE = "src/test/resources/hdf/sensors.h5";

	private static String SENSORS_DATASET_PATH = "Sensors/SENSORS";

	private static final String CITIES_FILE = "src/test/resources/hdf/FR-small.h5";

	private static String CITIES_DATASET_PATH = "GEONAMES/FR";

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	@Test
	public void should_read_and_write_4_sensors() throws Exception {
		//Given
		HDF5Reader reader = new HDF5Reader(new File(SENSORS_FILE), SENSORS_DATASET_PATH, 2);
		SensorConverter converter = new SensorConverter();
		MongoWriter<Sensor> writer = new MongoWriter<Sensor>(
			mongoHelper.getMongoHost(), 
			mongoHelper.getMongoPort(), 
			"", 
			"", 
			mongoHelper.getMongoWriteConcern().toString(),
			mongoHelper.getDb().getName(), 
			"sensors");
		Runner<Sensor> runner = new Runner<Sensor>();
		runner.setReader(reader);
		runner.setConverter(converter);
		runner.setMongoWriter(writer);
		runner.setConversionPool(new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.CallerRunsPolicy()));
		runner.setMongoPool(new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.CallerRunsPolicy()));
		//When
		runner.init();
		runner.run();
		//Then
		MongoCollection sensors = mongoHelper.getJongo().getCollection("sensors");
		long count = sensors.count();
		assertThat(count).isEqualTo(4);
		Iterator<Sensor> it = sensors.find().sort("{serial_no:1}").as(Sensor.class).iterator();
		assertThat(it.next().getSerial_no()).isEqualTo(-2130444288);
		assertThat(it.next().getSerial_no()).isEqualTo(-1610350592);
		assertThat(it.next().getSerial_no()).isEqualTo(50593792);
		assertThat(it.next().getSerial_no()).isEqualTo(553975808);
		assertThat(it.hasNext()).isFalse();
	}


	@Test
	public void should_read_and_write_4_cities() throws Exception {
		//Given
		HDF5Reader reader = new HDF5Reader(new File(CITIES_FILE), CITIES_DATASET_PATH, 2);
		CityConverter converter = new CityConverter();
		MongoWriter<City> writer = new MongoWriter<City>(
			mongoHelper.getMongoHost(), 
			mongoHelper.getMongoPort(), 
			"", 
			"", 
			mongoHelper.getMongoWriteConcern().toString(),
			mongoHelper.getDb().getName(), 
			"cities");
		Runner<City> runner = new Runner<City>();
		runner.setReader(reader);
		runner.setConverter(converter);
		runner.setMongoWriter(writer);
		runner.setConversionPool(new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.CallerRunsPolicy()));
		runner.setMongoPool(new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.CallerRunsPolicy()));
		//When
		runner.init();
		runner.run();
		//Then
		MongoCollection cities = mongoHelper.getJongo().getCollection("cities");
		long count = cities.count();
		assertThat(count).isEqualTo(4);
		Iterator<City> it = cities.find().sort("{name:1}").as(City.class).iterator();
		assertThat(it.next().getName()).isEqualTo("Lille");
		assertThat(it.next().getName()).isEqualTo("Lyon");
		assertThat(it.next().getName()).isEqualTo("Neuilly-sur-Seine");
		City paris = it.next();
		assertThat(paris.getName()).isEqualTo("Paris");
		assertThat(paris.getAlternateNames()).contains("Departement de Paris","Département de Paris","Parigi","Paris");
		assertThat(it.hasNext()).isFalse();
	}

	
}
