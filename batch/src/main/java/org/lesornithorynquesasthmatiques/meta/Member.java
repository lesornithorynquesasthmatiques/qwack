package org.lesornithorynquesasthmatiques.meta;




/**
 * A member of a compound type.
 * @author Alexandre Dutra
 *
 */
public class Member {
	
	private String name;

	private String propertyName;
	
	private Class<?> type;
	
	private int index;
	
	private int memoryTypeId;
	
	private int fileTypeId;
	
	private int size;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getMemoryTypeId() {
		return memoryTypeId;
	}

	public void setMemoryTypeId(int memoryTypeId) {
		this.memoryTypeId = memoryTypeId;
	}

	public int getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(int fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public int getDataSize() {
		//TODO other dimensions?
		int dimension = 1;
		return getSize() * dimension;
	}

}