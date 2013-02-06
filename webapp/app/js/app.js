'use strict';

/* App Module */
angular.module('qwackApp', ['qwackServices'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/login', {
        templateUrl: 'partials/login.html',
        controller: 'LoginCtrl'
      })
      .when('/artists', {
        templateUrl: 'partials/artist.html',
        controller: 'ArtistListCtrl'
      })
      .when('/register', {
        templateUrl: 'partials/register.html',
        controller: 'RegisterCtrl'
      })
      .otherwise({
        redirectTo: '/login'
      });
  }]);
