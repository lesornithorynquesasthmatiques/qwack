package org.lesornithorynquesasthmatiques.converter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.model.City;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

/**
 * @author Alexandre Dutra
 *
 */
public class CityConverter implements Converter<City> {

	private static final Splitter SPLITTER = Splitter.on(',').trimResults();
	
	@Override
	public List<City> convert(DataSubset ds) {
		int rows = ds.getRows();
		List<City> cities = new ArrayList<>(rows);
		for(int i = 0; i < rows; i++) {
			int j = 0;
			int geonameid = ds.getValue(i, j++);
			String name = ds.getValue(i, j++);
			String asciiname = ds.getValue(i, j++);
			String alternatename = ds.getValue(i, j++);
			double latitude = ds.getValue(i, j++);
			double longitude = ds.getValue(i, j++);
			String feature_class = ds.getValue(i, j++);
			String feature_code = ds.getValue(i, j++);
			String country_code = ds.getValue(i, j++);
			String cc2 = ds.getValue(i, j++);
			String admin1_code = ds.getValue(i, j++);
			String admin2_code = ds.getValue(i, j++);
			String admin3_code = ds.getValue(i, j++);
			String admin4_code = ds.getValue(i, j++);
			long population = ds.getValue(i, j++);
			int elevation = ds.getValue(i, j++);
			int dem = ds.getValue(i, j++);
			String timezone = ds.getValue(i, j++);
			String modification_date = ds.getValue(i, j++);
						
			City city = new City();
			city.setGeonameId(geonameid);
			city.setName(name);
			city.setAsciiName(asciiname);
			city.setAlternateNames(alternatename == null || alternatename.isEmpty() ? null : Sets.newLinkedHashSet(SPLITTER.split(alternatename)));
			city.setLocation(new double[]{longitude, latitude});
			city.setFeatureClass(feature_class);
			city.setFeatureCode(feature_code);
			city.setCountryCode(country_code);
			city.setAlternateCountryCodes(cc2 == null || cc2.isEmpty() ? null : Sets.newLinkedHashSet(SPLITTER.split(cc2)));
			city.setAdmin1Code(admin1_code);
			city.setAdmin2Code(admin2_code);
			city.setAdmin3Code(admin3_code);
			city.setAdmin4Code(admin4_code);
			city.setPopulation(population);
			city.setElevation(elevation);
			city.setDem(dem);
			city.setTimezone(timezone);
			city.setModificationDate(modification_date == null || modification_date.isEmpty() ? null : new LocalDate(modification_date));
			
			cities.add(city);
		}
		
		return cities;
	}

}
