var app = angular.module("roomApp", []);
let hostAddRoom = "http://localhost:8080/api/addFavoriteRoom"
let hostMotelRoom = "http://localhost:8080/api/listMotelRoom"

var username = $("#accountIdFavorite").val();
// console.log(username);

app.controller('roomCtrl', function ($scope, $http) {
    console.log(username);

    $scope.addFavoriteRoom = (motelRoomId) => {
        const url = `${hostAddRoom}/${$scope.usernameId}/${motelRoomId}`;
        const item = '';
        $http.post(url, item).then(resp => {
            console.log(resp.data);
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