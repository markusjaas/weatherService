package com.mj.weatherservice.openweathermapservice;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.mj.weatherservice.model.WeatherResponse;


@Service("openWeatherMapService")
public class OpenWeatherMapServiceImpl implements OpenWeatherMapService{

	public static final Logger logger = LoggerFactory.getLogger(OpenWeatherMapServiceImpl.class);

    private static final String baseUrlFind = "http://api.openweathermap.org/data/2.5/find?APPID=120a09bd82b40352968281a2c6db1314&type=like&mode=xml&units=metric&q=";
    
    
    public WeatherResponse getWeather(String location, String lang) throws Exception{
    	RestTemplate restTemplate = new RestTemplate();
		String queryUrl = baseUrlFind + location + "&lang="+lang;
    	return parseXml(restTemplate.getForObject(queryUrl, String.class));
		
    }
    
    private  WeatherResponse parseXml(String xml) throws ParserConfigurationException, SAXException, IOException{
    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	Document doc = db.parse(new InputSource(new StringReader(xml)));
    	NodeList locations = doc.getElementsByTagName("item");
    	// we only care about the best match
    	
    	Element location = (Element) locations.item(0);
    	    
	    String locationName = location.getElementsByTagName("city").item(0).getAttributes().getNamedItem("name").getNodeValue();
	    String country = location.getElementsByTagName("country").item(0).getTextContent();
	    String temperature = location.getElementsByTagName("temperature").item(0).getAttributes().getNamedItem("value").getNodeValue();
	    String description = location.getElementsByTagName("weather").item(0).getAttributes().getNamedItem("value").getNodeValue();
	    WeatherResponse wr = new WeatherResponse(locationName, country, temperature, description);
    	    
    	return wr;
    }
    
 
}
