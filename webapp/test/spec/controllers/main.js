'use strict';

describe('Controller: MainCtrl', function() {

  // load the controller's module
  beforeEach(module('webApp'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function($controller) {
    scope = {};
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('should attach a team name to the scope', function() {
    expect(scope.teamName).toBe('Qwack');
  });
});
