package org.lesornithorynquesasthmatiques.lang;

import com.google.common.primitives.Doubles;

public class Numbers {

	public static Double infiniteOrNanToNull(double val){
		if(Doubles.isFinite(val)){
			return val;
		}
		return null;
	}
	
	public static Integer negativeOrZeroToNull(int val){
		if(val > 0){
			return val;
		}
		return null;
	}
}
