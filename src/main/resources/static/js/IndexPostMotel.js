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
              $scope.ListPostMotel = response.data;
             console.log($scope.ListPostMotel)
             $scope.checkloading =true;
            },
            function (error) {
              console.log(error);
            }
          );
    }
    $scope.counter = function(event) {
      var totalItems = $scope.ListPostMotel.length;
      if(totalItems<=3){
        totalItems=1;
      }else{
        var totalItems = $scope.ListPostMotel.length-2;
      }
      var currentPage = event.item.index + 1;
    
      $('#counter').text(currentPage + ' / ' + totalItems);
    }
    $scope.GetPostMotelnew =function (){
      let url =$scope.host+'/api/data/postmotel';
      $http.get(url).then(
       function (response) {
        $scope.ListPostMotel = response.data;
        console.log($scope.ListPostMotel);
        $scope.checkloading =true;
        
       },
       function (error) {
         console.log(error);
       }
     );
    }
  

$scope.initCarousel = function(element) {
  var customOptions = $scope.$eval($(element).attr('data-options'));
  var defaultOptions = {
    items: 3, // hiển thị 3 items một lần
    nav: true,
    dotsEach: true, // hiển thị dấu chấm cho mỗi group
    onInitialized: $scope.counter, 
    onChanged: $scope.counter
  };

  for(var key in customOptions) {
    defaultOptions[key] = customOptions[key];
  }

  var owl = $(element).owlCarousel(defaultOptions);

  owl.on('changed.owl.carousel', function(event) {
    $scope.counter(event);
  });

  $scope.reloadCarousel = function() {
    owl.trigger('refresh.owl.carousel');
  }
};
});

app.directive('owlCarouselItem', [function() {
  return {
      restrict: 'A',
      transclude: false,
      link: function(scope, element) {
          // Chờ cho đến khi item ng-repeat cuối cùng sau đó gọi init
          if(scope.$last) {
              scope.$parent.initCarousel(element.parent());
          }
      }
  };
}]);
