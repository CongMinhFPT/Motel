var app = angular.module("app", []);
app.controller("ctrl", function($scope, $http, $window) {

	$http.get("/rest/authorities").then(resp => {
		$scope.db = resp.data;
		console.log(resp.data);
	})

	$scope.index_of = function(accountId, role) {
		console.log(accountId, role);
		console.log($scope.db.authorities);

		return $scope.db.authorities
			.findIndex(a => a.account.accountId == accountId && a.role.id == role);
	}


	$http.get("/api/account").then(function(response) {
		var accountName = response.data.name;
		console.log("Username accountName>> ", accountName);

		$scope.update = function(accountId, role) {
			var index = $scope.index_of(accountId, role);
			console.log("Hello index: ", index);

			if (index >= 0) {
				console.log("Hello index 1: ", index);
				console.log("Authority_Index>> ", $scope.db.authorities[index]);
				console.log("Authority_Data>> ", $scope.db.authorities);

				var id = $scope.db.authorities[index].id;
				console.log("Hello id1 : ", id);
				if ($scope.db.authorities[index].account.email === accountName) {
					$window.Swal.fire({
						title: "Lỗi",
						text: "Không được xét quyền của chính mình!",
						icon: "error",
					});
					$http.get("/rest/authorities").then(resp => {
						$scope.db = resp.data;
						console.log(resp.data);
					})
					return;
				}

				if ($scope.canUpdateRole(id)) {
					console.error("Cannot update your own role.");
					$window.Swal.fire({
						title: "Lỗi",
						text: "Không được xét quyền cho người có cùng quyền!",
						icon: "error",
					});
					$http.get("/rest/authorities").then(resp => {
						$scope.db = resp.data;
						console.log(resp.data);
					})
					return;
				}
				$http.delete(`/rest/authorities/${id}`).then(resp => {
					console.log("Delete successful");
					$window.Swal.fire({
						icon: "success",
						title: "Thành công!",
						text: "Cập nhật quyền thành công!",
						showConfirmButton: false,
						timer: 1500
					});
					$scope.db.authorities.splice(index, 1); //xóa trong list phía cleint
				})
			} else {
				var accountRole = {
					account: { accountId: accountId },
					role: { id: role }
				};
				console.log("Hello index 2: ", index);
				console.log(accountRole);

				$http.post('/rest/authorities/', accountRole).then(resp => {
					console.log("Update successful");
					$window.Swal.fire({
						icon: "success",
						title: "Thành công!",
						text: "Cập nhật quyền thành công!",
						showConfirmButton: false,
						timer: 1500
					});
					$scope.db.authorities.push(resp.data);
				});
				/*$http.post('/rest/authorities/', JSON.stringify(accountRole), {
					headers: {
						'Content-Type': 'application/json' // Loại bỏ charset=UTF-8
					}
				}).then(resp => {
					console.log("Update successful");
					$scope.db.authorities.push(resp.data);
				}).catch(error => {
					console.error("Error:", error);
				});*/

			}
		}
	}).catch(function(error) {
		console.error("Error getting user information:", error);
	});

	$scope.canUpdateRole = function(id) {
		var isAdmin = $scope.isAdmin(id);
		console.log("Calling isAdmin with:", id);
		console.log("isAdmin for admin user:", $scope.isAdmin(id));

		return isAdmin;
	}

	$scope.isAdmin = function(id) {
		var adminIndex = $scope.index_of(id, 'MANAGER');
		console.log("isAdmin: ", adminIndex >= 0);
		return adminIndex >= 0;
	};
})