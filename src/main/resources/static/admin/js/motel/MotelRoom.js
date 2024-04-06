var app = angular.module("myAppMotelRoom", ["ngMessages"]);
let host = "http://localhost:8080";
app.controller("myCtrlMotelRoom", function($scope, $http, $rootScope,$sce) {
 $scope.ListMotelRoom =[];
 $scope.ListMotelRooms=[];
 $scope.ListCategoryRoom=[];
 $scope.selectedCategory = ''; 
 $scope.ListroomStatus =[];
 $scope.selectedroomStatus='';
 $scope.GetListMotelRoom =function(key){
    let url =host+'/api/all/MotelRoom/'+key;
    $http.get(url).then(function (response){
       $scope.ListMotelRoom =response.data;
       $scope.ListMotelRooms =response.data;
       console.log(response.data)
       response.data.forEach(element => {
        if($scope.ListCategoryRoom.indexOf(element.title)===-1){
            $scope.ListCategoryRoom.push(element.title)
        }
       });
       response.data.forEach(element => {
       if(element.name!==null){
        if($scope.ListroomStatus.indexOf(element.name)===-1){
          $scope.ListroomStatus.push(element.name)
      }
       }
       });
       console.log($scope.ListroomStatus)
       console.log($scope.ListCategoryRoom)
       console.table($scope.ListMotelRoom);
       }, function (error) {
           console.log(error)
       });
 }
 $scope.filterItems = function() {
   if($scope.selectedCategory==='' && $scope.selectedroomStatus===''){
    $scope.ListMotelRoom = $scope.ListMotelRooms;
   }else if($scope.selectedCategory==='' && $scope.selectedroomStatus!==''){
    $scope.ListMotelRoom = $scope.ListMotelRooms.filter(function(item) {
        return item.name === $scope.selectedroomStatus;
      });
   }else if($scope.selectedCategory!=='' && $scope.selectedroomStatus===''){
    $scope.ListMotelRoom = $scope.ListMotelRooms.filter(function(item) {
      return item.title === $scope.selectedCategory;
    });
   }else if($scope.selectedCategory!=='' && $scope.selectedroomStatus!==''){
    $scope.ListMotelRoom = $scope.ListMotelRooms.filter(function(item) {
      return item.title === $scope.selectedCategory && item.name === $scope.selectedroomStatus;
    });
   }
 }
 $scope.repvideo=function(src){
    $scope.urlfull ='https://www.youtube.com/embed/'+src;
    $scope.linkfull = $sce.trustAsResourceUrl(  $scope.urlfull );
      console.log($scope.linkfull)
    console.log(src)
 }
 $scope.daovideo=function(){
   $scope.repvideo('video');
 }
})