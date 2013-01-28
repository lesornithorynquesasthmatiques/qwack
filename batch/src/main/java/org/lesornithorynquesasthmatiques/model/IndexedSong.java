package org.lesornithorynquesasthmatiques.model;

import org.apache.solr.client.solrj.beans.Field;

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

	/** song title (String) */
	@Field
	private String title;

	/** album name (String) */
	@Field
	private String release;

	/** song release year from MusicBrainz or 0 (int) */
	@Field
	private int year;

	/** artist name (String) */
	@Field
	private String artist;

	@Field
	private String locationName;

	@Field
	private String location;

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
		this.artist = artistName;
		this.locationName = locationName;
		if( ! Double.isNaN(latitude) && ! Double.isNaN(longitude)){
			this.location = latitude + "," + longitude;
		}
		this.year = year;
	}

	/**
	 * Constructor used to convert a Song into and IndexedSong.
	 * @param original
	 */
	public IndexedSong(Song original) {
		this.id = original.getId().toString();
		this.title = original.getTitle();
		this.release = original.getRelease();
		this.year = original.getYear();
		Artist artist = original.getArtist();
		if (artist != null) {
			this.artist = artist.getName();
			Location location = artist.getLocation();
			if (location != null) {
				this.locationName = location.getName();
				if( ! Double.isNaN(location.getLatitude()) && ! Double.isNaN(location.getLongitude())){
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

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
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

}
