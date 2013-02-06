'use strict';

/* Controllers */

function ArtistListCtrl($scope, Artist) {
  $scope.artists = Artist.query();
  $scope.orderProp = 'name';
}

//PhoneListCtrl.$inject = ['$scope', 'Phone'];
