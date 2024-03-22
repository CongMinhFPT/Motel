var app = angular.module('app', []);

app.controller('roomController', function($scope, $http) {
	$http.get('/room').then(function(resp) {
			$scope.db = resp.data;
            console.log($scope.db);
		})
		.catch(function(error) {
			console.error('Error fetching authorities:', error);
		});
});