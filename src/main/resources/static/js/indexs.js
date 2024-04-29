var app = angular.module('indexesApp', []);

app.controller('indexesCtrl', function ($scope, $http) {

    $scope.indexesId = $('#indexesIdInput').val();

    $scope.getAllIndexs = function (indexesId) {
        let url = "http://localhost:8080/api/indexs-list/" + indexesId;
        $http.get(url)
            .then(function (response) {
                $scope.indexes = response.data;
                console.log(response.data);
            });
    };

    $scope.getAllIndexs($scope.indexesId);

    $scope.getAllIndexsByIndex = function (indexesId) {
        let url = "http://localhost:8080/api/indexs-list-index/" + indexesId;
        $http.get(url)
            .then(function (response) {
                $scope.indexss = response.data;
                console.log(response.data);
            });
    };

    $scope.getAllIndexsByIndex($scope.indexesId);

    $scope.getIndexsByYear = function () {
        let url = "http://localhost:8080/api/indexs-by-year";
        $http.get(url)
            .then(function (response) {
                $scope.indexYear = response.data;
                console.log(response.data);
            });
    };

    $scope.getIndexsByYear();

    $scope.getIndexsByYearAndMotelRoom = function (year, motelRoomId) {
        let url = "http://localhost:8080/api/indexs-list-motel-room/" + year + "/" + motelRoomId;
        $http.get(url)
            .then(function (response) {
                $scope.indexYearMotelRoom = response.data;
                console.log(response.data);
            });
    };

    $scope.updateIndexs = function (indexsId, createDate, electricityIndex, waterIndex, motelRoom) {
        let urlIndexs = "http://localhost:8080/api/update-indexs/" + indexsId;
        let data = {
            createDate: createDate,
            electricityIndex: electricityIndex,
            waterIndex: waterIndex,
            motelRoom: motelRoom
        };

        $http.put(urlIndexs, JSON.stringify(data), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(function (response) {
                console.log(response.data);
            })
            .catch(function (error) {
                console.error(error);
            });
    }

});

