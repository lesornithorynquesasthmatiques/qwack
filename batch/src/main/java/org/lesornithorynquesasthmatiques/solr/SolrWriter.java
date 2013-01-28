package org.lesornithorynquesasthmatiques.solr;

import java.io.IOException;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * @author Alexandre Dutra
 *
 */
@ThreadSafe
public class SolrWriter<T> {

	private final SolrServer solr;
	
	public SolrWriter(String solrUrl) {
		SolrHelper.initSolr(solrUrl);
		solr = SolrHelper.getSolr();
	}
	
	public void write(T object) throws SolrServerException, IOException {
		solr.addBean(object);
	}
	
	public void write(List<T> objects) throws SolrServerException, IOException {
		solr.addBeans(objects);
	}

}
