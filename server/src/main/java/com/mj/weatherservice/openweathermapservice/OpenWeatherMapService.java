package com.mj.weatherservice.openweathermapservice;

import java.util.List;

import com.mj.weatherservice.model.WeatherResponse;

public interface OpenWeatherMapService {

	List<WeatherResponse> getWeather(String location, String lang) throws Exception;;

}
