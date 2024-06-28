package com.example.myapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.Adapter.DailyForecastAdapter;
import com.example.myapplication.Model.Weather;
import com.example.myapplication.Class.LineGraphView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DailyForecastActivity extends AppCompatActivity {

    private ListView listViewDailyForecast;
    private DailyForecastAdapter dailyForecastAdapter;
    private List<Weather> weatherList = new ArrayList<>();
    private final String API_KEY = "e04712cb60396afc5ddd7bcb97bfe6bd";
    private LineGraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        listViewDailyForecast = findViewById(R.id.listViewDailyForecast);
        dailyForecastAdapter = new DailyForecastAdapter(this, weatherList);
        listViewDailyForecast.setAdapter(dailyForecastAdapter);

        graphView = findViewById(R.id.graph);

        String city = getIntent().getStringExtra("city");
        if (city != null) {
            new GetDailyWeatherTask().execute(city);
        } else {
            Toast.makeText(this, "Không nhận được tên thành phố", Toast.LENGTH_SHORT).show();
        }

        listViewDailyForecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Weather selectedWeather = weatherList.get(position);
                // Tạo và hiển thị AlertDialog với thông tin thời tiết
                new AlertDialog.Builder(DailyForecastActivity.this)
                        .setTitle("Thông tin thời tiết")
                        .setMessage(selectedWeather.toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });
    }

    private class GetDailyWeatherTask extends AsyncTask<String, Void, String> {
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
                    Set<String> dateSet = new HashSet<>();

                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject dayObject = listArray.getJSONObject(i);

                        long dt = dayObject.getLong("dt");
                        Date date = new Date(dt * 1000);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("vi", "VN"));

                        String dateString = dateFormat.format(date);

                        // Chỉ thêm dữ liệu lúc 12:00 PM mỗi ngày
                        if (dateString.contains("12:00:00")) {
                            String dateOnly = dateString.split(" ")[0];
                            if (!dateSet.contains(dateOnly)) {
                                dateSet.add(dateOnly);

                                JSONObject mainObject = dayObject.getJSONObject("main");
                                double temp = mainObject.getDouble("temp");
                                int humidity = mainObject.getInt("humidity");
                                JSONObject weatherObject = dayObject.getJSONArray("weather").getJSONObject(0);
                                String description = weatherObject.getString("description");
                                String icon = weatherObject.getString("icon");

                                Weather weather = new Weather(dt, temp, humidity, description, icon);
                                weatherList.add(weather);
                            }
                        }
                    }
                    dailyForecastAdapter.notifyDataSetChanged();
                    graphView.setWeatherList(weatherList);  // Cập nhật dữ liệu cho biểu đồ

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DailyForecastActivity.this, "Lỗi khi phân tích dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DailyForecastActivity.this, "Không thể lấy dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
