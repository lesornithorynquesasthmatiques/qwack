package org.lesornithorynquesasthmatiques.hdf;

import java.lang.reflect.Array;
import java.util.List;

/**
 * A small wrapper around raw HDF compound data subsets to simplify access
 * to individual cells.
 * 
 * @author Alexandre Dutra
 *
 */
public class DataSubset {
	
	private List<Object> data;
	
	public DataSubset(List<Object> data) {
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
	
}
