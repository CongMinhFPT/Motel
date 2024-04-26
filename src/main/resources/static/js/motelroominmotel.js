var app = angular.module("myAppmotelroominmotel", []);

app.controller("myCtrlmotelroominmotel", function($scope, $http, $rootScope, $sce) {
  $scope.host = "http://localhost:8080";
  $scope.indexcuon =677.5999755859375;
  $scope.items = [];
  $scope.addresss ='';
$scope.itemsPerPage = 9;
$scope.currentPage = 1;

$scope.pageCount = function() {
    return Math.ceil($scope.items.length / $scope.itemsPerPage);
};

$scope.pages = function() {
    var rangeSize = 4;
    var pages = [];
    var start;

    start = $scope.currentPage;
    if ($scope.pageCount() <= rangeSize) {
        start = 1;
    } 
    else {
        start = $scope.currentPage;
        if (start > $scope.pageCount() - rangeSize) {
            start = $scope.pageCount() - rangeSize + 1;
        }
    }
    for (var i = start; i < start + rangeSize && i <= $scope.pageCount(); i++) {
        pages.push(i);
    }
    return pages;
};

$scope.prevPage = function() {
    if ($scope.currentPage > 1) {
        $scope.currentPage--;
        window.scrollTo(0, 677.5999755859375);
    }
};

$scope.nextPage = function() {
    if ($scope.currentPage < $scope.pageCount()) {
        $scope.currentPage++;
        window.scrollTo(0, 677.5999755859375);
    }
};

$scope.setPage = function(n) {
    $scope.currentPage = n;
    window.scrollTo(0, 677.5999755859375);
};
$scope.firstPage = function() {
    $scope.currentPage = 1;
    window.scrollTo(0, 677.5999755859375);
};

$scope.lastPage = function() {
    $scope.currentPage = $scope.pageCount();
    window.scrollTo(0, 677.5999755859375);
};

$scope.DataGetMotel = function(motelid , address){
  $scope.addresss = address;
  let url = $scope.host+'/api/motel/motelroom/data/'+motelid;
  $http.get(url, { 
  }).then(function(response){
   console.log(response.data)
   $scope.items = response.data.listmotel;
  }).catch(function(error) {
    console.log(error)
  });
}

});