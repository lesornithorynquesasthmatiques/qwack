'use strict';

if (process.env.MODE == 'FRONT_DEV') {
	exports.Songs = {};
	return;
}

var mongoose = require('mongoose');

if (process.env.MODE == 'PRODUCTION') {
    mongoose.connect('mongodb://4ever-db.aws.xebiatechevent.info/main');   
} else {
    mongoose.connect('mongodb://localhost/main');  
}


var db = mongoose.connection;

db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function() {
    console.log('DB connection is open');
});

var songSchema = mongoose.Schema({

    /** Echo Nest track ID (String) */
    _id: String,
    
    /** ID from 7digital.com or -1 (int) */
    tracksdid: Number,
    
    /** ID from 7digital.com or -1 (int) */
    releasesdid: Number,
    
    /** song title (String) */
    title: String,
    
    /** album name (String) */
    release: String,
    
//    artist: Artist,
    
    /** song release year from MusicBrainz or 0 (int) */
    year: Number,
    
    /** audio hash code (String) */
    audiomd5: String,
    
    /** in seconds (double) */
    duration: Number
});

var artistSchema = mongoose.Schema({
	 _id: String,

	/** ID from musicbrainz.org (String) */
	mbid: String,

	/** ID from 7digital.com or -1 (Integer) */
	sdid: Number,
	
	/** ID from playme.com, or -1 (Integer) */
	playmeid: Number,

	/** artist name (String) */
	name: String,
	
	//private Location location;
	
	/** tags from musicbrainz.org (List<String>) */
	mbtags: [String],

	/** artist terms */
	terms: [String],
	
	/** similar artist IDs */
	similarArtists: [String]
});

var userSchema = mongoose.Schema({
    email: String,
    password: String,
    city: String,
    favArtists:[{ artistId: String, creationDate: Date }]
});

var Songs = mongoose.model('songs', songSchema);
var Users = mongoose.model('users', userSchema);
var Artists = mongoose.model('artists', artistSchema);

exports.Songs = Songs;
exports.Users = Users;
exports.Artists = Artists;
