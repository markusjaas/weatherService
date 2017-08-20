package com.mj.weatherservice.model;

public class WeatherResponse {
	
	private String locationName;
	
	private String country;
	
	private String temperature;
	
	private String description;
	
	public WeatherResponse(String locationName, String country, String temperature, String description){
		this.locationName = locationName;
		this.country = country;
		this.temperature = temperature;
		this.description = description;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
