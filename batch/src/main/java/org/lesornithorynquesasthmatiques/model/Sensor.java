package org.lesornithorynquesasthmatiques.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.jongo.marshall.jackson.id.Id;
import org.lesornithorynquesasthmatiques.meta.Datatype;

public class Sensor implements java.io.Externalizable {
	
	@Id
    private String id;
	
	private final static int LOCATIONSIZE = 80;
	
	@Datatype(name="Serial number", index=0, memoryType="H5T_NATIVE_INT", fileType="H5T_STD_I32BE", size=Datatype.INTEGERSIZE)
	private int serial_no;
	
	@Datatype(name="Location", index=1, memoryType="H5T_C_S1", fileType="H5T_C_S1", size=LOCATIONSIZE)
	private String location;
	
	@Datatype(name="Temperature (F)", index=2, memoryType="H5T_NATIVE_DOUBLE", fileType="H5T_IEEE_F64BE", size=Datatype.DOUBLESIZE)
	private double temperature;
	
	@Datatype(name="Pressure (inHg)", index=3, memoryType="H5T_NATIVE_DOUBLE", fileType="H5T_IEEE_F64BE", size=Datatype.DOUBLESIZE)
	private double pressure;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSerial_no() {
		return serial_no;
	}

	public void setSerial_no(int serial_no) {
		this.serial_no = serial_no;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException {
		serial_no = in.readInt();
		byte[] tempbuf = new byte[LOCATIONSIZE];
		for (int indx = 0; indx < LOCATIONSIZE; indx++) {
			tempbuf[indx] = in.readByte();
		}
		location = new String(tempbuf, "UTF-8").trim();
		temperature = in.readDouble();
		pressure = in.readDouble();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(serial_no);
		for (int indx = 0; indx < LOCATIONSIZE; indx++) {
			if (indx < location.length())
				//FIXME cast char -> byte
				out.writeByte(location.charAt(indx));
			else
				out.writeByte(0);
		}
		out.writeDouble(temperature);
		out.writeDouble(pressure);
	}
}
