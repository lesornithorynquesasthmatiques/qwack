package org.lesornithorynquesasthmatiques.hdf;

import static org.fest.assertions.api.Assertions.*;

import java.util.List;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

import org.junit.Test;

public class SimpleReadTest {
	
	private static final String FILENAME = "src/test/resources/sensors.h5";

	private static String DATASETNAME = "Sensors/SENSORS";

	@Test
	public void should_read_entire_dataset() throws Exception {

		// Open an existing file.
		H5File file = new H5File(FILENAME, FileFormat.READ);
        file.open();
        
		// Open an existing dataset.
		H5CompoundDS dataset = (H5CompoundDS) file.get(DATASETNAME);
        dataset.open();

        @SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) dataset.getData();
        
		int[] serials = (int[]) data.get(0);
		assertThat(serials).containsSequence(-2130444288,-1610350592,50593792,553975808);
		
		String[] locations = (String[]) data.get(1);
		assertThat(locations).containsSequence("Exterior (static)","Intake", "Intake manifold", "Exhaust manifold");
		
		double[] temperatures = (double[]) data.get(2);
		assertThat(temperatures).containsSequence(1.1920392515278585E-14,-9.539767610322231E-233,4.667261468365516E-62,-2.439312392945428E19);
		
		double[] pressures = (double[]) data.get(3);
		assertThat(pressures).containsSequence(3.070733824170739E90,4.6672614692633455E-62,7.688168983161245E284,-1.4959242067111115E114);
		
		file.close();
		
	}


	@Test
	public void should_read_subset() throws Exception {

		// Open an existing file.
		H5File file = new H5File(FILENAME, FileFormat.READ);
        file.open();
        
		// Open an existing dataset.
		H5CompoundDS dataset = (H5CompoundDS) file.get(DATASETNAME);
        dataset.open();
        
        // Retrieves datatype and dataspace information from file and sets the dataset in memory.
        dataset.init();

        int rank = dataset.getRank(); // number of dimensions of the dataset
        assertThat(rank).isEqualTo(1);
        
        long[] dims = dataset.getDims(); // the dimension sizes of the dataset
        assertThat(dims).containsOnly(4);
        
        long[] start = dataset.getStartDims(); // the offset of the selection
        assertThat(start).containsOnly(0);
        
        long[] selected = dataset.getSelectedDims(); // the selected size of the dataset
        assertThat(selected).containsOnly(4);
        
        start[0] = 1;
        selected[0] = 2;
        
        @SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) dataset.getData();
        
		int[] serials = (int[]) data.get(0);
		assertThat(serials).containsSequence(-1610350592,50593792);
		
		String[] locations = (String[]) data.get(1);
		assertThat(locations).containsSequence("Intake", "Intake manifold");
		
		double[] temperatures = (double[]) data.get(2);
		assertThat(temperatures).containsSequence(-9.539767610322231E-233,4.667261468365516E-62);
		
		double[] pressures = (double[]) data.get(3);
		assertThat(pressures).containsSequence(4.6672614692633455E-62,7.688168983161245E284);
		
		file.close();
		
	}

	
}