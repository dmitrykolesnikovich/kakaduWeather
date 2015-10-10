package com.dmitrykolesnikovich.weather;

import android.app.Application;

public class MyApp extends Application {

  public static MyApp instance;

  public MyApp() {
    instance = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Settings.init(this);
  }

}
