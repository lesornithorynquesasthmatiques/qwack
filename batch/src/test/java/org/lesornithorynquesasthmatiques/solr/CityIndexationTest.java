package org.lesornithorynquesasthmatiques.solr;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Rule;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.model.City;

public class CityIndexationTest {

	@Rule
	public SolrTestsHelper solrHelper = new SolrTestsHelper();

	@Test
	public void should_index_documents() {
		solrHelper.addBeansAndCommit(new City("1", "Paris", 48.8534, 2.3486));
		solrHelper.addBeansAndCommit(new City("2", "Neuilly-sur-Seine", 48.8846, 2.26965));
		solrHelper.addBeansAndCommit(new City("3", "Lille", 50.63297, 3.05858));
		solrHelper.addBeansAndCommit(new City("4", "Lyon", 45.75889, 4.84139));
		assertThat(solrHelper.count("*:*")).isEqualTo(4);
	}

}
