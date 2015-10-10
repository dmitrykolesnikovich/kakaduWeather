package com.dmitrykolesnikovich.weather;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class CityList extends LinkedHashMap<Integer, City> {

  private final Context context;

  public interface ProgressListener {
    void onProgress(double progress);
  }

  public CityList(final Context context, final ProgressListener listener) {
    this.context = context;
    new AsyncTask() {
      int lines = 0;
      private double total = 74000;

      @Override
      protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (values == null) {
          lines++;
          listener.onProgress(lines / total);
        } else {
          listener.onProgress(1);
        }
      }

      @Override
      protected Object doInBackground(Object[] params) {
        try {
          InputStream inputStream = context.getResources().getAssets().open("city_list.txt");
          BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));
          String line = "";
          while ((line = bufferedInputStream.readLine()) != null) {
            String[] tokens = line.split("\t");
            int id = Integer.valueOf(tokens[0]);
            String name = tokens[1];
            put(id, new City(id, name));
            publishProgress(null);
          }
          publishProgress(new Object());
        } catch (IOException e) {
          e.printStackTrace();
        }


        return null;
      }
    }.execute();

  }

}
