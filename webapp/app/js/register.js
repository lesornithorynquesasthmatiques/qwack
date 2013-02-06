function RegisterCtrl($scope, $location, User) {
	
	$scope.register = function() {
		if(arePasswordsDifferent()){
			
			console.log("password and password confirmation aren't identical")
		} else {
			user = new User({
				email: $scope.email,
				password: $scope.password,
				passwordVerify: $scope.passwordConfirmation,
				city: $scope.town
			});
			
			user.$save(
				function(){
					console.log("user is registred : " + JSON.stringify(user));
					$location.path('/news');
				},
				function(){
					console.log("Registration failed for user :" + JSON.stringify(user));
			});
		}
	};
	
	var arePasswordsDifferent = function() {
		return $scope.password !== $scope.passwordConfirmation
	};
	
	
}
RegisterCtrl.$inject = ['$scope', '$location', 'User'];