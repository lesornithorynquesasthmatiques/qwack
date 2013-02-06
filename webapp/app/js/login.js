'use strict';

function AuthenticationCtrl($scope, $location) {
  var loggedIn = false;
  $location.path(loggedIn ? '/news' : '/login');
}

function LoginCtrl($scope, $http, $location) {
  $scope.user = {
      email: "Saisissez votre email"
  };
  $scope.login = function() {
    $http.post('/login', $scope.user).
      success(function(data, status, headers, config) {
          $location.path('/news');
        }).
      error(function(data, status, headers, config) {
        console.log("ERRORRRRR", data, status, headers, config);
      });
  };
}
