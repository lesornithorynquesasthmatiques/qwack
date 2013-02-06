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
				city: {
					name: $scope.city,
					lat: $scope.lat,
					lon: $scope.lon
				}
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
	
	$("#city").change(function (e) {
        $scope.$apply(function() {
            $scope.city = e.target.value;
        });
	});
	$("#lat").change(function (e) {
        $scope.$apply(function() {
            $scope.lat = e.target.value;
        });
	});
	$("#lon").change(function (e) {
        $scope.$apply(function() {
            $scope.lon = e.target.value;
        });
	});

	
}
RegisterCtrl.$inject = ['$scope', '$location', 'User'];
