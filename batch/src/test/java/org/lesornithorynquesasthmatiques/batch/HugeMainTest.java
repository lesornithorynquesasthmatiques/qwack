package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsHelper;

/**
 * CAUTION: VERY long test. Should only be run for benchmark purposes.
 * @author Alexandre Dutra
 *
 */
@Ignore
public class HugeMainTest {

	private static final String FILENAME = "src/test/resources/FR-all.h5";

	private static String DATASET_PATH = "GEONAMES/FR";

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();
	
	@Test
	public void benchmark() throws Exception {
		//Given
		String[] args = new String[]{
			"--file"       , FILENAME, 
			"--dataset"    , DATASET_PATH, 
			"--host"       , MongoTestsHelper.getMongoHost(), 
			"--port"       , Integer.toString(MongoTestsHelper.getMongoPort()), 
			"--chunk-size" , "1000", 
			"--database"   , MongoTestsHelper.getDb().getName(), 
			"--collection" , "cities"
		};
		//When
		Main main = new Main();
		int status = main.run(args);
		//Then
		assertThat(status).isEqualTo(0);
	}

}
