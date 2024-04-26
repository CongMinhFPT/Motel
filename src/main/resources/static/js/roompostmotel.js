var app = angular.module("myApproompostmotel", []);

app.controller("myCtrlroompostmotel", function($scope, $http, $rootScope, $sce) {
  $scope.host = "http://localhost:8080";
  $scope.items =[];
  $scope.item =[];
//   $scope.items =[];
// for (var i = 1; i <= 20; i++) {
//     $scope.items.push(i);
// }
$scope.sesearchnamemotel ='';
$scope.itemsPerPage = 8;
$scope.currentPage = 1;
$scope.$watch('sesearchnamemotel', function(newVal, oldVal) {
    $scope.items = $scope.item.filter(function(item) {
        if (!newVal) {
            return true; 
        }
        return item.motel.descriptions.toLowerCase().indexOf(newVal.toLowerCase()) >= 0;
    });
    console.log($scope.items)
});
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
        window.scrollTo(0, 0);
    }
};

$scope.nextPage = function() {
    if ($scope.currentPage < $scope.pageCount()) {
        $scope.currentPage++;
        window.scrollTo(0,0);
    }
};

$scope.setPage = function(n) {
    $scope.currentPage = n;
    window.scrollTo(0,0);
};
$scope.firstPage = function() {
    $scope.currentPage = 1;
    window.scrollTo(0,0);
};

$scope.lastPage = function() {
    $scope.currentPage = $scope.pageCount();
    window.scrollTo(0, 0);
};

  $scope.GetPostMotelnew =function (){
    let url =$scope.host+'/api/data/postmotel';
    $http.get(url).then(
     function (response) {
      $scope.items =response.data;
      $scope.item =response.data;
      console.log($scope.items);
      $scope.checkloading =true;
     },
     function (error) {
       console.log(error);
     }
   );
}
});