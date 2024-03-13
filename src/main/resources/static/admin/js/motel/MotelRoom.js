var app = angular.module("myAppMotelRoom", ["ngMessages"]);
let host = "http://localhost:8080";
app.controller("myCtrlMotelRoom", function($scope, $http, $rootScope,$sce) {
 $scope.ListMotelRoom =[];
 $scope.ListMotelRooms=[];
 $scope.ListCategoryRoom=[];
 $scope.selectedCategory = ''; 
 $scope.GetListMotelRoom =function(key){
    let url =host+'/api/all/MotelRoom/'+key;
    $http.get(url).then(function (response){
       $scope.ListMotelRoom =response.data;
       $scope.ListMotelRooms =response.data;
       response.data.forEach(element => {
        if($scope.ListCategoryRoom.indexOf(element.title)===-1){
            $scope.ListCategoryRoom.push(element.title)
        }
       });
       console.log($scope.ListCategoryRoom)
       console.table($scope.ListMotelRoom);
       }, function (error) {
           console.log(error)
       });
 }
 $scope.filterItems = function() {
   if($scope.selectedCategory===''){
    $scope.ListMotelRoom =$scope.ListMotelRooms;
   }else{
    $scope.ListMotelRoom = $scope.ListMotelRooms.filter(function(item) {
        return item.title === $scope.selectedCategory;
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