'use strict';

var db = require("../db/mongo"),
	rest = require ("restler");

function replaceLuceneSpecialChars(input){
	var luceneChars = new RegExp('+-!(){}[]^"~*?:\&|'.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1"), "g");
	return input.replace(luceneChars, "\\$1");
}

function addRequiredFlagToSearchTerms(input){
	return input.replace(/(\W|^)(\b)/g, "$1+$2");
}

var solrUrl = 'http://localhost:8983/solr';

if (process.env.MODE == 'PRODUCTION'){
	solrUrl = 'http://4ever-db2.aws.xebiatechevent.info:8983/solr';
}

	
exports.solrSearch = function(req, res) {
	var offset = req.query.offset || 0;
	var pageSize = req.query.pageSize || 10;
	var url = solrUrl + '/songs/select?q=' + 
  	encodeURIComponent(addRequiredFlagToSearchTerms(replaceLuceneSpecialChars(req.query["q"]))) +
  	'&start=' + encodeURIComponent(offset) +
  	'&rows=' + encodeURIComponent(pageSize);
	rest.get(url)
	.on('complete', function(result) {
		if (result instanceof Error) {
			console.log(result);
		} else {
			res.json(result.response);
		}
	});
};


exports.solrSuggest = function(req, res) {
	var url = solrUrl + '/suggestions/ac?q=' + 
	encodeURIComponent(replaceLuceneSpecialChars(req.query["q"]));
	rest.get(url)
	.on('complete', function(result) {
		if (result instanceof Error) {
			console.log(result);
		} else {
			res.json(result.response);
		}
	});
};

exports.mongoSearch = function(req, res) {
	var query = {};
	var offset = req.query.offset || 0;
	var pageSize = req.query.pageSize || 10;
	if(req.query['title'] != undefined){
		query.title = new RegExp('.*' + req.query['title'], 'i');
	}
	if(req.query['album'] != undefined){
		query.release = new RegExp('.*' + req.query['album'], 'i');
	}
	if(req.query['artist'] != undefined){
		query["artist.name"] = new RegExp('.*' + req.query['artist'], 'i');
	}
	if(req.query['location'] != undefined){
		query['artist.location.name'] = new RegExp('.*' + req.query['location'], 'i');
	}
	if(req.query['keyword'] != undefined){
		query['artist.mbtags'] = new RegExp('.*' + req.query['keyword'], 'i');
	}
	//TODO async
	db.Songs.count(query, function(err, count) {
		if (err) {
			console.log('arg');
		} else {
			db.Songs.find(query, 'songid title release artist.name artist.location year')
			.skip(offset).limit(pageSize)
			.execFind(function(err, songs) {
				if (err) {
					console.log('arg');
				} else {
					res.json({count: count, songs: songs});
				}
			});
		}
	});
};



exports.addFavArtistForUser = function(req, res) {
	  // id d'artiste
		// id user
	};

exports.removeFavArtistForUser = function(req, res) {
	// id d'artiste
	// id user
};

exports.listFavArtistsForUser = function(req, res) {
	// id d'artiste
	// id user
};


exports.solrArtistSearch = function(req, res) {
	var offset = req.query.offset || 0;
	var pageSize = req.query.pageSize || 10;
	var url = solrUrl + '/artists/select?q=' + 
	encodeURIComponent(addRequiredFlagToSearchTerms(replaceLuceneSpecialChars(req.query["q"]))) +
	'&start=' + encodeURIComponent(offset) +
	'&rows=' + encodeURIComponent(pageSize);
	console.log("solr url" + url);
	rest.get(url)
	.on('complete', function(result) {
		if (result instanceof Error) {
			console.log(result);
		} else {
			res.json(result.response);
		}
	});
};

exports.listArtists = function(req, res) {
	db.Artists.find(function(err, artists) {
	    if (err) {
	      console.log('arg');
	    }
	    res.json(artists);
	  });
};

exports.latestVotes = function(req, res) {
  res.json([{
    user: {
      id: "f57iugjkf6",
      name: "Laurène"
    },
    artist: {
      id: "sdfgh9876sdf",
      name: "Black Sabbath"
    },
    time: new Date()
  }, {
    user: {
      id: "f57iugjkf6",
      name: "Yann"
    },
    artist: {
      id: "sdfgh9876sdf",
      name: "René la Taupe"
    },
    time: new Date()
  }, {
    user: {
      id: "f57iugjkf6",
      name: "Séven"
    },
    artist: {
      id: "sdfgh9876sdf",
      name: "La Compagnie Créole"
    },
    time: new Date()
  }, {
    user: {
      id: "f57iugjkf6",
      name: "Clément"
    },
    artist: {
      id: "sdfgh9876sdf",
      name: "Édith Piaf"
    },
    time: new Date()
  }, {
    user: {
      id: "f57iugjkf6",
      name: "Loïc"
    },
    artist: {
      id: "sdfgh9876sdf",
      name: "Madonna"
    },
    time: new Date()
  }]);
};
