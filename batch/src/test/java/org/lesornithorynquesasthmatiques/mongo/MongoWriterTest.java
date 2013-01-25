package org.lesornithorynquesasthmatiques.mongo;
import static org.fest.assertions.api.Assertions.*;

import java.net.UnknownHostException;
import java.util.Collections;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.City;
import org.lesornithorynquesasthmatiques.model.Sensor;

import com.google.common.collect.Lists;
import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoWriterTest {
	
	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	@Test
	public void should_write_one_sensor() throws UnknownHostException, MongoException {
		
		MongoWriter<Sensor> mongoWriter = new MongoWriter<Sensor>(
				mongoHelper.getMongoHost(), 
				mongoHelper.getMongoPort(), 
				null, null, 
				mongoHelper.getMongoWriteConcern().toString(),
				mongoHelper.getDb().getName(), 
				"sensors");
		
		Sensor expected = new Sensor();
		expected.setLocation("Test");
		expected.setSerial_no(12345);
		expected.setTemperature(-15);
		expected.setPressure(42);
		
		mongoWriter.init();
		mongoWriter.write(Collections.singletonList(expected));
		
		MongoCollection sensors = mongoHelper.getJongo().getCollection("sensors");
		Sensor actual = sensors.findOne("{location: 'Test'}").as(Sensor.class);
		
		assertThat(actual).isEqualsToByComparingFields(expected);
	}

	@Test
	public void should_write_4_cities() throws UnknownHostException, MongoException {
		
		MongoWriter<City> mongoWriter = new MongoWriter<City>(
				mongoHelper.getMongoHost(), 
				mongoHelper.getMongoPort(), 
				null, null, 
				mongoHelper.getMongoWriteConcern().toString(),
				mongoHelper.getDb().getName(), 
				"cities");
		
		City paris = new City();
		paris.setName("Paris");
		paris.setLocation(48.8534, 2.3486);

		City neuilly = new City();
		neuilly.setName("Neuilly-sur-Seine");
		neuilly.setLocation(48.8846, 2.26965);

		City lille = new City();
		lille.setName("Lille");
		lille.setLocation(50.63297, 3.05858);

		City lyon = new City();
		lyon.setName("Lyon");
		lyon.setLocation(45.75889, 4.84139);

		mongoWriter.init();
		mongoWriter.write(Lists.newArrayList(paris, neuilly, lille, lyon));
		
		MongoCollection cities = mongoHelper.getJongo().getCollection("cities");
		City actual = cities.findOne("{name: 'Paris'}").as(City.class);
		
		assertThat(actual).isEqualsToByComparingFields(paris);
		
	}

}
