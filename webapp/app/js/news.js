'use strict';

function NewsCtrl($scope, $http) {
  $scope.latestVotes = [];
  $http.get('/api/latest-votes', $scope.user).
    success(function(data, status, headers, config) {
        $scope.latestVotes = data;
      }).
    error(function(data, status, headers, config) {
      console.log("ERRORRRRR", data, status, headers, config);
    });
}