package org.lesornithorynquesasthmatiques.model;

import java.util.Set;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City {

	/** integer id of record in geonames database */
	@JsonProperty("_id")
	private int geonameId;
	
	/** name of geographical point (utf8) varchar(200) */
	private String name;
	
	/** name of geographical point in plain ascii characters, varchar(200) */
	private String asciiName;
	
	/** alternatenames, comma separated varchar(5000) */
	private Set<String> alternateNames;
	
	/** latitude in decimal degrees (wgs84) */
	private double latitude;
	
	/** longitude in decimal degrees (wgs84) */
	private double longitude;
	
	/** see http://www.geonames.org/export/codes.html, char(1) */
	private String featureClass;
	
	/** see http://www.geonames.org/export/codes.html, varchar(10) */
	private String featureCode;
	
	/** ISO-3166 2-letter country code, 2 characters */
	private String countryCode;
	
	/**
	 * alternate country codes, comma separated, ISO-3166 2-letter country code,
	 * 60 characters
	 */
	private Set<String> alternateCountryCodes;
	
	/**
	 * fipscode (subject to change to iso code), see exceptions below, see file
	 * admin1Codes.txt for display names of this code; varchar(20)
	 */
	private String admin1Code;
	
	/**
	 * code for the second administrative division, a county in the US, see file
	 * admin2Codes.txt; varchar(80)
	 */
	private String admin2Code;
	
	/** code for third level administrative division, varchar(20) */
	private String admin3Code;
	
	/** code for fourth level administrative division, varchar(20) */
	private String admin4Code;
	
	/** bigint (8 byte int) */
	private long population;
	
	/** in meters, integer */
	private int elevation;
	
	/**
	 * digital elevation model, srtm3 or gtopo30, average elevation of 3''x3''
	 * (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm
	 * processed by cgiar/ciat.
	 */
	private int dem;
	
	/** the timezone id (see file timeZone.txt) varchar(40) */
	private DateTimeZone timezone;
	
	/** // date of last modification in yyyy-MM-dd format */
	private LocalDate modificationDate;

	public int getGeonameId() {
		return geonameId;
	}

	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAsciiName() {
		return asciiName;
	}

	public void setAsciiName(String asciiName) {
		this.asciiName = asciiName;
	}

	public Set<String> getAlternateNames() {
		return alternateNames;
	}

	public void setAlternateNames(Set<String> alternateNames) {
		this.alternateNames = alternateNames;
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

	public String getFeatureClass() {
		return featureClass;
	}

	public void setFeatureClass(String featureClass) {
		this.featureClass = featureClass;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Set<String> getAlternateCountryCodes() {
		return alternateCountryCodes;
	}

	public void setAlternateCountryCodes(Set<String> alternateCountryCodes) {
		this.alternateCountryCodes = alternateCountryCodes;
	}

	public String getAdmin1Code() {
		return admin1Code;
	}

	public void setAdmin1Code(String admin1Code) {
		this.admin1Code = admin1Code;
	}

	public String getAdmin2Code() {
		return admin2Code;
	}

	public void setAdmin2Code(String admin2Code) {
		this.admin2Code = admin2Code;
	}

	public String getAdmin3Code() {
		return admin3Code;
	}

	public void setAdmin3Code(String admin3Code) {
		this.admin3Code = admin3Code;
	}

	public String getAdmin4Code() {
		return admin4Code;
	}

	public void setAdmin4Code(String admin4Code) {
		this.admin4Code = admin4Code;
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	public int getDem() {
		return dem;
	}

	public void setDem(int dem) {
		this.dem = dem;
	}

	public DateTimeZone getTimezone() {
		return timezone;
	}

	public void setTimezone(DateTimeZone timezone) {
		this.timezone = timezone;
	}

	public LocalDate getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(LocalDate modificationDate) {
		this.modificationDate = modificationDate;
	}

}
