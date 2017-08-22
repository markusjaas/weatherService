'use strict';

angular.module('weatherServiceApp', [])
.service('weatherService',[  '$http', function($http) {
	this.queryWeather = function queryWeather(city){
	      return $http.get("http://localhost:8090/weatherservice/search/" + city );
	}
	this.getFavourites = function getFavourites(){
        return $http.get("http://localhost:8090/weatherservice/favourites/" );
        
    }
	
	this.addFavourite = function addFavourite(location){
		return $http.put("http://localhost:8090/weatherservice/favourites/", location, {headers: {'Content-Type': 'application/json'} });
	}
	
	this.removeFavourite = function removeFavourite(location){
		return $http({
	        url: "http://localhost:8090/weatherservice/favourites/" + location,
	        method: 'DELETE',
	        data: {
	            locationName: location
	        },
	        headers: {
	            "Content-Type": "application/json;charset=utf-8"
	        }
	    });
	}
	
	this.deleteFavourites = function deleteFavourites(){
		return $http({
	        url: "http://localhost:8090/weatherservice/favourites/",
	        method: 'DELETE'

	    });
	}
    
}])
.controller('weatherServiceController',[ "weatherService", '$scope', '$http', function(ws,$scope,$http) {
	$scope.locationModel = {city: ""};
	$scope.favourites = [];
	$scope.error = false;
	function queryWeather(city){
	     ws.queryWeather(city)
	     .then(function(response){
	    	 	$scope.weather = response.data; 
	    	 	$scope.error = false;
	    			 })
	     .catch(function(error){
	    	 $scope.error = true;
	    	 });
	}
	
	$scope.onLoad = function() {
		
		queryWeather("oulu");
    	getFavourites();
    }
	function getFavourites(){
	     ws.getFavourites()
	     .then(function(response){ 
	    	 $scope.favourites = response.data;
	     })
	     .catch(function(error){
	    	 console.log(error)
    	 });
	}
	$scope.addFavourite = function(){
		var parameter = JSON.stringify({locationName: $scope.weather.locationName});
	    ws.addFavourite(parameter)
	     .then(function(response){ console.log(response)} )
	     .catch(function(error){
			 console.log(error)
		});
	     
	    ws.getFavourites()
	     .then(function(response){ 
	    	 $scope.favourites = response.data;
	     })
	     .catch(function(error){
	    	 console.log(error)
   	 });

	}
    
	$scope.removeFavourite = function(idx){
		var location = $scope.favourites[idx].locationName;
		var json = JSON.stringify({locationName: location});
	    ws.removeFavourite(location)
	     .then(function(response){ console.log(response)} )
	     .catch(function(error){
			 console.log(error)
		});
	     
	    ws.getFavourites()
	     .then(function(response){ 
	    	 $scope.favourites = response.data;
	     })
	     .catch(function(error){
	    	 console.log(error)
   	 });

	}
	
	$scope.searchFavourite = function(idx){
		var location = $scope.favourites[idx].locationName;
	    queryWeather(location);

	}
	
    $scope.select = function(){
    	queryWeather($scope.locationModel.city);
    }
    
    
    $scope.deleteAllFavourites = function(){
	    ws.deleteFavourites()
	     .then(function(response){ console.log(response)} )
	     .catch(function(error){
			 console.log(error)
		});
	     
	    ws.getFavourites()
	     .then(function(response){ 
	    	 $scope.favourites = response.data;
	     })
	     .catch(function(error){
	    	 console.log(error)
   	 });
	  
	}
}]);