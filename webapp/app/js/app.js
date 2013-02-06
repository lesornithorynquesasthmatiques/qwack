'use strict';

/* App Module */
angular.module('qwackApp', ['qwackServices'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'partials/artist.html',
        controller: 'ArtistListCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  }]);
