package org.lesornithorynquesasthmatiques.model;


public class Location {

	/** location name (String) */
	private String name;
	
	/** latitude (double) */
	private double latitude;
	
	/** longitude (double) */
	private double longitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
