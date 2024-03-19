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

});

