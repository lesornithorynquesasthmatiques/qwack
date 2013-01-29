'use strict';

function AppCtrl($scope, $http) {
  $http({method: 'GET', url: '/api/name'}).
    success(function(data, status, headers, config) {
        $scope.name = data.name;
      }).
    error(function(data, status, headers, config) {
      console.log("ERRORRRRR", data, status, headers, config);
      $scope.name = 'Error';
    });
}

function LoveSongsCtrl($scope, $http) {
  $http({method: 'GET', url: '/api/love-songs'}).
    success(function(data, status, headers, config) {
        $scope.songs = data;
      }).
    error(function(data, status, headers, config) {
      console.log("ERRORRRRR", data, status, headers, config);
      $scope.songs = [];
    });
}
//LoveSongsCtrl.$inject = [];
