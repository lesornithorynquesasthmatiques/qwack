package org.lesornithorynquesasthmatiques.hdf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

import com.google.common.base.Splitter;

/**
 * A small tool to generate H5 files from flat CSV files.
 * @author Alexandre Dutra
 *
 */
public class HDF5FileWriter {

	public static void main(String[] args) throws Exception {
		H5File h5file = new H5File("cities" + System.currentTimeMillis() + ".h5", FileFormat.WRITE);
		h5file.createNewFile();
        h5file.open();
        Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) h5file.getRootNode()).getUserObject(); 
        Group group = h5file.createGroup( "GEONAMES", root); 
        int linesToRead = 4;
    	int chunkSize = 2;
        H5CompoundDS dataset = (H5CompoundDS) h5file.createCompoundDS(
        		"FR", 
        		group, 
        		new long[]{linesToRead}, 
        		null,
        		new long[]{chunkSize}, 
        		6, 
        		new String[]{
					"geonameid", // integer id of record in geonames database
					"name", // name of geographical point (utf8) varchar(200)
					"asciiname", // name of geographical point in plain ascii characters, varchar(200)
					"alternatenames", // alternatenames, comma separated varchar(5000)
					"latitude", // latitude in decimal degrees (wgs84)
					"longitude", // longitude in decimal degrees (wgs84)
					"feature class", // see http://www.geonames.org/export/codes.html, char(1)
					"feature code", // see http://www.geonames.org/export/codes.html, varchar(10)
					"country code", // ISO-3166 2-letter country code, 2 characters
					"cc2", // alternate country codes, comma separated, ISO-3166 2-letter country code, 60 characters
					"admin1 code", // fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
					"admin2 code", // code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80) 
					"admin3 code", // code for third level administrative division, varchar(20)
					"admin4 code", // code for fourth level administrative division, varchar(20)
					"population", // bigint (8 byte int) 
					"elevation", // in meters, integer
					"dem", // digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
					"timezone", // the timezone id (see file timeZone.txt) varchar(40)
					"modification date" // date of last modification in yyyy-MM-dd format
        		}, 
        		new Datatype[]{
    				h5file.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE), // integer id of record in geonames database
    				h5file.createDatatype(Datatype.CLASS_STRING, 200, Datatype.NATIVE, Datatype.NATIVE), // name of geographical point (utf8) varchar(200)
    				h5file.createDatatype(Datatype.CLASS_STRING, 200, Datatype.NATIVE, Datatype.NATIVE), // name of geographical point in plain ascii characters, varchar(200)
    				h5file.createDatatype(Datatype.CLASS_STRING, 5000, Datatype.NATIVE, Datatype.NATIVE), // alternatenames, comma separated varchar(5000)
    				h5file.createDatatype(Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE), // latitude in decimal degrees (wgs84)
    				h5file.createDatatype(Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE), // longitude in decimal degrees (wgs84)
    				h5file.createDatatype(Datatype.CLASS_STRING, 1, Datatype.NATIVE, Datatype.NATIVE), // see http://www.geonames.org/export/codes.html, char(1)
    				h5file.createDatatype(Datatype.CLASS_STRING, 10, Datatype.NATIVE, Datatype.NATIVE), // see http://www.geonames.org/export/codes.html, varchar(10)
    				h5file.createDatatype(Datatype.CLASS_STRING, 2, Datatype.NATIVE, Datatype.NATIVE), // ISO-3166 2-letter country code, 2 characters
    				h5file.createDatatype(Datatype.CLASS_STRING, 60, Datatype.NATIVE, Datatype.NATIVE), // alternate country codes, comma separated, ISO-3166 2-letter country code, 60 characters
    				h5file.createDatatype(Datatype.CLASS_STRING, 20, Datatype.NATIVE, Datatype.NATIVE), // fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
    				h5file.createDatatype(Datatype.CLASS_STRING, 80, Datatype.NATIVE, Datatype.NATIVE), // code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80) 
    				h5file.createDatatype(Datatype.CLASS_STRING, 20, Datatype.NATIVE, Datatype.NATIVE), // code for third level administrative division, varchar(20)
    				h5file.createDatatype(Datatype.CLASS_STRING, 20, Datatype.NATIVE, Datatype.NATIVE), // code for fourth level administrative division, varchar(20)
    				h5file.createDatatype(Datatype.CLASS_INTEGER, 8, Datatype.NATIVE, Datatype.NATIVE), // bigint (8 byte int) 
    				h5file.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE), // in meters, integer
    				h5file.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE), // digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
    				h5file.createDatatype(Datatype.CLASS_STRING, 40, Datatype.NATIVE, Datatype.NATIVE), // the timezone id (see file timeZone.txt) varchar(40)
    				h5file.createDatatype(Datatype.CLASS_STRING, 10, Datatype.NATIVE, Datatype.NATIVE) // date of last modification in yyyy-MM-dd format
            		}, 
        		new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, 
        		null);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\Téléchargements\\FR\\cities.txt"), "UTF-8"));
    	Splitter splitter = Splitter.on("\t");

		int[] geonameids = new int[chunkSize];
		String[] names = new String[chunkSize];
		String[] asciinames = new String[chunkSize];
		String[] alternatenames = new String[chunkSize];
		double[] latitudes = new double[chunkSize];
		double[] longitudes = new double[chunkSize];
		String[] feature_classes = new String[chunkSize];
		String[] feature_codes = new String[chunkSize];
		String[] country_codes = new String[chunkSize];
		String[] cc2s = new String[chunkSize];
		String[] admin1_codes = new String[chunkSize];
		String[] admin2_codes = new String[chunkSize];
		String[] admin3_codes = new String[chunkSize];
		String[] admin4_codes = new String[chunkSize];
		long[] populations = new long[chunkSize];
		int[] elevations = new int[chunkSize];
		int[] dems = new int[chunkSize];
		String[] timezones = new String[chunkSize];
		String[] modification_dates = new String[chunkSize];
		
		List<Object> data = new Vector<>();
		
		data.add(geonameids);
		data.add(names);
		data.add(asciinames);
		data.add(alternatenames);
		data.add(latitudes);
		data.add(longitudes);
		data.add(feature_classes);
		data.add(feature_codes);
		data.add(country_codes);
		data.add(cc2s);
		data.add(admin1_codes);
		data.add(admin2_codes);
		data.add(admin3_codes);
		data.add(admin4_codes);
		data.add(populations);
		data.add(elevations);
		data.add(dems);
		data.add(timezones);
		data.add(modification_dates);
		
		int start = 0;
        String line = null;
        do {
    		int i = 0;
	    	for(; i < chunkSize && (line = br.readLine()) != null; i++) {
    			Iterator<String> tokens = splitter.split(line).iterator();
				geonameids[i] = Integer.parseInt(tokens.next());
				names[i] = tokens.next();
				asciinames[i] = tokens.next();
				alternatenames[i] = tokens.next();
				String lat = tokens.next();
				latitudes[i] = lat.isEmpty() ? -1 : Double.parseDouble(lat);
				String longitude = tokens.next();
				longitudes[i] = longitude.isEmpty() ? -1 : Double.parseDouble(longitude);
				feature_classes[i] = tokens.next();
				feature_codes[i] = tokens.next();
				country_codes[i] = tokens.next();
				cc2s[i] = tokens.next();
				admin1_codes[i] = tokens.next();
				admin2_codes[i] = tokens.next();
				admin3_codes[i] = tokens.next();
				admin4_codes[i] = tokens.next();
				String population = tokens.next();
				populations[i] = population.isEmpty() ? 0 : Long.parseLong(population);
				String elevation = tokens.next();
				elevations[i] = elevation.isEmpty() ? 0 : Integer.parseInt(elevation);
				String dem = tokens.next();
				dems[i] = dem.isEmpty() ? 0 : Integer.parseInt(dem); 
				timezones[i] = tokens.next();
				modification_dates[i] = tokens.next();
	    	}

            // Retrieves datatype and dataspace information from h5file and sets the dataset in memory.
           if(line != null) {
        	   System.out.println("writing " + start);
   	    	dataset.init();
       		long[] starts = dataset.getStartDims();
       		long[] selecteds = dataset.getSelectedDims();
       		starts[0] = start;
       		selecteds[0] = i;

       		dataset.write(data);
       		start += i;
           }
    		
		} while(line != null);
		
        br.close();
		h5file.close();
	}
	
}
