package org.lesornithorynquesasthmatiques.batch;
import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.Iterator;

import org.jongo.MongoCollection;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.Song;
import org.lesornithorynquesasthmatiques.mongo.MongoTestsHelper;
import org.lesornithorynquesasthmatiques.solr.SolrTestsHelper;

public class SongMainTest {

	private static final String NEVER_GONNA_GIVE_YOU_UP = "src/test/resources/hdf/TRAXLZU12903D05F94.h5";

	private static final String THE_MEANTIME = "src/test/resources/hdf/TRBIBGN128F42ADC66.h5";

	@Rule
	public MongoTestsHelper mongoHelper = new MongoTestsHelper();

	@Rule
	public SolrTestsHelper solrHelper = new SolrTestsHelper();

	@Rule
	public DirectoryManager directoryManager = new DirectoryManager();
	
	@Test
	public void should_read_and_write_4_objects() throws Exception {
		//Given
		directoryManager.copyFileToBaseDir(new File(NEVER_GONNA_GIVE_YOU_UP));
		directoryManager.copyFileToBaseDir(new File(THE_MEANTIME));
		String[] args = new String[]{
			"--directory"  , directoryManager.getBaseDirectory().getAbsolutePath(), 
			"--host"       , mongoHelper.getMongoHost(), 
			"--port"       , Integer.toString(mongoHelper.getMongoPort()), 
			"--database"   , mongoHelper.getDb().getName(), 
			"--collection" , "songs"
		};
		//When
		SongMain main = new SongMain();
		int status = main.run(args);
		//Then
		assertThat(status).isEqualTo(0);
		MongoCollection cities = mongoHelper.getJongo().getCollection("songs");
		Iterator<Song> it = cities.find().sort("{title:1}").as(Song.class).iterator();
		Song neverGonnaGiveYouUp = it.next();
		assertThat(neverGonnaGiveYouUp.getTitle()).isEqualTo("Never Gonna Give You Up");
		Song theMeantime = it.next();
		assertThat(theMeantime.getTitle()).isEqualTo("The Meantime");
		assertThat(it.hasNext()).isFalse();
	}

}
