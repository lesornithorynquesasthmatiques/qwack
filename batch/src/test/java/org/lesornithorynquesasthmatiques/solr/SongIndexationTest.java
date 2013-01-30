package org.lesornithorynquesasthmatiques.solr;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.IndexedSong;

public class SongIndexationTest {

	@Rule
	public SolrTestsHelper solrHelper = new SolrTestsHelper();

	@Before
	public void addDocs() {
		solrHelper.addBeansAndCommit(
			new IndexedSong(
				new ObjectId().toString(),
				"Never Gonna Give You Up",
				"Rick Astley", 
				"Best Of", 
				1985, 
				"Los Angeles", 34.052337, -118.243680),
			new IndexedSong(
				new ObjectId().toString(),
				"La vie en rose",
				"Edith Piaf", 
				"Olympia", 
				1964, 
				"Paris", 48.8534, 2.3486)
		);
	}

	@Test
	public void should_index_documents() {
		assertThat(solrHelper.count("*:*")).isEqualTo(2);
	}

	@Test
	public void should_find_Rick_Astley() {
		assertThat(solrHelper.count("artistName:Astley")).isEqualTo(1);
	}

	@Test
	public void should_find_Los_Angeles() {
		SolrQuery query = new SolrQuery("location:[30,-120 TO 40,-110]");
		List<IndexedSong> docs = solrHelper.query(query, IndexedSong.class);
		assertThat(docs.size()).isEqualTo(1);
	}

}
