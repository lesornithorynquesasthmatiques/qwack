package org.lesornithorynquesasthmatiques.meta;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.lesornithorynquesasthmatiques.helper.ReflectionHelper;

/**
 * A compound type.
 * @author Alexandre Dutra
 *
 */
public class CompoundType<T extends Externalizable> {

	private static final class MemberComparator implements Comparator<Member> {
		@Override
		public int compare(Member m1, Member m2) {
			return Integer.compare(m1.getIndex(), m2.getIndex());
		}
	}

	private static final MemberComparator MEMBER_COMPARATOR = new MemberComparator();
	
	private static final int OBJHEADERSIZE = 2;

	private static final int MAGICNUMBERSIZE = 4;
	
	private static final int[] MAGICNUMBER = { 0xac, 0xed, 0x00, 0x05 };
	
	private final Class<T> clazz;
	
	private final List<Member> members;

	private int compoundMemoryTypeId = -1;

	private int compoundFileTypeId = -1;
	
	private Set<Integer> openMemberIds;
	
	public CompoundType(Class<T> clazz) {
		this.clazz = clazz;
		try {
			Map<PropertyDescriptor, Datatype> map = ReflectionHelper.findPropertyAnnotations(clazz, Datatype.class);
			this.members = new ArrayList<>();
			Member oh = new Member();
			oh.setPropertyName("ObjectHeader");
			oh.setName("ObjectHeader");
			oh.setIndex(0);
			oh.setMemoryTypeId(HDF5Constants.H5T_NATIVE_SHORT);
			oh.setFileTypeId(HDF5Constants.H5T_STD_I16BE);
			oh.setSize(OBJHEADERSIZE);
			members.add(oh);
			this.openMemberIds = new HashSet<>();
			for (Entry<PropertyDescriptor, Datatype> entry : map.entrySet()) {
				Member m = new Member();
				PropertyDescriptor pd = entry.getKey();
				Datatype datatype = entry.getValue();
				m.setPropertyName(pd.getName());
				m.setType(pd.getReadMethod().getReturnType());
				m.setName(datatype.name());
				m.setIndex(datatype.index() + 1);
				m.setSize(datatype.size());
				int memoryTypeId = (int) HDF5Constants.class.getDeclaredField(datatype.memoryType()).get(null);
				if (memoryTypeId == HDF5Constants.H5T_C_S1) {
					memoryTypeId = createStringType(datatype.size());
					openMemberIds.add(memoryTypeId);
				}
				m.setMemoryTypeId(memoryTypeId);
				int fileTypeId = (int) HDF5Constants.class.getDeclaredField(datatype.fileType()).get(null);
				if (fileTypeId == HDF5Constants.H5T_C_S1) {
					fileTypeId = createStringType(datatype.size());
					openMemberIds.add(fileTypeId);
				}
				m.setFileTypeId(fileTypeId);
				members.add(m);
			}
			Collections.sort(members, MEMBER_COMPARATOR);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid compound type: " + clazz, e);
		}
	}

	/**
	 * @param length
	 * @return
	 * @throws HDF5LibraryException
	 */
	private static int createStringType(int length) throws HDF5LibraryException {
		final int stringTypeId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
		if (stringTypeId >= 0) {
			H5.H5Tset_size(stringTypeId, length);
			//H5.H5Tset_size(type_id, HDF5Constants.H5T_VARIABLE); --> does not work for compound types?
			//see http://www.hdfgroup.org/ftp/HDF5/hdf-java/src/hdf-java/examples/datatypes/H5Ex_T_VLString.java
		}
		return stringTypeId;
	}
	
	/**
	 * @return
	 * @throws HDF5LibraryException
	 */
	public int createMemoryTypeId() throws HDF5LibraryException {
		if(compoundMemoryTypeId == -1) {
			compoundMemoryTypeId = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, getDataSize());
			if (compoundMemoryTypeId >= 0) {
				for (int indx = 0; indx < members.size(); indx++) {
					Member member = members.get(indx);
					int typeId = member.getMemoryTypeId();
					if (typeId == HDF5Constants.H5T_C_S1) {
						typeId = createStringType(member.getSize());
					}
					H5.H5Tinsert(compoundMemoryTypeId, member.getName(), getOffset(indx), typeId);
				}
			}
		}
		return compoundMemoryTypeId;
	}

	/**
	 * @return
	 * @throws HDF5LibraryException
	 */
	public int createFileTypeId() throws HDF5LibraryException {
		if(compoundFileTypeId == -1) {
			compoundFileTypeId = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, getDataSize());
			if (compoundFileTypeId >= 0) {
				for (int indx = 0; indx < members.size(); indx++) {
					Member member = members.get(indx);
					int typeId = member.getFileTypeId();
					
					H5.H5Tinsert(compoundFileTypeId, member.getName(), getOffset(indx), typeId);
				}
			}
		}
		return compoundFileTypeId;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public int getDataSize() {
		int dataSize = 0;
		for (Member member : members) {
			dataSize += member.getDataSize();
		}
		return dataSize;
	}

	/**
	 * @param clazz
	 * @param memberIndex
	 * @return
	 */
	public int getOffset(int memberIndex) {
		int dataOffset = 0;
		for (int i = 0; i < memberIndex; i++) {
			Member member = members.get(i);
			dataOffset += member.getSize();
		}
		return dataOffset;
	}

	/**
	 * @param buf
	 * @param length
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public List<T> readObjects(byte[] buf, int length) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		List<T> objects = new ArrayList<>(length);
		byte[] dset_data = new byte[buf.length + MAGICNUMBERSIZE];
		for (int indx = 0; indx < MAGICNUMBERSIZE; indx++)
			dset_data[indx] = (byte) MAGICNUMBER[indx];
		for (int indx = 0; indx < buf.length; indx++)
			dset_data[indx + MAGICNUMBERSIZE] = buf[indx];
		ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(dset_data));
		for (int indx = 0; indx < length; indx++) {
			T o = clazz.newInstance();
			o.readExternal(objectIn);
			objects.add(o);
		}
		return objects;
	}
	
	/**
	 * @param objects
	 * @return
	 * @throws IOException
	 */
	public byte[] writeObjects(T[] objects) throws IOException {
//		Data size is the storage size for the members not the object.
//		 * Java Externalization also adds a 4-byte "Magic Number" to the beginning 
//		 * of the data stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream(MAGICNUMBERSIZE + (getDataSize() * objects.length));
		ObjectOutputStream oout = new ObjectOutputStream(baos);
		for (T o : objects) {
			o.writeExternal(oout);
			oout.flush();
		}
		oout.close();
		baos.close();
		byte[] dset_data = baos.toByteArray();
		byte[] write_data = new byte[dset_data.length - MAGICNUMBERSIZE];
		for (int indx = 0; indx < dset_data.length - MAGICNUMBERSIZE; indx++) {
			write_data[indx] = dset_data[indx + MAGICNUMBERSIZE];
		}
		return write_data;
	}
	
	public void close() throws HDF5LibraryException {
		if(this.compoundFileTypeId != -1)  H5.H5Tclose(this.compoundFileTypeId);
		if(this.compoundMemoryTypeId != -1) H5.H5Tclose(this.compoundMemoryTypeId);
		for (int id : openMemberIds) {
			if(id > 0) H5.H5Tclose(id);
		}
	}
}
