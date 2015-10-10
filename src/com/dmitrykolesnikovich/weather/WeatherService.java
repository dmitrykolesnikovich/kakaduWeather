package com.dmitrykolesnikovich.weather;

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherService {

  private static final String TAG = "WeatherService";
  private static final String APPID = "ed4eea3adcbe63c4cd4de3699b320a4e";
  private static final String GET_3_DAY_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

  public void getWeather(Context context, final int cityId, final WeatherListener listener) {

    StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_3_DAY_FORECAST_URL,
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          Log.d(TAG, response);
          try {
            JSONObject jsonResponse = new JSONObject(response);
            Weather.WeatherCast weatherCast = new Weather.WeatherCast(jsonResponse);
            listener.onResponse(weatherCast.weatherList);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      },
      new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {          
          listener.onError();
        }
      }){
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("units", "metric");
        params.put("id", cityId+"");
        params.put("APPID", APPID+"");
        params.put("cnt", 3+"");
        return params;
      }
    };
    VolleySender.getInstance(context).add(context, stringRequest);
  }

  public interface WeatherListener {
    void onResponse(List<Weather> weatherList);
    void onError();
  }

}
