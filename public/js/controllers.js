'use strict';

function AppCtrl($scope, $http) {
  $http({method: 'GET', url: '/api/name'}).
    success(function(data, status, headers, config) {
        $scope.name = data.name;
      }).
    error(function(data, status, headers, config) {
      console.log("ERRORRRRR", data, status, headers, config);
      $scope.name = 'Error';
    });
}

function LoveSongsCtrl($scope, $http) {
  $http({method: 'GET', url: '/api/love-songs'}).
    success(function(data, status, headers, config) {
        $scope.songs = data;
      }).
    error(function(data, status, headers, config) {
      console.log("ERRORRRRR", data, status, headers, config);
      $scope.songs = [];
    });
}

function SolrSearchCtrl($scope, $http) {
	$scope.songs = [];
	$scope.search = function() {
		$http({
			method : 'GET',
			url : '/api/solr-search?q=' + $scope.q
		}).success(function(data, status, headers, config) {
			$scope.songs = data;
		}).error(function(data, status, headers, config) {
			console.log("ERRORRRRR", data, status, headers, config);
			$scope.songs = [];
		});
	};
}
	
function MongoSearchCtrl($scope, $http) {
	$scope.songs = [];
	$scope.search = function() {
		var url = '/api/mongo-search?';
		//TODO URL encode
		if($scope.title) url += '&title=' + $scope.title;
		if($scope.album) url += '&album=' + $scope.album;
		if($scope.artist) url += '&artist=' + $scope.artist;
		if($scope.location) url += '&location=' + $scope.location;
		if($scope.keyword) url += '&keyword=' + $scope.keyword;
		$http({
			method : 'GET',
			url : url
		}).success(function(data, status, headers, config) {
			$scope.songs = data;
		}).error(function(data, status, headers, config) {
			console.log("ERRORRRRR", data, status, headers, config);
			$scope.songs = [];
		});
	};
}

// LoveSongsCtrl.$inject = [];
