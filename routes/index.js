'use strict';

exports.index = function(req, res) {
  res.sendfile('webapp/app/index.html');
};

exports.partials = function(req, res) {
  var name = req.params.name;
  res.sendfile('webapp/app/partials/' + name);
};
