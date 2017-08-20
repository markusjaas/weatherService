package com.mj.weatherservice.model;

public class Location {
	private String locationName;
	
	public Location(){}
	
	public Location (String locationName){
		this.locationName = locationName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
}
