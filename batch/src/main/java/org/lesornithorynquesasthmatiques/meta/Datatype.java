package org.lesornithorynquesasthmatiques.meta;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Annotates a compound member with info about its data type.
 * @author Alexandre Dutra
 *
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface Datatype {
	
	String name();
	
	/**
	 * Zero-based.
	 * @return
	 */
	int index();
	
	String memoryType();
	
	String fileType();
	
	/**
	 * Number of bytes needed to store the value.
	 * @return
	 */
	int size();
	
	public static final int INTEGERSIZE = 4;
	public static final int DOUBLESIZE  = 8;
	
}