package org.lesornithorynquesasthmatiques.hdf;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * A small wrapper around raw HDF compound datasets to simplify access
 * to individual cells.
 * 
 * @author Alexandre Dutra
 *
 */
public class CompoundDataset {
	
	private List<Object> data;
	
	public CompoundDataset(List<Object> data) {
		this.data = data;
	}

	public int getRows(){
		Object members = data.get(0);
		return Array.getLength(members);
	}
	
	public int getColumns(){
		return data.size();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(int row, int column) {
		Object members = data.get(column);
		return (T) Array.get(members, row);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getColumn(int column) {
		return (List<T>) Arrays.asList(data.get(column));
	}
	
}
