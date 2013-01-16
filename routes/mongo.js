var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/test');
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function callback() {
    // yay!
});

var kittySchema = mongoose.Schema({
    name : String
});

kittySchema.methods.speak = function() {
    var greeting = this.name ? "Meow name is " + this.name : "I don't have a name";
    console.log(greeting);
};

var Kitten = mongoose.model('Kitten', kittySchema);

var fluffy = new Kitten({
    name : 'Fluffy'
});
fluffy.speak(); // "Meow name is Fluffy"

fluffy.save(function(err, fluffy) {
    if (err)
        console.log('arg');
    fluffy.speak();
});

exports.kittensWhoseNameStartsWithFluff = function(req, res) {
    // TODO : voir s'il y a un système de promises/futures
    // TODO : sortir la conf de la base Mongo dans app.js
    var result = [];
    Kitten.find({ name: /^Fluff/ }, function(err, kittens) {
        if (err)
            console.log('arg');
        result = kittens;
        console.log(kittens);
    });
    console.log('to wait or not to wait, that is the question');
    res.json(result);
};

