package com.dmitrykolesnikovich.weather;

import java.util.*;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WeatherWidgetProvider extends AppWidgetProvider {

  @Override
  public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] ids) {
    final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
    for (int i = 0; i < ids.length; i++) {
      int id = ids[i];
      Intent intent = new Intent(context, MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
      remoteViews.setOnClickPendingIntent(R.id.config, pendingIntent);
      List<Weather> weatherList = Weather.weatherList;
      if (weatherList != null) {
        Weather today1Weather = weatherList.get(0);
        Weather today2Weather = weatherList.get(1);
        Weather today3Weather = weatherList.get(2);
        remoteViews.setTextViewText(R.id.today1, today1Weather.temp + "" + MainActivity.DEGREE_SIGN);
        remoteViews.setTextViewText(R.id.today2, today2Weather.temp + "" + MainActivity.DEGREE_SIGN);
        remoteViews.setTextViewText(R.id.today3, today3Weather.temp + "" + MainActivity.DEGREE_SIGN);
      }
      appWidgetManager.updateAppWidget(id, remoteViews);
    }
  }

}
