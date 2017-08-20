package com.mj.weatherservice.favouritesservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.html.FormView;

import org.springframework.stereotype.Service;

import com.mj.weatherservice.model.Location;

@Service("favouritesService")
public class FavouritesServiceImpl implements FavouritesService{
    
     
    private static List<Location> favourites;
     
    static{
    	favourites= populateDummyFavourites();
    }
 
    public List<Location> getFavourites(){
    	return favourites;
    }
    
    public void addFavourite(Location favourite){
    	//avoid duplicates
    	if(!locationExists(favourite)){
    		favourites.add(favourite);
    	}
    }
     
    public void deleteAllFavourites() {
    	favourites.clear();
    }
 
    public void deleteFavourite(Location favouriteToRemove) {
    
        for (Iterator<Location> iterator = favourites.iterator(); iterator.hasNext(); ) {
            Location favourite = iterator.next();
            if (favourite.getLocationName().equals(favouriteToRemove.getLocationName())) {
                iterator.remove();
            }
        }
    }
    
    public Boolean locationExists(Location location){
    	for (Iterator<Location> iterator = favourites.iterator(); iterator.hasNext(); ) {
            Location favourite = iterator.next();
            if (favourite.getLocationName().equals(location.getLocationName()) ) {
                return true;
            }
        }
    	return false;
    }

    private static List<Location> populateDummyFavourites(){
        List<Location> fav = new ArrayList<Location>();
        fav.add(new Location("Oulu"));
        fav.add(new Location("Kärsämäki"));
        return fav;
    }

}
