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
import org.lesornithorynquesasthmatiques.model.City;

public class SolrTestsHelper extends SolrHelper implements TestRule {

	private static SolrServer core;

	public SolrServer getCore() {
		if(core == null) {
			try {
				core = EmbeddedSolrServerFactory.createEmbeddedSolrServer(
						new File("src/main/solr/solrconfig-test.xml"),
						new File("src/main/solr/schema.xml")
						);
			} catch (IOException e) {
				throw new RuntimeException("Failed to initialize core", e);
			}
		}
		return core;
	}

	public List<City> queryCities(String queryString) {
		try {
			QueryResponse response = getCore().query(new SolrQuery(queryString));
			return response.getBeans(City.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public long count(String queryString) {
		QueryResponse response;
		try {
			response = getCore().query(new SolrQuery(queryString));
			return response.getResults().getNumFound();
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}
	}

	public void addBeansAndCommit(Object... beans) {
		try {
			for (Object bean : beans) {
				getCore().addBean(bean);
			}
			getCore().commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public UpdateResponse addBeans(Collection<?> beans) throws SolrServerException, IOException {
		return getCore().addBeans(beans);
	}

	public UpdateResponse addBean(Object obj) throws IOException, SolrServerException {
		return getCore().addBean(obj);
	}

	public UpdateResponse commit(SolrServer core) throws SolrServerException, IOException {
		return core.commit();
	}

	public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException {
		return getCore().deleteByQuery(query);
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
		if(core != null) core.deleteByQuery("*:*");
	}
	
}