'use strict';

/* Services */

angular.module('qwackServices', ['ngResource']).
    factory('Artist', function($resource){
  return $resource('artists/:artistId.json', {}, {
    query: {method:'GET', params:{artistId:'artists'}, isArray:true}
  });
}).
factory('socket', function ($rootScope) {
    var socket = io.connect();
    return {
      on: function (eventName, callback) {
        socket.on(eventName, function () {  
          var args = arguments;
          $rootScope.$apply(function () {
            callback.apply(socket, args);
          });
        });
      },
      emit: function (eventName, data, callback) {
        socket.emit(eventName, data, function () {
          var args = arguments;
          $rootScope.$apply(function () {
            if (callback) {
              callback.apply(socket, args);
            }
          });
        })
      }
    };
  });
