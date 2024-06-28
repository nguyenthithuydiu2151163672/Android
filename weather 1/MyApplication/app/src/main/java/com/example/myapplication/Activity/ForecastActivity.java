package com.example.myapplication.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ForecastAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastActivity extends AppCompatActivity {

    private RecyclerView recyclerViewForecast;
    private final String API_KEY = "e04712cb60396afc5ddd7bcb97bfe6bd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        recyclerViewForecast = findViewById(R.id.recyclerViewForecast);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(this));

        String city = getIntent().getStringExtra("city");

        if (city != null && !city.isEmpty()) {
            new GetForecastTask().execute(city);
        } else {
            Toast.makeText(this, "Không tìm thấy tên thành phố", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetForecastTask extends AsyncTask<String, Void, String> {

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
                    JSONArray list = jsonObject.getJSONArray("list");

                    ForecastAdapter forecastAdapter = new ForecastAdapter(list);
                    recyclerViewForecast.setAdapter(forecastAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ForecastActivity.this, "Lỗi khi phân tích dữ liệu dự báo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForecastActivity.this, "Không thể lấy dữ liệu dự báo thời tiết", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
