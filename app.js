var express = require('express'),
  routes = require('./routes'),
  api = require('./routes/api'),
  db = require('./db/mongo');

var app = module.exports = express();


app.configure(function() {
  // body parsing middleware supporting JSON, urlencoded, and multipart requests
  app.use(express.bodyParser());
  // allows to fake "PUT" and "DELTE" methods via a "method" attribute in POST requests
  app.use(express.methodOverride());
  // serves static resources
  app.use(express.static(__dirname + '/public'));
  
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
app.get('/api/name', api.name);
app.get('/mongo/kittensWhoseNameStartsWithFluff', api.kittensWhoseNameStartsWithFluff(db));

// redirect all others to the index (HTML5 history)
app.get('*', routes.index);


app.listen(3000, function() {
  console.log("Express server listening on port %d in %s mode", this.address().port, app.settings.env);
});
