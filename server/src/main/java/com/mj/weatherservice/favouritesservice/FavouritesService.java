package com.mj.weatherservice.favouritesservice;

import java.util.List;

import com.mj.weatherservice.model.Location;

public interface FavouritesService {
	
	List<Location> getFavourites();
    
    void addFavourite(Location favourite);
     
    void deleteAllFavourites();
 
    void deleteFavourite(Location favouriteToRemove);

}
