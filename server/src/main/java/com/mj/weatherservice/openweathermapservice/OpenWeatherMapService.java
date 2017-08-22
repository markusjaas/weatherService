package com.mj.weatherservice.openweathermapservice;


import com.mj.weatherservice.model.WeatherResponse;

public interface OpenWeatherMapService {

	WeatherResponse getWeather(String location, String lang) throws Exception;;

}
