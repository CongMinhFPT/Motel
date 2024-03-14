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
				var email = $scope.db.authorities[index].account.email;
				console.log("email : ", email);
				if (email === accountName) {
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

				if ($scope.canUpdateRole(accountId)) {
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

				$window.Swal.fire({
					title: "Xác Nhận!",
					text: "Bạn có muốn xóa vai trò này không?",
					icon: "question",
					showCancelButton: true,
					confirmButtonColor: "#3085d6",
					cancelButtonColor: "#d33",
					confirmButtonText: "Xóa",
					cancelButtonText: "Hủy"
				}).then((result) => {
					if (result.isConfirmed) {
						// Thực hiện xóa
						$http.delete(`/rest/authorities/${id}`).then(resp => {
							console.log("Delete successful");
							// Hiển thị thông báo xóa thành công
							$window.Swal.fire({
								icon: "success",
								title: "Thành công!",
								text: "Xóa vai trò thành công!",
								showConfirmButton: false,
								timer: 1500
							});
							$scope.db.authorities.splice(index, 1); // Xóa trong danh sách client
						});
					} else {
						$http.get("/rest/authorities").then(resp => {
							$scope.db = resp.data;
							console.log(resp.data);
						})
						$window.Swal.fire({
							icon: "info",
							title: "Thông báo",
							text: "Hủy cập nhật quyền!",
							showConfirmButton: false,
							timer: 1500
						});
					}
				});
			}
			else {
				// Xác nhận trước khi cập nhật
				$window.Swal.fire({
					title: "Xác Nhận!",
					text: "Bạn có muốn cập nhật quyền cho tài khoản này?",
					icon: "question",
					showCancelButton: true,
					confirmButtonColor: "#3085d6",
					cancelButtonColor: "#d33",
					confirmButtonText: "Cập Nhật",
					cancelButtonText: "Hủy"
				}).then((result) => {
					if (result.isConfirmed) {
						// Thực hiện cập nhật
						var accountRole = {
							account: { accountId: accountId },
							role: { id: role }
						};
						$http.post('/rest/authorities/', accountRole).then(resp => {
							console.log("Update successful");
							// Hiển thị thông báo cập nhật thành công
							$window.Swal.fire({
								icon: "success",
								title: "Thành công!",
								text: "Cập nhật quyền thành công!",
								showConfirmButton: false,
								timer: 1500
							});
							$scope.db.authorities.push(resp.data);
						});
					} else {
						$http.get("/rest/authorities").then(resp => {
							$scope.db = resp.data;
							console.log(resp.data);
						})
						$window.Swal.fire({
							icon: "info",
							title: "Thông báo",
							text: "Hủy cập nhật quyền!",
							showConfirmButton: false,
							timer: 1500
						});
					}
				});
			}
		}
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



	}).catch(function(error) {
		console.error("Error getting user information:", error);
	});

	$scope.canUpdateRole = function(accountId) {
		var isAdmin = $scope.isAdmin(accountId);
		console.log("Calling isAdmin with:", accountId);
		console.log("isAdmin for admin user:", $scope.isAdmin(accountId));

		return isAdmin;
	}

	$scope.isAdmin = function(accountId) {
		var adminIndex = $scope.index_of(accountId, 'SUPPERMANAGER');
		console.log("isAdmin: ", adminIndex >= 0);
		return adminIndex >= 0;
	};
})