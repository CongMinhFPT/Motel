var app = angular.module("myAppUpdateMotelRoom", []);

app.controller("myCtrlUpdateMotelRoom", function($scope, $http, $rootScope, $sce) {
  $scope.host = "http://localhost:8080";
  $scope.checkloading =false;
  $scope.ListPostMotel =[];
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, handleError);
    } else { 
        console.log("Geolocation is not supported by this browser.");
    }

    function showPosition(position) {
        var lat = position.coords.latitude;
        var lon = position.coords.longitude;
        
        $http.get('https://nominatim.openstreetmap.org/reverse', {
            params: {
              format: "json",
              lat: lat,
              lon: lon
            }
          }).then(function(response){
            $scope.location = "Latitude: " + lat + 
            ", Longitude: " + lon + 
            ", City: " + response.data.address.city;
            $scope.city =response.data.address.city;
            $scope.GetPostMotelCity($scope.city);
          }).catch(function(error) {
            console.error("Error occurred while fetching location data: ", error);
          });
    }
    function handleError(error) {
        switch(error.code) {
            case error.PERMISSION_DENIED:
                console.log('Không cho');
                $scope.GetPostMotelnew();
                break;
            default:
                console.log("An unknown error occurred.");
                break;
        }
    }
    $scope.GetPostMotelCity =function (city){
           let url =$scope.host+'/api/data/postmotel/'+city;
           $http.get(url).then(
            function (response) {
             $scope.ListPostMotel =response.data;
             console.log($scope.ListPostMotel)
             $scope.checkloading =true;
            },
            function (error) {
              console.log(error);
            }
          );
    }
    $scope.GetPostMotelnew =function (){
      let url =$scope.host+'/api/data/postmotel';
      $http.get(url).then(
       function (response) {
        $scope.ListPostMotel =response.data;
        console.log($scope.ListPostMotel);
        $scope.checkloading =true;
       },
       function (error) {
         console.log(error);
       }
     );
}
});