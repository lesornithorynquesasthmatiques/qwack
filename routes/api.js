'use strict';

var db = require("../db/mongo");

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
