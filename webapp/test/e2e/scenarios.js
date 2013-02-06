'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('qwack App', function() {

  it('should redirect index.html to index.html#/', function() {
    browser().navigateTo('../../app/index.html');
    expect(browser().location().url()).toBe('/');
  });


  describe('Artist list view', function() {

    beforeEach(function() {
      browser().navigateTo('../../app/index.html#/');
    });


    it('should display two artists', function() {
      expect(repeater('#artists li').count()).toBe(2);
    });
  });
});
