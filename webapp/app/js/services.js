'use strict';

/* Services */

angular.module('qwackServices', ['ngResource']).
    factory('Artist', function($resource){
  return $resource('artists/:artistId.json', {}, {
    query: {method:'GET', params:{artistId:'artists'}, isArray:true}
  });
});
