'use strict';

var db = require("../db/mongo"),
	rest = require ("restler");

exports.name = function(req, res) {
  res.json({
    name: 'Bob'
  });
};

exports.loveSongs = function(req, res) {
  db.Songs.find({ title: /.*love.*/i }, function(err, songs) {
    if (err) {
      console.log('arg');
    }
    res.json(songs);
  });
};

function replaceLuceneSpecialChars(input){
	var luceneChars = new RegExp('+-!(){}[]^"~*?:\&|'.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1"), "g");
	return input.replace(luceneChars, "\\$1");
}

function addRequiredFlagToSearchTerms(input){
	return input.replace(/(\W|^)(\b)/g, "$1+$2");
}

var solrUrl = 'http://localhost:8983/solr';
	
exports.solrSearch = function(req, res) {
	var offset = req.query.offset || 0;
	var pageSize = req.query.pageSize || 10;
	var url = solrUrl + '/songs/select?q=' + 
	encodeURIComponent(addRequiredFlagToSearchTerms(replaceLuceneSpecialChars(req.query["q"]))) +
	'&start=' + encodeURIComponent(offset)
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