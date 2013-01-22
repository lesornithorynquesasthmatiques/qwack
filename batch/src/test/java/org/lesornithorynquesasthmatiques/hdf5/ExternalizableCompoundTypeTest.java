package org.lesornithorynquesasthmatiques.hdf5;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.List;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

import org.junit.Before;
import org.junit.Test;
import org.lesornithorynquesasthmatiques.meta.CompoundType;
import org.lesornithorynquesasthmatiques.model.Sensor;

public class ExternalizableCompoundTypeTest {
	
	private static String DATASETNAME = "DS1";

	private static final int RANK = 1;

	private Sensor[] objects;

	private CompoundType<Sensor> compoundType = new CompoundType<Sensor>(Sensor.class);
		
	@Before
	public void initObjects(){
		
		objects = new Sensor[4];

		objects[0] = new Sensor();
		objects[0].setSerial_no(1153);
		objects[0].setLocation(new String("Exterior (static)"));
		objects[0].setTemperature(53.23);
		objects[0].setPressure(24.57);
		
		objects[1] = new Sensor();
		objects[1].setSerial_no(1184);
		objects[1].setLocation(new String("Intake"));
		objects[1].setTemperature(55.12);
		objects[1].setPressure(22.95);
		
		objects[2] = new Sensor();
		objects[2].setSerial_no(1027);
		objects[2].setLocation(new String("Intake manifold"));
		objects[2].setTemperature(103.55);
		objects[2].setPressure(31.23);
		
		objects[3] = new Sensor();
		objects[3].setSerial_no(1313);
		objects[3].setLocation(new String("Exhaust manifold"));
		objects[3].setTemperature(1252.89);
		objects[3].setPressure(84.11);

	}
	
	@Test
	public void should_write_dataset() throws Exception {
		
		File file = File.createTempFile("test", ".h5");
		file.deleteOnExit();

		// Create a new file using default properties.
		int file_id = H5.H5Fcreate(file.getAbsolutePath(), HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		assertThat(file_id).isGreaterThan(0);
		
		// Create dataspace. Setting maximum size to NULL sets the maximum
		// size to be the current size.
		long[] dims = { objects.length };
		int dataspace_id = H5.H5Screate_simple(RANK, dims, null);
		assertThat(dataspace_id).isGreaterThan(0);
		
		// Create the compound datatype for the file.
		int filetype_id = compoundType.createFileTypeId();
		assertThat(filetype_id).isGreaterThan(0);
		
		// Create the dataset.
		int dataset_id = H5.H5Dcreate(file_id, DATASETNAME, 
		        filetype_id, dataspace_id, 
		        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		assertThat(dataset_id).isGreaterThan(0);
		
		// Create the compound datatype for memory.
		int memtype_id = compoundType.createMemoryTypeId();
		assertThat(memtype_id).isGreaterThan(0);
		
		byte[] write_data = compoundType.writeObjects(objects);

		// Write the compound data to the dataset.
		int status = H5.H5Dwrite(dataset_id, memtype_id, 
		        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, 
		        write_data);
		assertThat(status).isGreaterThanOrEqualTo(0);
		
		H5.H5Dclose(dataset_id);
		H5.H5Sclose(dataspace_id);
		compoundType.close();
		H5.H5Fclose(file_id);
		
		//can't test this, the byte array is apparently not deterministic
//		byte[] expected = IOUtils.toByteArray(new FileInputStream("src/test/resources/sensors.h5"));
//		byte[] actual = IOUtils.toByteArray(new FileInputStream(file));
//		assertThat(actual).isEqualTo(expected);
		
	}

	@Test
	public void should_read_dataset() throws Exception {

		// Open an existing file.
		int file_id = H5.H5Fopen("src/test/resources/sensors.h5", HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
		assertThat(file_id).isGreaterThan(0);
		
		// Open an existing dataset.
		int dataset_id = H5.H5Dopen(file_id, DATASETNAME, HDF5Constants.H5P_DEFAULT);
		assertThat(dataset_id).isGreaterThan(0);
		
		// Get dataspace and allocate memory for read buffer.
		int dataspace_id = H5.H5Dget_space(dataset_id);
		assertThat(dataspace_id).isGreaterThan(0);
		
		//Get the total number of objects to read
		long[] dims = { 0 };
		H5.H5Sget_simple_extent_dims(dataspace_id, dims, null);
		assertThat(dims[0]).isEqualTo(objects.length);
		
		// Create the compound datatype for memory.
		int memtype_id = compoundType.createMemoryTypeId();
		assertThat(memtype_id).isGreaterThan(0);
		
		// allocate memory for read buffer.
		byte[] buf = new byte[objects.length * compoundType.getDataSize()];

		// Read data.
		//FIXME how to read by chunks instead of the whole dataset??
		H5.H5Dread(dataset_id, memtype_id, 
		        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, 
		        buf);
		
		// Transform data into objects
		List<Sensor> actualObjects = compoundType.readObjects(buf, objects.length);
		
		// Output the data to the screen.
		for (int indx = 0; indx < actualObjects.size(); indx++) {
			Sensor s = actualObjects.get(indx);
			assertThat(s).isEqualsToByComparingFields(objects[indx]);
		}

		H5.H5Dclose(dataset_id);
		H5.H5Sclose(dataspace_id);
		compoundType.close();
		H5.H5Fclose(file_id);

	}

}