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

	private static SolrServer songs;
	
	private static SolrServer artists;

	private static SolrServer suggestions;

	public static void initSongsCore(String solrUrl) {
		if(songs == null) {
			songs = new HttpSolrServer(solrUrl);
		}
	}

	public static void initSuggestionsCore(String solrUrl) {
		if(suggestions == null) {
			suggestions = new HttpSolrServer(solrUrl);
		}
	}

	public static void initArtistsCore(String solrUrl) {
		if(artists == null) {
			artists = new HttpSolrServer(solrUrl);
		}
	}

	public static SolrServer getSongsCore() {
		return songs;
	}

	public static SolrServer getArtistsCore() {
		return artists;
	}

	public static SolrServer getSuggestionsCore() {
		return suggestions;
	}

	/**
	 * Back-door for unit tests.
	 * @param solr
	 */
	static void setSongsCore(SolrServer solr) {
		SolrHelper.songs = solr;
	}
	
	/**
	 * Back-door for unit tests.
	 * @param solr
	 */
	static void setArtistsCore(SolrServer solr) {
		SolrHelper.artists = solr;
	}
	
	/**
	 * Back-door for unit tests.
	 * @param solr
	 */
	static void setSuggestionsCore(SolrServer solr) {
		SolrHelper.suggestions = solr;
	}

}
