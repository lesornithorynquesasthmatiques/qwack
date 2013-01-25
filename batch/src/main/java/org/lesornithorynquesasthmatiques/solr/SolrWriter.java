package org.lesornithorynquesasthmatiques.solr;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

/**
 * @author Alexandre Dutra
 *
 */
public class SolrWriter<T> {

	private static final Logger LOG = LoggerFactory.getLogger(SolrWriter.class);

	private final String solrUrl;
	
	private SolrServer solr;
	
	public SolrWriter(String solrUrl) {
		this.solrUrl = solrUrl;
	}

	public void init() throws UnknownHostException, MongoException {
		SolrHelper.initSolr(solrUrl);
		solr = SolrHelper.getSolr();
	}
	
	public void write(List<T> objects) throws SolrServerException, IOException {
		LOG.debug("Writing {} objects", objects.size());
		solr.addBeans(objects);
	}

}
