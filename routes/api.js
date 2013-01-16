/*
 * Serve JSON to our AngularJS client
 */

exports.name = function(req, res) {
  res.json({
    name: 'Bob'
  });
};

exports.kittensWhoseNameStartsWithFluff = function(db) {
  return function(req, res) {
    db.Kitten.find({ name: /^Fluff/ }, function(err, kittens) {
      if (err) {
        console.log('arg');
      }

      console.log(kittens);
      res.json(kittens);
    });
  };
};
