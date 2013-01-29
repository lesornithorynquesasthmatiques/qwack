package org.lesornithorynquesasthmatiques.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"longitude", "latitude", "name"})
public class Location {

	/** location name (String) */
	private String name;
	
	/** longitude x latitude (double) */
	private double[] coords;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getCoords() {
		return coords;
	}

	public void setCoords(double[] coords) {
		this.coords = coords;
	}

	public void setCoords(double latitude, double longitude) {
		//longitude first!
		this.coords = new double[]{longitude, latitude};
	}

	public Double getLatitude() {
		return this.coords == null ? null : this.coords[1];
	}
	
	public Double getLongitude() {
		return this.coords == null ? null : this.coords[0];
	}
	
}
