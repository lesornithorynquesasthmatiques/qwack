'use strict';

var express = require('express'),
    routes = require('./routes'),
    api = require('./routes/api'),
    lastfm = require('./routes/lastfm'),
    db = require('./db/mongo'),
    restler = require('restler'),
    passport = require('passport'),
    LocalStrategy = require('passport-local').Strategy;

var app = module.exports = express();




app.configure(function () {
    // body parsing middleware supporting JSON, urlencoded, and multipart requests
    app.use(express.bodyParser());
    // allows to fake "PUT" and "DELTE" methods via a "method" attribute in POST requests
    app.use(express.methodOverride());
    // serves static resources
    app.use(express.static(__dirname + '/webapp/app'));
    app.use(passport.initialize());
    app.use(passport.session());
    app.use(app.router);
});

app.configure('development', function () {
    app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});

app.configure('production', function () {
    app.use(express.errorHandler());
});


passport.use(new LocalStrategy({
        usernameField: 'email',
        passwordField: 'passwd'
    },
    function (username, password, done) {
        db.Users.findOne({ email: username }, function (err, user) {
            if (err) {
                return done(err);
            }
            if (!user) {
                return done(null, false, { message: 'Incorrect username.' });
            }
            if (password != user.password) {
                return done(null, false, { message: 'Incorrect password.' });
            }
            return done(null, user);
        });
    }
));

passport.serializeUser(function (user, done) {
    done(null, user._id);
});


passport.deserializeUser(function (id, done) {
    db.Users.findById(id, function (err, user) {
        done(err, user);
    });
});

// auth POST
app.post('/login',
    passport.authenticate('local', {
        successRedirect: '/',
        failureRedirect: '/login'
    })
);

// routes
app.get('/', routes.index);
app.get('/api/latest-votes', api.latestVotes);
app.get('/api/mongo-search', api.mongoSearch);
app.get('/api/solr-search', api.solrSearch);
app.get('/api/solr-suggest', api.solrSuggest);
app.get('/partials/:name', routes.partials);

app.get('/lastfm/events/:mbid', lastfm.getArtistEvents);
app.get('/lastfm/artist/:mbid', lastfm.getArtistBio);

app.get('/api/artists/', api.listArtists);
app.get('/api/artists/find', api.solrArtistSearch);


// Artistes favoris d'un user
app.get('/api/user/:userId/starred/', api.listFavArtistsForUser);
app.post('/api/user/:userId/artist/:artistId', api.addFavArtistForUser);
app.delete('/api/user/:userId/artist/:artistId', api.removeFavArtistForUser);

// redirect all others to the index (HTML5 history)
//app.get('*', routes.index);

var port = process.env.PORT || 3000;
app.listen(port, function () {
    console.log("Express server listening on port %d in %s mode", this.address().port, app.settings.env);
});
