'use strict';

/* jasmine specs for controllers go here */
describe('Qwack controllers', function() {

  beforeEach(function(){
    this.addMatchers({
      toEqualData: function(expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });


  beforeEach(module('qwackServices'));


  describe('ArtistListCtrl', function(){
    var scope, ctrl, $httpBackend;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      $httpBackend = _$httpBackend_;
      $httpBackend.expectGET('artists/artists.json').
          respond([{name: 'Beyonce'}, {name: 'Motorhead'}]);

      scope = $rootScope.$new();
      ctrl = $controller(ArtistListCtrl, {$scope: scope});
    }));


    it('should create "artists" model with 2 artists fetched from xhr', function() {
      expect(scope.artists).toEqual([]);
      $httpBackend.flush();

      expect(scope.artists).toEqualData(
          [{name: 'Beyonce'}, {name: 'Motorhead'}]);
    });


    it('should set the default value of orderProp model', function() {
      expect(scope.orderProp).toBe('name');
    });
  });
});
