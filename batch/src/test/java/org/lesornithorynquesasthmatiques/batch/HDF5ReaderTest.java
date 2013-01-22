package org.lesornithorynquesasthmatiques.batch;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class HDF5ReaderTest {

	private static final String FILENAME = "src/test/resources/sensors2.h5";

	private static String DATASET_PATH = "Sensors/SENSORS";

	@Test
	public void should_read_by_2() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 2);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		List<Object> data = reader.readNextChunk();
		assertThat(data).hasSize(4);
		assertThat((int[]) data.get(0)).hasSize(2);
		assertThat((int[]) data.get(0)).containsSequence(-2130444288,-1610350592);
		assertThat(reader.hasMoreChunks()).isTrue();
		data = reader.readNextChunk();
		assertThat(data).hasSize(4);
		assertThat((int[]) data.get(0)).hasSize(2);
		assertThat((int[]) data.get(0)).containsSequence(50593792,553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}

	@Test
	public void should_read_by_3() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 3);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		List<Object> data = reader.readNextChunk();
		assertThat(data).hasSize(4);
		assertThat((int[]) data.get(0)).hasSize(3);
		assertThat((int[]) data.get(0)).containsSequence(-2130444288,-1610350592,50593792);
		assertThat(reader.hasMoreChunks()).isTrue();
		data = reader.readNextChunk();
		assertThat(data).hasSize(4);
		assertThat((int[]) data.get(0)).hasSize(1);
		assertThat((int[]) data.get(0)).containsSequence(553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}

	@Test
	public void should_read_by_4() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 4);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		List<Object> data = reader.readNextChunk();
		assertThat(data).hasSize(4);
		assertThat((int[]) data.get(0)).hasSize(4);
		assertThat((int[]) data.get(0)).containsSequence(-2130444288,-1610350592,50593792,553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}

	@Test
	public void should_read_by_5() throws Exception {
		HDF5Reader reader = new HDF5Reader(new File(FILENAME), DATASET_PATH, 5);
		reader.init();
		assertThat(reader.hasMoreChunks()).isTrue();
		List<Object> data = reader.readNextChunk();
		assertThat(data).hasSize(4);
		assertThat((int[]) data.get(0)).hasSize(4);
		assertThat((int[]) data.get(0)).containsSequence(-2130444288,-1610350592,50593792,553975808);
		assertThat(reader.hasMoreChunks()).isFalse();
	}


}
