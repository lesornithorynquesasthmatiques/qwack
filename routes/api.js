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

exports.solrSearch = function(req, res) {
	var offset = req.query.offset || 0;
	var pageSize = req.query.pageSize || 10;
	//TODO url encode user input + escape Solr special chars
	var url = 'http://localhost:8983/solr/songs/select?' +
	'wt=json&defType=edismax&' +
	'qf=title+release+artistName+locationName+mbtags&lowercaseOperators=true&q=' + 
	encodeURIComponent(req.query["q"]) +
	'&start=' + encodeURIComponent(offset)
	'&rows=' + encodeURIComponent(pageSize);
	console.log(url);
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
