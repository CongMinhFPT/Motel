var app = angular.module("roomApp", []);
let hostAddRoom = "http://localhost:8080/api/addFavoriteRoom"
let hostMotelRoom = "http://localhost:8080/api/listMotelRoom"
let hostFavoriteRoom = "http://localhost:8080/api/motelRoom"

var username = $("#accountIdFavorite").val();

app.controller('roomCtrl', function ($scope, $http) {

    console.log(username);

    $scope.usernameId = username;

    $scope.isFavorite = false;
    $scope.addFavoriteRoom = (motelRoomId) => {
        const url = `${hostAddRoom}/${$scope.usernameId}/${motelRoomId}`;
        const item = '';
        $http.post(url, item).then(resp => {
            console.log(resp.data);
            Swal.fire({
                icon: 'success',
                title: 'Thành Công !',
                text: 'Đã thêm vào trang yêu thích!',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1000
            })
            const imgElement = document.getElementById('img' + motelRoomId);
            // Kiểm tra và thay đổi hình ảnh
            if (imgElement.src.includes('icons8-heart-50.png')) {
                imgElement.src = '/img/icons8-heart-filled-50.png';
            } else {
                imgElement.src = '/img/icons8-heart-50.png';
            }
        }).catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất Bại !',
                text: 'Vui lòng đăng nhập!',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1000
            })
        })
    }

    $scope.getMotelRoom = () => {
        const url = `${hostMotelRoom}`;
        $http.get(url).then(resp => {
            console.log(resp.data);
            $scope.motelRooms = resp.data;
        }).catch(error => {
            console.log(error);
        })
    }
    $scope.getMotelRoom();

})