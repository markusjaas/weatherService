package com.mj.weatherservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherQueryObject {
	
	private String location;
	
	@JsonProperty(required=false)
	private String unit;
	
	@JsonProperty(required=false)
	private String lang;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

}
