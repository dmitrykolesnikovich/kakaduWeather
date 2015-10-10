package com.dmitrykolesnikovich.weather;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

  public static final char DEGREE_SIGN = (char) 0x00B0;
  public static final int CHOOSE_CITY_CODE = 2;

  private Button chooseCityButton;
  private Button getWeatherButton;
  private ProgressBar progressBar;
  private LinearLayout resultLayout;
  private LinearLayout errorLayout;
  private TextView cityTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    chooseCityButton = (Button) findViewById(R.id.chooseCity);
    getWeatherButton = (Button) findViewById(R.id.getWeather);
    cityTextView = (TextView) findViewById(R.id.city);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    resultLayout = (LinearLayout) findViewById(R.id.result_layout);
    errorLayout = (LinearLayout) findViewById(R.id.error_layout);

    String settingsCityId = Settings.getSetting("cityId");
    if (settingsCityId != null) {
      int cityId = Integer.valueOf(settingsCityId);
      setWeatherFor(cityId);
    }

    chooseCityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivityForResult(new Intent(MainActivity.this, ChooseCityActivity.class), CHOOSE_CITY_CODE);
      }
    });

    getWeatherButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Weather.cityId != -1) {
          setWeatherFor(Weather.cityId);
        }
      }
    });

    Weather.cityList = new CityList(this, new CityList.ProgressListener() {
      @Override
      public void onProgress(double progress) {
        progressBar.setProgress((int) (progress * 100));
        if (progress == 1) {
          findViewById(R.id.progress_layout).setVisibility(View.GONE);
          findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
          chooseCityButton.setEnabled(true);
          getWeatherButton.setEnabled(true);
          if (Weather.cityId != -1) {
            setCity(Weather.cityId);
          }
        }
      }
    });

  }

  private void setWeatherFor(final int cityId) {
    setCity(cityId);
    final WeatherService.WeatherListener listener = new WeatherService.WeatherListener() {
      @Override
      public void onResponse(final List<Weather> weatherList) {
        Weather.weatherList = weatherList;
        MainActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            resultLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            resultLayout.removeAllViews();
            for (Weather weather : weatherList) {
              LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.weather_item, null);
              TextView temp_min = (TextView) linearLayout.findViewById(R.id.temp_min);
              TextView temp = (TextView) linearLayout.findViewById(R.id.temp);
              TextView humidity = (TextView) linearLayout.findViewById(R.id.humidity);
              TextView pressure = (TextView) linearLayout.findViewById(R.id.pressure);
              TextView temp_max = (TextView) linearLayout.findViewById(R.id.temp_max);
              temp_min.setText("Min temp: " + weather.temp_min + "" + DEGREE_SIGN);
              temp_max.setText("Max temp: " + weather.temp_max + "" + DEGREE_SIGN);
              temp.setText("Temp: " + weather.temp + "" + DEGREE_SIGN);
              humidity.setText("Humidity: " + weather.humidity + "%");
              pressure.setText("Pressure: " + weather.pressure + "");
              resultLayout.addView(linearLayout);
            }
          }
        });

        Intent intent = new Intent(MainActivity.this, WeatherWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WeatherWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
      }

      @Override
      public void onError() {
        resultLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
      }
    };
    new WeatherService().getWeather(MainActivity.this, cityId, listener);
  }

  private void setCity(int cityId) {
    Weather.cityId = cityId;
    Settings.setSetting("cityId", cityId + "");
    if (Weather.cityList != null) {
      City city = Weather.cityList.get(cityId);
      cityTextView.setText("Your City: " + city.name);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (data != null) {
      if (requestCode == CHOOSE_CITY_CODE) {
        int cityId = data.getIntExtra("cityId", -1);
        if (cityId != -1) {
          setWeatherFor(cityId);
        }
      }
    }
  }

}
