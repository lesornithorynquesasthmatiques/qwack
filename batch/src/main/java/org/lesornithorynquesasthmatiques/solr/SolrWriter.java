package org.lesornithorynquesasthmatiques.solr;

import java.io.IOException;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.lesornithorynquesasthmatiques.model.IndexedArtist;
import org.lesornithorynquesasthmatiques.model.IndexedSong;

/**
 * @author Alexandre Dutra
 *
 */
@ThreadSafe
public class SolrWriter {

	private final SolrServer songs;

	private final SolrServer artists;

	private final SolrServer suggestions;

	public SolrWriter(String songsCoreUrl, String suggestionsCoreUrl, String artistsCoreUrl) {
		SolrHelper.initSongsCore(songsCoreUrl);
		SolrHelper.initSuggestionsCore(suggestionsCoreUrl);
		SolrHelper.initArtistsCore(artistsCoreUrl);
		songs = SolrHelper.getSongsCore();
		artists = SolrHelper.getArtistsCore();
		suggestions = SolrHelper.getSuggestionsCore();
	}
	
	public void write(IndexedSong song, IndexedArtist artist) throws SolrServerException, IOException {
		this.songs.addBean(song);
		List<SolrInputDocument> suggestions = song.getSuggestions();
		if( ! suggestions.isEmpty()) this.suggestions.add(suggestions);
		this.artists.addBean(artist);
	}

}
