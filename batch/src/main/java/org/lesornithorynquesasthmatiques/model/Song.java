package org.lesornithorynquesasthmatiques.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Song {

	/** Echo Nest song ID (String) */
	@JsonProperty("_id")
	private String id;
	
	/** Echo Nest track ID (String) */
	private String trackid;
	
	/** ID from 7digital.com or null */
	private Integer tracksdid;

	/** ID from 7digital.com or null */
	private Integer releasesdid;
	
	/** song title (String) */
	private String title;

	/** album name (String) */
	private String release;
	
	private Artist artist;

	/** song release year from MusicBrainz or null */
	private Integer year;
	
	/** audio hash code (String) */
	private String audiomd5;
	
	/** in seconds (double) */
	private Double duration;

	public Song() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getAudiomd5() {
		return audiomd5;
	}

	public void setAudiomd5(String audiomd5) {
		this.audiomd5 = audiomd5;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}
	
	
	
}
