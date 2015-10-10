package com.dmitrykolesnikovich.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class ChooseCityActivity extends Activity {

  public static final String TAG = "ChooseCityActivity";

  private EditText textView;
  private ListView listView;
  private List<City> sortedCityList = new ArrayList<>();
  private ArrayAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.choose_city);
    textView = (EditText) findViewById(R.id.textView);
    listView = (ListView) findViewById(R.id.listView);

    textView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence text, int start, int before, int count) {
        sortedCityList.clear();
        if (text.length() <= 2) {
          resetSortedList();
        } else {
          for (City city : Weather.cityList.values()) {
            if (city.name.startsWith(text.toString())) {
              sortedCityList.add(city);
            }
          }
        }
        adapter.notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    adapter = new ArrayAdapter(this, -1) {
      @Override
      public int getCount() {
        return sortedCityList.size();
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
          convertView = new TextView(getContext());
        }
        TextView textView = (TextView) convertView;
        textView.setText(sortedCityList.get(position).name);
        return textView;
      }
    };


    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView parent, View view, int position, long id) {
        City city = sortedCityList.get(position);
        int cityId = city.id;
        Intent data = new Intent();
        data.putExtra("cityId", cityId);
        setResult(0, data);
        ChooseCityActivity.this.finish();
      }
    });

    resetSortedList();
  }

  private void resetSortedList() {
    sortedCityList.addAll(Weather.cityList.values());
  }

}
