var app = angular.module("favoriteRoomApp", []);
let hostFavoriteRoom = "http://localhost:8080/api/deleteFavoriteRoom";

app.controller('favoriteRoomCtrl', function ($scope, $http) {

    $scope.deleteFavoriteRoom = (favoriteRoomId) => {
        const confirmed = window.confirm("Bạn có chắc chắn muốn xóa không?");
        if (confirmed) {
            const url = `${hostFavoriteRoom}/${favoriteRoomId}`;
            $http.delete(url).then(resp => {
                console.log(resp.data);
                let img = document.getElementById('img');
                if (img.src.match('heart.png')) {
                    img.src = '/img/icons8-heart-50.png'
                }
                location.reload();
            }).catch(error => {
                console.log(error);
            })
        }
    }

    $scope.sortBy = 'create_date'; // Default sort by ID
    $scope.currentPage = 0;

    $scope.getFavoriteRoom = function () {
        var url = '/api/listFavoriteRoom?page=' + $scope.currentPage + '&size=3&sortBy=' + $scope.sortBy + '&direction=asc';
        $http.get(url)
            .then(function (response) {
                $scope.favoriteRooms = response.data.content; console.log(response.data);
                $scope.totalPages = response.data.totalPages;
            }).catch(error => {
                console.log(error);
            })
    };

    $scope.prevPage = function () {
        if ($scope.currentPage > 0) {
            $scope.currentPage--;
            $scope.getFavoriteRoom();
        }
    };

    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPages - 1) {
            $scope.currentPage++;
            $scope.getFavoriteRoom();
        }
    };
    $scope.getFavoriteRoom();
})