'use strict';

var db = require("../db/mongo");


exports.register = function(req, res) {

    var email = req.body.email;
    var password = req.body.password;
    var passwordVerify = req.body.passwordVerify;
    var city = req.body.city;
    console.log("*****************************" + req.body.city);

    if (password == passwordVerify) {
        var user = new db.Users();
        user.email = email;
        user.password = password;
        user.city = city;
        user.save();
    } else {
        res.send(400);
    }

    console.log(user);

    res.json(user);
};