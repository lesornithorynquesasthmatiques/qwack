package org.lesornithorynquesasthmatiques.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;


/**
 * A helper that holds a singleton instance of {@link SolrServer}.
 * 
 * @author Alexandre Dutra
 *
 */
public class SolrHelper {

	private static SolrServer solr;
	
	public static void initSolr(String solrUrl) {
		if(solr == null) {
			solr = new HttpSolrServer(solrUrl);
		}
	}

	public static SolrServer getSolr() {
		return solr;
	}

	/**
	 * Back-door for unit tests.
	 * @param mongo
	 */
	static void setSolr(SolrServer solr) {
		SolrHelper.solr = solr;
	}

}
