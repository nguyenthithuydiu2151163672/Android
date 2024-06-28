package com.example.myapplication.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Class.LineGraphView;
import com.example.myapplication.R;
import com.example.myapplication.Adapter.HourlyForecastAdapter;
import com.example.myapplication.Model.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HourlyForecastActivity extends AppCompatActivity {

    private ListView listViewHourlyForecast;
    private HourlyForecastAdapter hourlyForecastAdapter;
    private List<Weather> weatherList = new ArrayList<>();
    private final String API_KEY = "e04712cb60396afc5ddd7bcb97bfe6bd";
    private LineGraphView lineGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        listViewHourlyForecast = findViewById(R.id.listViewHourlyForecast);
        hourlyForecastAdapter = new HourlyForecastAdapter(this, weatherList);
        listViewHourlyForecast.setAdapter(hourlyForecastAdapter);

        lineGraphView = findViewById(R.id.lineGraphView);

        String city = getIntent().getStringExtra("city");
        if (city != null) {
            new GetHourlyWeatherTask().execute(city);
        } else {
            Toast.makeText(this, "Không nhận được tên thành phố", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetHourlyWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String urlString = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric", city, API_KEY);
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray listArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject hourObject = listArray.getJSONObject(i);

                        long dt = hourObject.getLong("dt");
                        JSONObject mainObject = hourObject.getJSONObject("main");
                        double temp = mainObject.getDouble("temp");
                        int humidity = mainObject.getInt("humidity");
                        JSONObject weatherObject = hourObject.getJSONArray("weather").getJSONObject(0);
                        String description = weatherObject.getString("description");
                        String icon = weatherObject.getString("icon");
                        Weather weather = new Weather(dt, temp, humidity, description, icon);
                        weatherList.add(weather);
                    }
                    hourlyForecastAdapter.notifyDataSetChanged();
                    lineGraphView.setWeatherList(weatherList);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HourlyForecastActivity.this, "Lỗi khi phân tích dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(HourlyForecastActivity.this, "Không thể lấy dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
