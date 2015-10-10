package com.dmitrykolesnikovich.weather;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

  public static final String PREF_NAME = "weather_preferences";
  private static SharedPreferences.Editor editor;
  private static SharedPreferences preferences;

  public static void init(Context context) {
    preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    editor = preferences.edit();
  }

  public static void setSetting(String key, String value) {
    editor.putString(key, value);
    editor.commit();
  }

  public static String getSetting(String key) {
    return preferences.getString(key, null);
  }

}
