'use strict';


// Declare app level module which depends on filters, and services
angular.module('qwack', []).config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider.when('/love-songs', {templateUrl: 'partials/songs', controller: LoveSongsCtrl});
//  $routeProvider.otherwise({redirectTo: '/love-songs'});
  $locationProvider.html5Mode(true);
}]);