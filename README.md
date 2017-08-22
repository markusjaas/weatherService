# weatherService

Steps required to run the weather service. 
1. Clone the repository (git clone https://github.com/markusjaas/weatherService.git)
2. Go to server folder
3. Build it (mvn clean package)
4. Run it (mvn spring-boot:run)

Service will run in http://localhost:8090/weatherservice/

Example queries if client is not used:
http://localhost:8090/weatherservice/search/Ylivieska
http://localhost:8090/weatherservice/search/Ylivieska?lang=en
http://localhost:8090/weatherservice/favourites/

I used https://openweathermap.org/ as the weather source. Api can return multiple locations with search, but my service returns the first element of the https://openweathermap.org/ response.


Steps required to run the weather service web client. 
1. Go to client folder
2. Build it (mvn clean package)
3. Run it (mvn spring-boot:run)

Client can be found from http://localhost:8080/

Favourite searches are populated with Oulu and Kärsämäki and Oulus weather is searched and set as default weather.

Client test are not implemented yet. Due the lack of skills and time.
