package org.lesornithorynquesasthmatiques.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artist {

	/** Echo Nest ID (String) */
	@JsonProperty("_id")
	private String id;

	/** ID from musicbrainz.org (String) */
	private String mbid;

	/** ID from 7digital.com or -1 (Integer) */
	private Integer sdid;
	
	/** ID from playme.com, or -1 (Integer) */
	private Integer playmeid;

	/** artist name (String) */
	private String name;
	
	private Location location;
	
	/** tags from musicbrainz.org (List<String>) */
	private List<String> mbtags;

	/** artist terms */
	private List<String> terms;
	
	/** similar artist IDs */
	private List<String> similarArtists;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getPlaymeid() {
		return playmeid;
	}

	public void setPlaymeid(Integer playmeid) {
		this.playmeid = playmeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
	
	
}
