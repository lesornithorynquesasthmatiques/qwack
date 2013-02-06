'use strict';

function ArtistListCtrl($scope, Artist) {
  $scope.artists = Artist.query();
  $scope.orderProp = 'name';
}
