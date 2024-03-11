var app = angular.module("app", []);
app.controller("ctrl", function($scope, $http, $window) {

	$http.get("/rest/authorities").then(resp => {
		$scope.db = resp.data;
		console.log(resp.data);
	})
/*	$scope.index_of = function(accountId, role) {
		console.log(accountId, role);
		console.log("authority>> ", $scope.db.authorities)
		// Kiểm tra xem accountId và role có giá trị hợp lệ không
		if (!accountId || !role) {
			console.log("accountId hoặc role không hợp lệ");
			return -1;
		}

		// Kiểm tra cấu trúc dữ liệu của db.authorities và tìm vị trí phù hợp
		var index = $scope.db.authorities.findIndex(a => {
			if (!a.account || !a.role) { // Kiểm tra xem account hoặc role có là null không
				console.log("Có đối tượng authority với account hoặc role là null:", a);
				return false; // Trả về false nếu có đối tượng authority không hợp lệ
			}
			return a.account.accountId == accountId && a.role.id == role;
		});

		// Kiểm tra xem có tìm thấy đối tượng hợp lệ không
		if (index !== -1) {
			// Trả về vị trí của phần tử nếu tìm thấy
			console.log("Tìm thấy dữ liệu phù hợp tại index:", index);
			return index;
		} else {
			// Xử lý trường hợp không tìm thấy dữ liệu
			console.log("Không tìm thấy dữ liệu phù hợp trong mảng db.authorities");
			// Thực hiện xử lý phù hợp, chẳng hạn thông báo lỗi
			return -1; // hoặc trả về giá trị khác để biểu thị không tìm thấy
		}
	}*/


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
				console.log("Hello id1: ", id);
				if ($scope.db.authorities[index].account.accountId === accountName) {
					$window.swal({
						title: "Lỗi",
						text: "Không được xét quyền của chính mình!",
						type: "error",
					});
					$http.get("/rest/authorities").then(resp => {
						$scope.db = resp.data;
						console.log(resp.data);
					})
					return;
				}

				if ($scope.canUpdateRole(id)) {
					console.error("Cannot update your own role.");
					$window.swal({
						title: "Lỗi",
						text: "Không được xét quyền cho người có cùng quyền!",
						type: "error",
					});
					$http.get("/rest/authorities").then(resp => {
						$scope.db = resp.data;
						console.log(resp.data);
					})
					return;
				}
				$http.delete(`/rest/authorities/${id}`).then(resp => {
					console.log("Delete successful");
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
					
					$scope.db.authorities.push(resp.data);
				});
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