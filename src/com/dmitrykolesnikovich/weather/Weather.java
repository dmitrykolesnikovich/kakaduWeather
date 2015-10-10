package com.dmitrykolesnikovich.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Weather {

  public static List<Weather> weatherList;
  public static int cityId = -1;
  public static CityList cityList;

  public double temp;
  public double pressure;
  public double humidity;
  public double temp_min;
  public double temp_max;

  public static class WeatherCast {
    public final List<Weather> weatherList = new ArrayList<>();

    public WeatherCast(JSONObject jsonResponse) {
      try {
        JSONArray list = jsonResponse.getJSONArray("list");
        for (int index = 0; index < list.length(); index++) {
          Weather weather = new Weather();
          JSONObject weatherObject = list.getJSONObject(index);
          weather.pressure = weatherObject.getDouble("pressure");
          weather.humidity = weatherObject.getDouble("humidity");
          JSONObject tempObject = weatherObject.getJSONObject("temp");
          weather.temp = tempObject.getDouble("day");
          weather.temp_min = tempObject.getDouble("min");
          weather.temp_max = tempObject.getDouble("max");
          weatherList.add(weather);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }


}
