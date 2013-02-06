'use strict';

var express = require('express'),
  routes = require('./routes'),
  api = require('./routes/api'),
  db = require('./db/mongo'),
  restler = require('restler');

var app = module.exports = express();


app.configure(function() {
  // body parsing middleware supporting JSON, urlencoded, and multipart requests
  app.use(express.bodyParser());
  // allows to fake "PUT" and "DELTE" methods via a "method" attribute in POST requests
  app.use(express.methodOverride());
  // serves static resources
  app.use(express.static(__dirname + '/webapp'));
  
  app.use(app.router);
});

app.configure('development', function() {
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});

app.configure('production', function() {
  app.use(express.errorHandler());
});


// routes
app.get('/', routes.index);
app.get('/partials/:name', routes.partials);
app.get('/api/name', api.name);
app.get('/api/love-songs', api.loveSongs);
app.get('/api/solr-search', api.solrSearch);
app.get('/api/solr-suggest', api.solrSuggest);
app.get('/api/mongo-search', api.mongoSearch);

// redirect all others to the index (HTML5 history)
//app.get('*', routes.index);

var port = process.env.PORT || 3000;
app.listen(port, function() {
  console.log("Express server listening on port %d in %s mode", this.address().port, app.settings.env);
});
