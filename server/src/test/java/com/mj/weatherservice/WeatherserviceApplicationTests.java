package com.mj.weatherservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mj.weatherservice.model.Location;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherserviceApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class WeatherserviceApplicationTests {
	
    private static final String baseUrlFind = "http://api.openweathermap.org/data/2.5/find?APPID=120a09bd82b40352968281a2c6db1314&type=like&units=metric&q=";

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();
	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
	//since favourites are kept in memory and modified in every test they must be populated again before tests
	@Before
	public void populateDummyFavourites(){
		restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.DELETE, entity, String.class);

		restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.POST, new HttpEntity<Location>(new Location("Oulu"), headers), String.class);

		restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.POST, new HttpEntity<Location>(new Location("Kärsämäki"), headers), String.class);
		
	}
	
	@LocalServerPort
	private int port;
	
	@Test
	public void testSuccessfulWeatherSearch() throws JSONException {
 
		List<String> assertStrings = retrieveAsserString("Oulu","fi");

		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/search/Oulu"),
				HttpMethod.GET, entity, String.class);
		
		String r = response.getBody();
		for(String assertString :assertStrings){
			Assert.assertTrue(r.contains(assertString));
		}
		assert(response.getStatusCode() == HttpStatus.OK);
	}
	
	@Test
	public void testSuccessfulWeatherSearchWithPartOfCityName() throws JSONException {
		List<String> assertStrings = retrieveAsserString("helsin","fi");
		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/search/helsin"),
				HttpMethod.GET, entity, String.class);
		String r = response.getBody();
		System.out.println(r);
		for(String assertString :assertStrings){
			System.out.println(assertString);
			Assert.assertTrue(r.contains(assertString));
		}
		assert(response.getStatusCode() == HttpStatus.OK);
	}
	
	@Test
	public void testSuccessfulWeatherSearchInEnglish() throws JSONException {
		
		List<String> assertStrings = retrieveAsserString("Oulu","en");
		
		Location loc = new Location();
		loc.setLocationName("Helsinki");

		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/search/Oulu?lang=en"),
				HttpMethod.GET, entity, String.class);
		
		String r = response.getBody();
		for(String assertString :assertStrings){
			Assert.assertTrue(r.contains(assertString));
		}
		assert(response.getStatusCode() == HttpStatus.OK);
	}
	
	@Test
	public void testSuccessfulWeatherSearchWithScandinavianCityName() throws JSONException {
		
		List<String> assertStrings = retrieveAsserString("Kärsämäki","en");


		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/search/Kärsämäki?lang=en"),
				HttpMethod.GET, entity, String.class);
		
		String r = response.getBody();
		for(String assertString :assertStrings){
			Assert.assertTrue(r.contains(assertString));
		}
		assert(response.getStatusCode() == HttpStatus.OK);
	}
	
	@Test
	public void testFailingWeatherSearch() throws JSONException {
		
		String assertString = "{'errorMessage': 'Could not retrieve weather information to location Ou'}";
		
		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/search/Ou"),
				HttpMethod.GET, entity, String.class);
		
		assert(response.getStatusCode() == HttpStatus.BAD_REQUEST);
		
		JSONAssert.assertEquals(assertString, response.getBody(), false);
	}
	
	@Test
	public void testSuccessfulGetFavourites() throws JSONException {
		//dummy favourites are loaded to favourites when server is started
		String assertString = "[{\"locationName\":\"Oulu\"},{\"locationName\":\"Kärsämäki\"}]";
		
		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.GET, entity, String.class);
		
		assert(response.getStatusCode() == HttpStatus.OK);
		
		JSONAssert.assertEquals(assertString, response.getBody(), false);
	}
	
	@Test
	public void testDeleteFavourites() throws JSONException {
		//dummy favourites are loaded to favourites when server is started
		String assertString = "[]";
		
		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.DELETE, entity, String.class);

		assert(response.getStatusCode() == HttpStatus.NO_CONTENT);
		
		response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.GET, entity, String.class);
		
		assert(response.getStatusCode() == HttpStatus.OK);
		
		JSONAssert.assertEquals(assertString, response.getBody(), false);
	}
	
	@Test
	public void testAddFavourite() throws JSONException {
		//dummy favourites are loaded to favourites when server is started
		String assertString = "{\"locationName\":\"Helsinki\"}";
		Location loc = new Location("Helsinki");
		HttpEntity<Location> postEntity = new HttpEntity<Location>(loc, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.POST, postEntity, String.class);
		JSONAssert.assertEquals(assertString, response.getBody(), false);
		
		assert(response.getStatusCode() == HttpStatus.CREATED);
		
	    assertString = "[{\"locationName\":\"Oulu\"},{\"locationName\":\"Kärsämäki\"},{\"locationName\":\"Helsinki\"}]";
		//assert that favourite is actually added
		response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.GET, entity, String.class);
		
		assert(response.getStatusCode() == HttpStatus.OK);
		
		JSONAssert.assertEquals(assertString, response.getBody(), false);
	}
	
	@Test
	public void testAddExistingFavourite() throws JSONException {
		//dummy favourites are loaded to favourites when server is started
		//first get the favourites in the beginning
		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.GET, entity, String.class);
		String assertString = response.getBody();
		
		//try to add oulu again
		Location loc = new Location("Oulu");
		HttpEntity<Location> postEntity = new HttpEntity<Location>(loc, headers);

		response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.POST, postEntity, String.class);
		
		assert(response.getStatusCode() == HttpStatus.CREATED);
		
		response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.GET, entity, String.class);
		
		JSONAssert.assertEquals(assertString, response.getBody(), false);
	}
	
	@Test
	public void testDeleteFavourite() throws JSONException {
		//dummy favourites are loaded to favourites when server is started
		String assertString = "[{\"locationName\":\"Kärsämäki\"}]";
 
		Location loc = new Location("Oulu");
		HttpEntity<Location> postEntity = new HttpEntity<Location>(loc, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/Oulu"),
				HttpMethod.DELETE, postEntity, String.class);
		
		assert(response.getStatusCode() == HttpStatus.NO_CONTENT);
		
		response = restTemplate.exchange(
				createRequestURL("/weatherservice/favourites/"),
				HttpMethod.GET, entity, String.class);
		
		JSONAssert.assertEquals(assertString, response.getBody(), false);
	}
	
	
	private String createRequestURL(String uri) {
		return "http://localhost:" +port + uri;
	}
	
	//retrieve up to date weather information from same service and map values to correct

	private List<String> retrieveAsserString(String location, String lang) throws JSONException {
		ResponseEntity<String> response = restTemplate.exchange(
				baseUrlFind + location + "&lang="+lang,
				HttpMethod.GET, entity, String.class);
		JSONObject assertData = new JSONObject(response.getBody());
		JSONArray locations = assertData.getJSONArray("list");
		List<String> assertStrings = new ArrayList<String>();
		for(int i = 0; i < locations.length() ;i++){
			String name = locations.getJSONObject(i).getString("name");
			String temp = locations.getJSONObject(i).getJSONObject("main").get("temp").toString();
			String desc = locations.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
			String country = locations.getJSONObject(i).getJSONObject("sys").get("country").toString();

			String assertString = "{\"locationName\":\""+ name + "\",\"country\":\""+ country +"\",\"temperature\":\""+ temp 
				+"\",\"description\":\""+ desc + "\"}";
			assertStrings.add(assertString);
		}
		return assertStrings;
	}

}
