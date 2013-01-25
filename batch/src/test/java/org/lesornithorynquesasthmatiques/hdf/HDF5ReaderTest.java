package org.lesornithorynquesasthmatiques.hdf;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;

import org.junit.Test;

public class HDF5ReaderTest {

	private static final String FILENAME = "src/test/resources/hdf/sensors.h5";

	private static String DATASET_PATH = "Sensors/SENSORS";

	@Test
	public void should_read_by_2() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 2);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		DataSubset data = reader.readNextChunk();
		assertThat(data.getRows()).isEqualTo(2);
		assertThat(data.getColumns()).isEqualTo(4);
		assertThat(data.getValue(0, 0)).isEqualTo(-2130444288);
		assertThat(data.getValue(1, 0)).isEqualTo(-1610350592);
		assertThat(reader.hasMoreChunks()).isTrue();
		data = reader.readNextChunk();
		assertThat(data.getRows()).isEqualTo(2);
		assertThat(data.getColumns()).isEqualTo(4);
		assertThat(data.getValue(0, 0)).isEqualTo(50593792);
		assertThat(data.getValue(1, 0)).isEqualTo(553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}

	@Test
	public void should_read_by_3() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 3);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		DataSubset data = reader.readNextChunk();
		assertThat(data.getRows()).isEqualTo(3);
		assertThat(data.getColumns()).isEqualTo(4);
		assertThat(data.getValue(0, 0)).isEqualTo(-2130444288);
		assertThat(data.getValue(1, 0)).isEqualTo(-1610350592);
		assertThat(data.getValue(2, 0)).isEqualTo(50593792);
		assertThat(reader.hasMoreChunks()).isTrue();
		assertThat(reader.itemsRead()).isEqualTo(3);
		data = reader.readNextChunk();
		assertThat(data.getRows()).isEqualTo(1);
		assertThat(data.getColumns()).isEqualTo(4);
		assertThat(data.getValue(0, 0)).isEqualTo(553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
		assertThat(reader.itemsRead()).isEqualTo(4);
	}

	@Test
	public void should_read_by_4() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 4);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		DataSubset data = reader.readNextChunk();
		assertThat(data.getRows()).isEqualTo(4);
		assertThat(data.getColumns()).isEqualTo(4);
		assertThat(data.getValue(0, 0)).isEqualTo(-2130444288);
		assertThat(data.getValue(1, 0)).isEqualTo(-1610350592);
		assertThat(data.getValue(2, 0)).isEqualTo(50593792);
		assertThat(data.getValue(3, 0)).isEqualTo(553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}

	@Test
	public void should_read_by_5() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 5);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		DataSubset data = reader.readNextChunk();
		assertThat(data.getRows()).isEqualTo(4);
		assertThat(data.getColumns()).isEqualTo(4);
		assertThat(data.getValue(0, 0)).isEqualTo(-2130444288);
		assertThat(data.getValue(1, 0)).isEqualTo(-1610350592);
		assertThat(data.getValue(2, 0)).isEqualTo(50593792);
		assertThat(data.getValue(3, 0)).isEqualTo(553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}


}
