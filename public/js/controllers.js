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
	$scope.response = null;
	$scope.offset = 0;
	$scope.pageSize = 10;
	$scope.next = function() {
		$scope.offset += 10;
		$scope.search();
	}
	$scope.previous = function() {
		$scope.offset -= 10;
		$scope.search();
	}
	$scope.search = function() {
		var params = [];
		params.push("offset=" + encodeURIComponent($scope.offset));
		params.push("pageSize=" + encodeURIComponent($scope.pageSize));
		params.push("q=" + encodeURIComponent($scope.q));
		var url = '/api/solr-search?' + params.join('&');
		$http({
			method : 'GET',
			url : url
		}).success(function(data, status, headers, config) {
			$scope.response = data;
		}).error(function(data, status, headers, config) {
			console.log("ERRORRRRR", data, status, headers, config);
			$scope.response = null;
		});
	};
}
	
function MongoSearchCtrl($scope, $http) {
	$scope.response = null;
	$scope.offset = 0;
	$scope.pageSize = 10;
	$scope.next = function() {
		$scope.offset += 10;
		$scope.search();
	}
	$scope.previous = function() {
		$scope.offset -= 10;
		$scope.search();
	}
	$scope.search = function() {
		var params = [];
		params.push("offset=" + $scope.offset);
		params.push("pageSize=" + $scope.pageSize);
		if($scope.title) params.push('title=' + encodeURIComponent($scope.title));
		if($scope.album) params.push('album=' + encodeURIComponent($scope.album));
		if($scope.artist) params.push('artist=' + encodeURIComponent($scope.artist));
		if($scope.location) params.push('location=' + encodeURIComponent($scope.location));
		if($scope.keyword) params.push('keyword=' + encodeURIComponent($scope.keyword));
		var url = '/api/mongo-search?' + params.join('&');
		$http({
			method : 'GET',
			url : url
		}).success(function(data, status, headers, config) {
			$scope.response = data;
		}).error(function(data, status, headers, config) {
			console.log("ERRORRRRR", data, status, headers, config);
			$scope.response = null;
		});
	};
}

// LoveSongsCtrl.$inject = [];
