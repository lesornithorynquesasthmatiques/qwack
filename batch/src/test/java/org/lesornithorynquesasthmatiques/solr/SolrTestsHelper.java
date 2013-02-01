package org.lesornithorynquesasthmatiques.solr;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A specialized {@link SolrHelper} for Solr unit tests.
 */
public class SolrTestsHelper extends SolrHelper implements TestRule {

	static {
		try {
			SolrServer songs = EmbeddedSolrServerFactory.createEmbeddedSolrServer(
					new File("src/main/solr/solr/songs/conf")
					);
			SolrHelper.setSongsCore(songs);
			SolrServer suggestions = EmbeddedSolrServerFactory.createEmbeddedSolrServer(
					new File("src/main/solr/solr/suggestions/conf")
					);
			SolrHelper.setSuggestionsCore(suggestions);
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize Solr core", e);
		}
	}

	public <T> List<T> query(String queryString, Class<T> clazz) {
		return query(new SolrQuery(queryString), clazz);
	}

	public <T> List<T> query(SolrQuery query, Class<T> clazz) {
		try {
			QueryResponse response = getSongsCore().query(query);
			return response.getBeans(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public long count(String queryString) {
		QueryResponse response;
		try {
			response = getSongsCore().query(new SolrQuery(queryString));
			return response.getResults().getNumFound();
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}
	}

	public void addBeansAndCommit(Object... beans) {
		try {
			for (Object bean : beans) {
				getSongsCore().addBean(bean);
			}
			getSongsCore().commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public UpdateResponse addBeans(Collection<?> beans) throws SolrServerException, IOException {
		return getSongsCore().addBeans(beans);
	}

	public UpdateResponse addBean(Object obj) throws IOException, SolrServerException {
		return getSongsCore().addBean(obj);
	}

	public UpdateResponse commit(SolrServer core) throws SolrServerException, IOException {
		return core.commit();
	}

	public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException {
		return getSongsCore().deleteByQuery(query);
	}

	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();
				try {
					base.evaluate();
				} finally {
					after();
				}
			}
		};
	}

	protected void before() throws SolrServerException, IOException {
	}

	protected void after() throws SolrServerException, IOException {
		if(SolrHelper.getSongsCore() != null) SolrHelper.getSongsCore().deleteByQuery("*:*");
	}
	
}