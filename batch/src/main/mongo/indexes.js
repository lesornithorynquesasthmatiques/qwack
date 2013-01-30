var db = connect("localhost:27017/main");
db.songs.ensureIndex({"artist.location.coords": "2d"});
db.songs.ensureIndex({"songid":1},{unique:1});
db.songs.ensureIndex({"trackid":1},{unique:1});
db.songs.ensureIndex({"tracksdid":1});//not  unique
db.songs.ensureIndex({"releasesdid":1});
db.songs.ensureIndex({"title":1});
db.songs.ensureIndex({"release":1});
db.songs.ensureIndex({"year":1});
db.songs.ensureIndex({"artist.id":1});
db.songs.ensureIndex({"artist.mbid":1});
db.songs.ensureIndex({"artist.sdid": 1});
db.songs.ensureIndex({"artist.playmeid": 1});
db.songs.ensureIndex({"artist.name": 1});
db.songs.ensureIndex({"artist.mbtags":1}, {sparse:1});
db.songs.ensureIndex({"artist.terms":1}, {sparse:1});
db.songs.ensureIndex({"artist.similarArtists":1}, {sparse:1});