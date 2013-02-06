'use strict';

function MyPageCtrl($scope, $http) {

 	$scope.searchArtist = function(){

 		if (this.searchKey.length>3){
              $http.get('/api/artists/find?q='+ this.searchKey).
			      success(function(data, status, headers, config) {
			          $scope.searchResult = data;
			        }).
			      error(function(data, status, headers, config) {
			        console.log("ERRORRRRR", data, status, headers, config);
			      });
        }
 	}
}
