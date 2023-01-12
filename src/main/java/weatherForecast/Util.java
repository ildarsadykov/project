package weatherForecast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Util {
  private static StringBuilder weatherAPI(String name) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + name + "&units=metric&APPID=b13856071761ad4e811aafda1944e2dd";
        HttpURLConnection connection = null;
        StringBuilder sb = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();
            sb = new StringBuilder();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }

            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return sb;
    }

    static String showInfo(String name) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(weatherAPI(name).toString());
        } catch (ParseException e) {
            System.out.println("wrong format, request for: "+name+" not found");
        }

        JSONObject city = (JSONObject) jsonObject.get("city");
        String currentCity = (String) city.get("name");

        JSONArray weatherArray = (JSONArray) jsonObject.get("list");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        JSONObject currentWeatherData = (JSONObject) weatherData.get("main");
        double currentTemperature = (double) currentWeatherData.get("temp");

        JSONObject windSpeedData = (JSONObject) weatherData.get("wind");
        double windSpeed = (double) windSpeedData.get("speed");

        JSONArray forecastData = (JSONArray) weatherData.get("weather");
        JSONObject currentForecast = (JSONObject) forecastData.get(0);
        String currentForecastDescription = (String) currentForecast.get("description");

        return "City: " + currentCity + "\n"
                + "Temp: " + currentTemperature + "\u00B0"+"C" + "\n"
                +"Wind speed: "+windSpeed+" m/s"+"\n"
                +"Forecast: "+currentForecastDescription;
    }
}