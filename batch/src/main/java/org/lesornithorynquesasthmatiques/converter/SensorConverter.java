package org.lesornithorynquesasthmatiques.converter;

import java.util.ArrayList;
import java.util.List;

import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.model.Sensor;

/**
 * @author Alexandre Dutra
 *
 */
public class SensorConverter implements Converter<Sensor> {

	@Override
	public List<Sensor> convert(DataSubset ds) {
		List<Sensor> sensors = new ArrayList<>(ds.getRows());
		for(int i = 0; i < ds.getRows(); i++) {
			int serial = ds.getValue(i, 0);
			String location = ds.getValue(i, 1);
			double temperature = ds.getValue(i, 2);
			double pressure = ds.getValue(i, 3);
			Sensor sensor = new Sensor();
			sensor.setLocation(location);
			sensor.setSerial_no(serial);
			sensor.setTemperature(temperature);
			sensor.setPressure(pressure);
			sensors.add(sensor);
			
		}
		return sensors;
	}

}
