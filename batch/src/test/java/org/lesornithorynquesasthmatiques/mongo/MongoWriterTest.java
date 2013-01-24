package org.lesornithorynquesasthmatiques.mongo;
import static org.fest.assertions.api.Assertions.*;

import java.net.UnknownHostException;
import java.util.Collections;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.Sensor;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;

import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class MongoWriterTest {
	
	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	@Test
	public void should_write_one_object() throws UnknownHostException, MongoException {
		
		MongoWriter<Sensor> mongoWriter = new MongoWriter<Sensor>(
				MongoTestsHelper.getMongoHost(), 
				MongoTestsHelper.getMongoPort(), 
				null, null, 
				MongoTestsHelper.getDb().getName(), 
				"sensors");
		
		Sensor expected = new Sensor();
		expected.setLocation("Test");
		expected.setSerial_no(12345);
		expected.setTemperature(-15);
		expected.setPressure(42);
		
		mongoWriter.init();
		mongoWriter.write(Collections.singletonList(expected));
		
		MongoCollection sensors = MongoTestsHelper.getJongo().getCollection("sensors");
		Sensor actual = sensors.findOne("{location: 'Test'}").as(Sensor.class);
		
		assertThat(actual).isEqualsToByComparingFields(expected);
	}

}
