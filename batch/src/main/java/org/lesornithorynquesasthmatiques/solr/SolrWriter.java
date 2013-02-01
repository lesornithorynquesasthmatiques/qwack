package org.lesornithorynquesasthmatiques.solr;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.lesornithorynquesasthmatiques.model.IndexedSong;

/**
 * @author Alexandre Dutra
 *
 */
@ThreadSafe
public class SolrWriter {

	private final SolrServer songs;

	private final SolrServer suggestions;

	public SolrWriter(String songsCoreUrl, String suggestionsCoreUrl) {
		SolrHelper.initSongsCore(songsCoreUrl);
		SolrHelper.initSuggestionsCore(suggestionsCoreUrl);
		songs = SolrHelper.getSongsCore();
		suggestions = SolrHelper.getSuggestionsCore();
	}
	
	public void write(IndexedSong song) throws SolrServerException, IOException {
		this.songs.addBean(song);
		this.suggestions.add(song.getSuggestions());
	}

}
