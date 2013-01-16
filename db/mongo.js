var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/test');

var db = mongoose.connection;

db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function() {
    console.log('DB connection is open');
});

var kittySchema = mongoose.Schema({
    name: String
});

kittySchema.methods.speak = function() {
    console.log(this.name + " is born!");
};

var Kitten = mongoose.model('Kitten', kittySchema);

Kitten.remove();

Kitten.find({ name: /^Fluff/ }, function(err, kittens) {
    if (err) {
        console.log('arg');
        return;
    }

    console.log(kittens.length);
    if (kittens && kittens.length > 0) {
        return;
    }

    var fluffy = new Kitten({
        name: 'Fluffy'
    });

    fluffy.save(function(err, fluffy) {
        if (err) {
            console.log('arg');
        }
        fluffy.speak();
    });
});

exports.Kitten = Kitten;