package org.lesornithorynquesasthmatiques.model;

import org.apache.solr.client.solrj.beans.Field;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Song {

	/**
	 * Mongo identifier.
	 */
	@JsonProperty("_id")
	@Field
	private ObjectId id;

	/** Echo Nest song ID (String) */
	private String songid;
	
	/** Echo Nest track ID (String) */
	private String trackid;
	
	/** ID from 7digital.com or -1 (int) */
	private int tracksdid;

	/** ID from 7digital.com or -1 (int) */
	private int releasesdid;
	
	/** song title (String) */
	private String title;

	/** album name (String) */
	private String release;
	
	private Artist artist;

	/** song release year from MusicBrainz or 0 (int) */
	private int year;
	
	/** audio hash code (String) */
	private String audiomd5;
	
	/** in seconds (double) */
	private double duration;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getSongid() {
		return songid;
	}

	public void setSongid(String songid) {
		this.songid = songid;
	}

	public String getTrackid() {
		return trackid;
	}

	public void setTrackid(String trackid) {
		this.trackid = trackid;
	}

	public int getTracksdid() {
		return tracksdid;
	}

	public void setTracksdid(int tracksdid) {
		this.tracksdid = tracksdid;
	}

	public int getReleasesdid() {
		return releasesdid;
	}

	public void setReleasesdid(int releasesdid) {
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getAudiomd5() {
		return audiomd5;
	}

	public void setAudiomd5(String audiomd5) {
		this.audiomd5 = audiomd5;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	
	
}
