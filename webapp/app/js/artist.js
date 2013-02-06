'use strict';

function ArtistCtrl($scope, $routeParams, $http) {
    $scope.mbid = $routeParams.mbid;

    $http.get('/lastfm/artist/' + $scope.mbid).
        success(function(data, status, headers, config) {
            $scope.artist = data.artist;
        }).
        error(function(data, status, headers, config) {
            console.log("ERRORRRRR", data, status, headers, config);
        });
    
    $http.get('/lastfm/events/' + $scope.mbid).
        success(function(data, status, headers, config) {
          if (data.events.event && data.events.event.length > 0) {
            $scope.events = data.events.event;
          }
          else {
            $scope.events = [];
          }
      }).
      error(function(data, status, headers, config) {
          console.log("ERRORRRRR", data, status, headers, config);
      });
}
