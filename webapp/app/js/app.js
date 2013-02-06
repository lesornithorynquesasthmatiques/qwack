'use strict';

angular.module('qwackApp', ['qwackServices'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/login', {
        templateUrl: 'partials/login.html',
        controller: LoginCtrl
      })
      .when('/news', {
        templateUrl: 'partials/news.html',
        controller: NewsCtrl
      })
      .when('/mypage', {
        templateUrl: 'partials/mypage.html',
        controller: MyPageCtrl
      })
      .when('/artist/:mbid', {
        templateUrl: 'partials/artist.html',
        controller: ArtistCtrl
      })
      .when('/register', {
        templateUrl: 'partials/register.html',
        controller: RegisterCtrl
      })
      .otherwise({
        redirectTo: '/login'
      });
  }]);
