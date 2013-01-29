package org.lesornithorynquesasthmatiques.converter;

import org.bson.types.ObjectId;
import org.lesornithorynquesasthmatiques.hdf.DataSubset;
import org.lesornithorynquesasthmatiques.lang.Numbers;
import org.lesornithorynquesasthmatiques.model.Artist;
import org.lesornithorynquesasthmatiques.model.Location;
import org.lesornithorynquesasthmatiques.model.Song;

import com.google.common.base.Strings;

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
		
		song.setAudiomd5(Strings.emptyToNull(Strings.emptyToNull((String) analyzis.getValue(0, 1))));
		song.setDuration(Numbers.infiniteOrNanToNull((double) analyzis.getValue(0, 3)));
		song.setTrackid(Strings.emptyToNull((String) analyzis.getValue(0, 30)));
		
		artist.setSdid(Numbers.negativeOrZeroToNull((int) metadata.getValue(0, 1)));
		artist.setId(Strings.emptyToNull((String) metadata.getValue(0, 4)));
		String locationName = Strings.emptyToNull((String) metadata.getValue(0, 6));
		Double latitude = Numbers.infiniteOrNanToNull((double) metadata.getValue(0, 5));
		Double longitude = Numbers.infiniteOrNanToNull((double) metadata.getValue(0, 7));
		if(locationName != null || latitude != null || longitude != null) {
			Location location = new Location();
			location.setName(locationName);
			if(latitude != null || longitude != null){
				location.setCoords(latitude, longitude);
			}
			artist.setLocation(location);
		}
		artist.setMbid(Strings.emptyToNull((String) metadata.getValue(0, 8)));
		artist.setName(Strings.emptyToNull((String) metadata.getValue(0, 9)));
		artist.setPlaymeid(Numbers.negativeOrZeroToNull((int) metadata.getValue(0, 10)));
		song.setRelease(Strings.emptyToNull((String) metadata.getValue(0, 14)));
		song.setReleasesdid(Numbers.negativeOrZeroToNull((int) metadata.getValue(0, 15)));
		song.setSongid(Strings.emptyToNull((String) metadata.getValue(0, 17)));
		song.setTitle(Strings.emptyToNull((String) metadata.getValue(0, 18)));
		song.setTracksdid(Numbers.negativeOrZeroToNull((int) metadata.getValue(0, 19)));
		
		song.setYear(Numbers.negativeOrZeroToNull((int) musicBrainz.getValue(0, 1)));
		
		return song;
	}

}
