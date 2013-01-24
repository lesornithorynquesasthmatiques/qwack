package org.lesornithorynquesasthmatiques.converter;

import java.util.List;

import org.lesornithorynquesasthmatiques.hdf.DataSubset;

public interface Converter<T> {

	public abstract List<T> convert(DataSubset ds);

}