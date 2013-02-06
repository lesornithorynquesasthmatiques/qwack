function RegisterCtrl($scope, $location, User) {
	
	$scope.register = function() {
		if($scope.arePasswordsDifferent()){
			$scope.message = "Password are not identical";
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
					$scope.message = "Registration failed";
					console.log("Registration failed for user :" + JSON.stringify(user));
			});
		}
	};
	
	$scope.arePasswordsDifferent = function() {
		return $scope.password !== $scope.passwordConfirmation
	};
	
	
}
RegisterCtrl.$inject = ['$scope', '$location', 'User'];