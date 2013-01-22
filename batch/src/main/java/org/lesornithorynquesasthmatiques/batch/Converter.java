package org.lesornithorynquesasthmatiques.batch;

import java.util.ArrayList;
import java.util.List;

import org.lesornithorynquesasthmatiques.model.Sensor;

/**
 * @author Alexandre Dutra
 *
 */
public class Converter {

	public List<Sensor> convertData(List<Object> data) {
		int[] serials = (int[]) data.get(0);
		String[] locations = (String[]) data.get(1);
		double[] temperatures = (double[]) data.get(2);
		double[] pressures = (double[]) data.get(3);
		List<Sensor> sensors = new ArrayList<>(serials.length);
		for (int i = 0; i < serials.length; i++) {
			int serial = serials[i];
			String location = locations[i];
			double temperature = temperatures[i];
			double pressure = pressures[i];
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
