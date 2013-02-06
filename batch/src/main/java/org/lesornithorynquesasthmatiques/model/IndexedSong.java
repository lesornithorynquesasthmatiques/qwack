package org.lesornithorynquesasthmatiques.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;

import com.google.common.base.Objects;

/**
 * A simplified view of a song to be indexed by Solr.
 * 
 * @author Alexandre Dutra
 * 
 */
public class IndexedSong {

	/**
	 * Solr identifier.
	 */
	@Field
	private String id;

	/** Echo Nest track ID (String) */
	@Field
	private String trackid;
	
	/** ID from 7digital.com or null */
	@Field
	private Integer tracksdid;

	/** ID from 7digital.com or null */
	@Field
	private Integer releasesdid;
	
	/** song title (String) */
	@Field
	private String title;

	/** album name (String) */
	@Field
	private String release;

	/** song release year from MusicBrainz */
	@Field
	private Integer year;

	/** artist name (String) */
	@Field
	private String artistName;
	
	/** Echo Nest ID (String) */
	@Field
	private String artistId;

	/** ID from musicbrainz.org (String) */
	@Field
	private String artistMbid;

	/** ID from 7digital.com or -1 (Integer) */
	@Field
	private Integer artistSdid;
	
	/** ID from playme.com, or -1 (Integer) */
	@Field
	private Integer artistPlaymeid;

	@Field
	private String locationName;

	@Field
	private String location;
	
	/** tags from musicbrainz.org (List<String>) */
	@Field
	private List<String> mbtags;

	/** artist terms */
	@Field
	private List<String> terms;
	
	/** similar artist IDs */
	@Field
	private List<String> similarArtists;
	

	public IndexedSong() {
	}

	/**
	 * Convenience constructor for unit tests.
	 * @param id
	 * @param title
	 * @param artistName
	 * @param release
	 * @param year
	 * @param locationName
	 * @param latitude
	 * @param longitude
	 */
	public IndexedSong(String id, String title, String artistName, String release, int year, String locationName, double latitude, double longitude) {
		this.id = id;
		this.title = title;
		this.release = release;
		this.artistName = artistName;
		this.locationName = locationName;
		this.location = latitude + "," + longitude;
		this.year = year;
	}

	/**
	 * Constructor used to convert a Song into and IndexedSong.
	 * @param original
	 */
	public IndexedSong(Song original) {
		this.id = original.getId();
		this.trackid = original.getTrackid();
		this.tracksdid = original.getTracksdid();
		this.releasesdid = original.getReleasesdid();
		this.title = original.getTitle();
		this.release = original.getRelease();
		this.year = original.getYear();
		Artist artist = original.getArtist();
		if (artist != null) {
			this.artistName = artist.getName();
			this.artistId = artist.getId();
			this.artistMbid = artist.getMbid();
			this.artistSdid = artist.getSdid();
			this.artistPlaymeid = artist.getPlaymeid();
			this.mbtags = artist.getMbtags();
			this.terms = artist.getTerms();
			this.similarArtists = artist.getSimilarArtists();
			Location location = artist.getLocation();
			if (location != null) {
				this.locationName = location.getName();
				if( location.getCoords() != null){
				this.location = 
						location.getLatitude() 
						+ ","
						+ location.getLongitude();
				}
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getTrackid() {
		return trackid;
	}

	public void setTrackid(String trackid) {
		this.trackid = trackid;
	}

	public Integer getTracksdid() {
		return tracksdid;
	}

	public void setTracksdid(Integer tracksdid) {
		this.tracksdid = tracksdid;
	}

	public Integer getReleasesdid() {
		return releasesdid;
	}

	public void setReleasesdid(Integer releasesdid) {
		this.releasesdid = releasesdid;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getArtistMbid() {
		return artistMbid;
	}

	public void setArtistMbid(String artistMbid) {
		this.artistMbid = artistMbid;
	}

	public Integer getArtistSdid() {
		return artistSdid;
	}

	public void setArtistSdid(Integer artistSdid) {
		this.artistSdid = artistSdid;
	}

	public Integer getArtistPlaymeid() {
		return artistPlaymeid;
	}

	public void setArtistPlaymeid(Integer artistPlaymeid) {
		this.artistPlaymeid = artistPlaymeid;
	}

	public List<String> getMbtags() {
		return mbtags;
	}

	public void setMbtags(List<String> mbtags) {
		this.mbtags = mbtags;
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	public List<String> getSimilarArtists() {
		return similarArtists;
	}

	public void setSimilarArtists(List<String> similarArtists) {
		this.similarArtists = similarArtists;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public static enum SuggestionType {
		TITLE, RELEASE, ARTIST, LOCATION, KEYWORD
	}
	
	public List<SolrInputDocument> getSuggestions() {
		List<SolrInputDocument> suggestions = new ArrayList<>();
		if(title != null) suggestions.add(toSuggestionDoc(title, SuggestionType.TITLE));
		if(release != null) suggestions.add(toSuggestionDoc(release, SuggestionType.RELEASE));
		if(artistName != null) suggestions.add(toSuggestionDoc(artistName, SuggestionType.ARTIST));
		if(locationName != null) suggestions.add(toSuggestionDoc(locationName, SuggestionType.LOCATION));
		if(mbtags != null) {
			for (String	tag : mbtags) {
				suggestions.add(toSuggestionDoc(tag, SuggestionType.KEYWORD));
			}
		}
		return suggestions;
	}

	private SolrInputDocument toSuggestionDoc(String textsuggest, SuggestionType type) {
		Map<String,Object> op = new HashMap<String,Object>(); 
	    op.put("inc", 1); 
		SolrInputDocument doc = new SolrInputDocument(); 
	    doc.addField("id", Objects.hashCode(type, textsuggest)); 
	    doc.addField("s", textsuggest); 
	    doc.addField("t", type.ordinal()); 
	    doc.addField("p", op);
		return doc;
	}

}
