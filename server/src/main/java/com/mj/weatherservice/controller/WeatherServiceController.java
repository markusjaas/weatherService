package com.mj.weatherservice.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mj.weatherservice.favouritesservice.FavouritesService;
import com.mj.weatherservice.model.CustomError;
import com.mj.weatherservice.model.Location;
import com.mj.weatherservice.model.WeatherResponse;
import com.mj.weatherservice.openweathermapservice.OpenWeatherMapService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/weatherservice")
public class WeatherServiceController {
	
	public static final Logger logger = LoggerFactory.getLogger(WeatherServiceController.class);
	
	private static final String DEFAULT_LANG = "fi";
	
	@Autowired
	FavouritesService favouritesService;
	
	@Autowired
	OpenWeatherMapService openWeatherMapService;
    
    @RequestMapping(value = "/search/{location}", method = RequestMethod.GET, produces={"application/json"} )
    public ResponseEntity<?> searchWeatherByCityAndLang(@PathVariable("location") String location, @RequestParam(value="lang",required=false) String lang) {
    	
    	if(lang==null){
    		lang = DEFAULT_LANG;
    	}
    	try{
    		logger.debug("Searching weather information for "+ location);
    		return new ResponseEntity<WeatherResponse>(openWeatherMapService.getWeather(location, lang), HttpStatus.OK);
    	}catch (Exception e) {
    		return new ResponseEntity<CustomError>(new CustomError("Could not retrieve weather information to location " +
    				location), HttpStatus.BAD_REQUEST);
		}
    }
    
    //All favourite endpoints are desinged to be launched from search results so they should always have correct payload on call
    @RequestMapping(value = "/favourites/", method = RequestMethod.GET, produces={"application/json"} )
    public ResponseEntity<List<Location>> getFavourites() {
    	
    	return new ResponseEntity<List<Location>>(favouritesService.getFavourites(), HttpStatus.OK);
    }

    @RequestMapping(value = "/favourites/", method = RequestMethod.POST, produces={"application/json"} )
    public ResponseEntity<Location> addFavourite(@RequestBody Location location) {
    	favouritesService.addFavourite(location);
    	//This is designed to add favourite if it does not exists already.
    	//optionally this could return HttpStatus.CONFLICT if location already exists
    	
        return new ResponseEntity<Location>(location, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/favourites/", method = RequestMethod.DELETE )
    public ResponseEntity<Location> deleteFavourites() {
    	favouritesService.deleteAllFavourites();
    	
        return new ResponseEntity<Location>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/favourites/{location}", method = RequestMethod.DELETE, produces={"application/json"} )
    public ResponseEntity<Location> deleteFavourite(@PathVariable("location") String locationId, @RequestBody Location location) {
    	favouritesService.deleteFavourite(location);
    	
        return new ResponseEntity<Location>(location, HttpStatus.NO_CONTENT);
    }
}
