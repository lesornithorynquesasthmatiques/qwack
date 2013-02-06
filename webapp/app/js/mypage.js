'use strict';

function MyPageCtrl($scope, $http) {

 	$scope.searchArtist = function(){

 		if (this.searchKey.length>3){
              $http.get('/api/artists/find?q='+ this.searchKey).
			      success(function(data, status, headers, config) {
			          $scope.searchResult = data;

			          data.docs.forEach(function (artist){

			         $http.get('/lastfm/artist/' + artist.mbid).
					        success(function(data, status, headers, config) {
					        	artist.lastfm = data.artist;
					      }).
					      error(function(data, status, headers, config) {
					          console.log("ERRORRRRR", data, status, headers, config);
					      });


			          })

			        }).
			      error(function(data, status, headers, config) {
			        console.log("ERRORRRRR", data, status, headers, config);
			      });
        }
 	}

    $scope.openArtistPage = function(mbid) {
    	$location.path('/artist/' + mbid);
  	};

}
