var app = angular.module("myApproompostmotel", []);

app.controller("myCtrlroompostmotel", function($scope, $http, $rootScope, $sce, $timeout) {
  $scope.host = "http://localhost:8080";
   $scope.items =[];
  $scope.item =[];
  $scope.selectedProvinceName ='';
  $scope.selectedDistrictName ='';
  $scope.selectedWardName ='';
  $scope.sesearchnamemotel ='';
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
//   $scope.items =[];
// for (var i = 1; i <= 20; i++) {
//     $scope.items.push(i);
// }
$scope.sesearchnamemotel ='';
$scope.itemsPerPage = 8;
$scope.currentPage = 1;
$scope.$watch('sesearchnamemotel', function(newVal, oldVal) {
    $scope.items = $scope.item.filter(filterItems);
    $scope.currentPage = 1; // Đặt lại trang hiện tại sau khi lọc
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
$scope.getData = function() {
    let url = '../data/data.json';

    $http.get(url).then(function(res) {
       $scope.citis = res.data;
    }).catch(function(err) {
       console.log(err);
    });
 };
 var filterItems = function(item) {
    var isNameMatched = $scope.sesearchnamemotel ? item.motel.descriptions.toLowerCase().includes($scope.sesearchnamemotel.toLowerCase()) : true;
    var isProvinceMatched = $scope.selectedProvinceName ? item.motel.province === $scope.selectedProvinceName : true;
    var isDistrictMatched = $scope.selectedDistrictName ? item.motel.district === $scope.selectedDistrictName : true;
    var isWardMatched = $scope.selectedWardName ? item.motel.ward === $scope.selectedWardName : true;
    console.log(isProvinceMatched)
    console.log(isDistrictMatched)
    return isNameMatched && isProvinceMatched && isDistrictMatched && isWardMatched;
  }
  $scope.filterItemsprovince = function () {
    let selectedCity = $scope.citis.find(x => x.Id === $scope.province);
    $scope.selectedDistrictName =''
    $scope.selectedWardName ='';
    $scope.district = null;
    $scope.districts = [];
    $scope.ward = null;
    $scope.wards = []; 

    if(selectedCity) {
        $scope.selectedProvinceName = selectedCity.Name;
        $scope.districts = selectedCity.Districts;
    } else {
        $scope.selectedProvinceName = '';
    }
    $scope.items = $scope.item.filter(filterItems);
    $scope.currentPage = 1; 
    console.log(selectedCity)
};

$scope.filterItemsdistrict = function () {
    if (!$scope.district || !$scope.districts || $scope.districts.length === 0) {         
        $scope.selectedDistrictName = '';
    } else {
        let selectedDistrict = $scope.districts.find(x => x.Id === $scope.district);
        
        if(selectedDistrict) {
            $scope.ward = null;
            $scope.wards = selectedDistrict.Wards;
            $scope.selectedDistrictName = selectedDistrict.Name;
            console.log(selectedDistrict)
        } else {
            
        }
    }
    $scope.items = $scope.item.filter(filterItems);
    $scope.currentPage = 1;
    
}; 

$scope.filterItemsWard = function () {
    let selectedWard = $scope.wards.find(x => x.Id === $scope.ward);
    
    if(selectedWard) {
        $scope.selectedWardName = selectedWard.Name;
    } else {
        $scope.selectedWardName = '';
    }
    $scope.items = $scope.item.filter(filterItems);
    $scope.currentPage = 1; 
};
 
 $scope.getData();
});