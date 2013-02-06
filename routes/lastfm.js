'use strict';

var LastFmNode = require('lastfm').LastFmNode;
/*

login: qwack2
password: qwack
api_key: c46d90be348080de1f22870110958615

*/
var lastfm = new LastFmNode({
  api_key: 'c46d90be348080de1f22870110958615',    // sign-up for a key at http://www.last.fm/api
  secret: 'qwack',
  useragent: 'yawyl/v42 ' // optional. defaults to lastfm-node.
});


// Requete contient le mbid de l'artiste
exports.getArtistBio = function (req, res){

	var request = lastfm.request("artist.getInfo", {
    mbid: req.params.mbid,
    api_key: 'c46d90be348080de1f22870110958615',
    handlers: {
        success: function(data) {

            console.log("Success: " + data);
            res.json(data);
        },
        error: function(error) {
        	res.json({message:"Unknown artist mbid : "+ req.params.mbid});
            console.log("Error: " + error.message);
        }
    }
});
};
