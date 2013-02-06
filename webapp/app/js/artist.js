'use strict';

function ArtistCtrl($scope, Artist) {

    $scope.mbid = 'bfcc6d75-a6a5-4bc6-8282-47aec8531818';

    $http.get('/lastfm/artist/'+ $scope.mbid).
        success(function(data, status, headers, config) {
            $scope.lastFmInfo = data;
        }).
        error(function(data, status, headers, config) {
            console.log("ERRORRRRR", data, status, headers, config);
        });
}
