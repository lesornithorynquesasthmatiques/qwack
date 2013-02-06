package org.lesornithorynquesasthmatiques.model;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

/**
 * A simplified view of an artist to be indexed by Solr.
 * 
 * @author Alexandre Dutra
 * 
 */
public class IndexedArtist {

	/**
	 * Solr identifier.
	 */
	@Field
	private String id;

	/** artist name (String) */
	@Field
	private String name;
	
	/** ID from musicbrainz.org (String) */
	@Field
	private String mbid;

	/** ID from 7digital.com or -1 (Integer) */
	@Field
	private Integer sdid;
	
	/** ID from playme.com, or -1 (Integer) */
	@Field
	private Integer pmid;

	@Field
	private String locName;

	@Field
	private String loc;
	
	/** tags from musicbrainz.org (List<String>) */
	@Field
	private List<String> mbtags;

	/** artist terms */
	@Field
	private List<String> terms;
	
	/** similar artist IDs */
	@Field
	private List<String> simArtists;
	

	public IndexedArtist() {
	}

	public IndexedArtist(Artist artist) {
		this.id = artist.getId();
		this.name = artist.getName();
		this.mbid = artist.getMbid();
		this.sdid = artist.getSdid();
		this.pmid = artist.getPlaymeid();
		this.mbtags = artist.getMbtags();
		this.terms = artist.getTerms();
		this.simArtists = artist.getSimilarArtists();
		Location location = artist.getLocation();
		if (location != null) {
			this.locName = location.getName();
			if( location.getCoords() != null){
			this.loc = 
					location.getLatitude() 
					+ ","
					+ location.getLongitude();
			}
		}

	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMbid() {
		return mbid;
	}


	public void setMbid(String mbid) {
		this.mbid = mbid;
	}


	public Integer getSdid() {
		return sdid;
	}


	public void setSdid(Integer sdid) {
		this.sdid = sdid;
	}


	public Integer getPmid() {
		return pmid;
	}


	public void setPmid(Integer pmid) {
		this.pmid = pmid;
	}


	public String getLocName() {
		return locName;
	}


	public void setLocName(String locName) {
		this.locName = locName;
	}


	public String getLoc() {
		return loc;
	}


	public void setLoc(String loc) {
		this.loc = loc;
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

	public List<String> getSimArtists() {
		return simArtists;
	}

	public void setSimArtists(List<String> simArtists) {
		this.simArtists = simArtists;
	}


}
