package org.lesornithorynquesasthmatiques.converter;

import org.bson.types.ObjectId;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.model.Artist;
import org.lesornithorynquesasthmatiques.model.Location;
import org.lesornithorynquesasthmatiques.model.Song;

/**
 * @author Alexandre Dutra
 *
 */
public class SongConverter {

	public Song convert(DataSubset analyzis, DataSubset metadata, DataSubset musicBrainz) {
		
		Song song = new Song();
		song.setId(new ObjectId());
		
		Artist artist = new Artist();
		song.setArtist(artist);
		Location location = new Location();
		artist.setLocation(location);
		
		song.setAudiomd5((String) analyzis.getValue(0, 1));
		song.setDuration((double) analyzis.getValue(0, 3));
		song.setTrackid((String) analyzis.getValue(0, 30));
		
		artist.setSdid((int) metadata.getValue(0, 1));
		artist.setId((String) metadata.getValue(0, 4));
		location.setName((String) metadata.getValue(0, 6));
		double latitude = (double) metadata.getValue(0, 5);
		double longitude = (double) metadata.getValue(0, 7);
		if(! Double.isNaN(latitude) && ! Double.isNaN(longitude)){
			location.setLatitude(latitude);
			location.setLongitude(longitude);
		}
		artist.setMbid((String) metadata.getValue(0, 8));
		artist.setName((String) metadata.getValue(0, 9));
		artist.setPlaymeid((int) metadata.getValue(0, 10));
		song.setRelease((String) metadata.getValue(0, 14));
		song.setReleasesdid((int) metadata.getValue(0, 15));
		song.setSongid((String) metadata.getValue(0, 17));
		song.setTitle((String) metadata.getValue(0, 18));
		song.setTracksdid((int) metadata.getValue(0, 19));
		
		song.setYear((int) musicBrainz.getValue(0, 1));
		
		return song;
	}

}
