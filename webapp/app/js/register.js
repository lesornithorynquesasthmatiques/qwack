function RegisterCtrl($scope) {
	
	$scope.register = function() {
		console.log("register user " + $scope.email)
	};
	
}
RegisterCtrl.$inject = ['$scope'];