var should = require("should");

var myArray = [3, 5];

describe('my array', function() {
  it('should exist with length of 2', function() {
    should.exist(myArray);
    myArray.should.have.lengthOf(2);
  });
});