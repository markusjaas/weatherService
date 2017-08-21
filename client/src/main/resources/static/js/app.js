'use strict';

angular.module('weatherServiceApp', [])
.controller('weatherServiceController', function($scope, $http){
    $scope.onLoad() = function() {

    	queryWeather();
    	getFavourites();
    }
    
    $scope.location = "Oulu";

    function queryWeather(){
      $http.get("http://localhost:8090/weatherservice/search/" + $scope.loc )
      .then(function(response){ $scope.weather = response.data; });
    }
    function getFavourites(){
        $http.get("http://localhost:8090/weatherservice/favourites/" )
        .then(function(response){ $scope.favourites = response.data; });
    }

});