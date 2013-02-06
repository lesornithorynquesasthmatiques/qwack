
angular.module('lastFmServices', ['ngResource']).
    factory('LastFm', function($resource){
        return $resource('lastfm/artist/:mbid', {}, {}
        });
    });