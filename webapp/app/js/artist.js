'use strict';

function ArtistCtrl($scope, $routeParams, $http) {
  console.log($routeParams);
    $scope.mbid = $routeParams.mbid;

    $http.get('/lastfm/artist/' + $scope.mbid).
        success(function(data, status, headers, config) {
            $scope.lastFmInfo = data;
        }).
        error(function(data, status, headers, config) {
            console.log("ERRORRRRR", data, status, headers, config);
        });
}
