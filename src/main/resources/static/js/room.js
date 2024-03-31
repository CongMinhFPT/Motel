
var app = angular.module("roomApp", []);
let hostAddRoom = "http://localhost:8080/api/addFavoriteRoom"
let hostMotelRoom = "http://localhost:8080/api/listMotelRoom"
let hostFavoriteRoom = "http://localhost:8080/api/motelRoom"

var username = $("#accountIdFavorite").val();

app.controller('roomCtrl', function ($scope, $http) {

    console.log(username);

    $scope.usernameId = username;

    $scope.addFavoriteRoom = (motelRoomId) => {
        const url = `${hostAddRoom}/${$scope.usernameId}/${motelRoomId}`;
        const item = '';
        $http.post(url, item).then(resp => {
            console.log(resp.data);
            Swal.fire({
                icon: 'success',
                title: 'Thành Công  !',
                text: 'Đã thêm vào trang yêu thích!',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1000
            }).then(() => {
                window.location.reload();
            });
        }).catch(error => {
            console.log(error);
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
