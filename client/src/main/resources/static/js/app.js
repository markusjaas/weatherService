'use strict';

angular.module('weatherServiceApp', [])
.service('weatherService',[  '$http', function($http) {
	this.queryWeather = function queryWeather(city){
	      return $http.get("http://localhost:8090/weatherservice/search/" + city );
	}
	this.getFavourites = function getFavourites(){
        return $http.get("http://localhost:8090/weatherservice/favourites/" );
        
    }
    
}])
.controller('weatherServiceController',[ "weatherService", '$scope', '$http', function(ws,$scope,$http) {
	$scope.locationModel = {city: ""};
	$scope.favourites = [];
	
	function queryWeather(city){
	     ws.queryWeather(city)
	     .then(function(response){
	    	 	$scope.weather = response.data; 
	    			 })
	     .catch(function(error){
	    	 console.log(error)
	    	 });
	}
	
	$scope.onLoad = function() {
		
		queryWeather("oulu");
    	getFavourites();
    }
	function getFavourites(){
	     ws.getFavourites()
	     .then(function(response){ $scope.favourites = response.data; })
	     .catch(function(error){
	    	 console.log(error)
    	 });
	}

    
    $scope.select = function(){
    	queryWeather($scope.locationModel.city);
    }
    
    
}]);