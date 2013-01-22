package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;

import java.net.UnknownHostException;
import java.util.Collections;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.Sensor;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsSupport;

import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoWriterTest {
	
	@Rule
	public MongoTestsSupport mongoTestsSupport = new MongoTestsSupport();
	
	@Test
	public void should_write_one_object() throws UnknownHostException, MongoException {
		
		MongoWriter<Sensor> mongoWriter = new MongoWriter<Sensor>(
				MongoTestsSupport.getMongoHost(), 
				MongoTestsSupport.getMongoPort(), 
				null, null, 
				MongoTestsSupport.getDb().getName(), 
				"sensors");
		
		Sensor expected = new Sensor();
		expected.setLocation("Test");
		expected.setSerial_no(12345);
		expected.setTemperature(-15);
		expected.setPressure(42);
		
		mongoWriter.init();
		mongoWriter.write(Collections.singletonList(expected));
		
		MongoCollection sensors = MongoTestsSupport.getJongo().getCollection("sensors");
		Sensor actual = sensors.findOne("{location: 'Test'}").as(Sensor.class);
		
		assertThat(actual).isEqualsToByComparingFields(expected);
	}

}
