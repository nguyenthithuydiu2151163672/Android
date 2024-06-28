package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private TextView textViewTemperature;
    private TextView textViewCondition;
    private TextView textViewHumidity;
    private TextView textViewCloudiness;
    private TextView textViewWindSpeed;
    private TextView textViewDate;
    private ImageView weatherIcon;
    private ImageView iconHumidity;
    private ImageView iconCloudiness;
    private ImageView iconWind;
    private TextView textViewCityName;

    private final String API_KEY = "e04712cb60396afc5ddd7bcb97bfe6bd";
    private final String DEFAULT_CITY = "Hà Nội";

    private boolean isCelsius = true;
    private double currentTempCelsius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewCondition = findViewById(R.id.textViewCondition);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewCloudiness = findViewById(R.id.textViewCloudiness);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewDate = findViewById(R.id.textViewDate);
        weatherIcon = findViewById(R.id.weatherIcon);
        iconHumidity = findViewById(R.id.iconHumidity);
        iconCloudiness = findViewById(R.id.iconCloudiness);
        iconWind = findViewById(R.id.iconWind);
        textViewCityName = findViewById(R.id.textViewCityName);

        Button buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                if (!city.isEmpty()) {
                    if (isNetworkAvailable()) {
                        new GetWeatherTask().execute(city);
                    } else {
                        showNetworkSettingsAlert();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên thành phố", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút để chuyển sang Activity dự báo thời tiết theo ngày
        Button buttonForecast1 = findViewById(R.id.buttonForecast1);
        buttonForecast1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                if (!city.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DailyForecastActivity.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên thành phố", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút để chuyển sang Activity dự báo thời tiết theo giờ
        Button buttonForecast = findViewById(R.id.buttonForecast);
        buttonForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                if (!city.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, HourlyForecastActivity.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên thành phố", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút để chuyển đổi đơn vị nhiệt độ
        Button buttonConvertUnit = findViewById(R.id.buttonConvertUnit);
        buttonConvertUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTemperatureUnit();
            }
        });

        // Hiển thị ngày hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm:ss", new Locale("vi", "VN"));
        textViewDate.setText(sdf.format(new Date()));

        // Đặt tên thành phố mặc định ban đầu
        textViewCityName.setText(DEFAULT_CITY);
    }

    private void toggleTemperatureUnit() {
        if (isCelsius) {
            double tempFahrenheit = (currentTempCelsius * 9/5) + 32;
            textViewTemperature.setText(String.format(Locale.getDefault(), "%.1f°F", tempFahrenheit));
        } else {
            textViewTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", currentTempCelsius));
        }
        isCelsius = !isCelsius;
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String urlString = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, API_KEY);
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

                    JSONObject main = jsonObject.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    currentTempCelsius = temp;
                    textViewTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", temp));

                    int humidity = main.getInt("humidity");
                    textViewHumidity.setText(humidity + "%");

                    JSONObject clouds = jsonObject.getJSONObject("clouds");
                    int cloudiness = clouds.getInt("all");
                    textViewCloudiness.setText(cloudiness + "%");

                    JSONObject wind = jsonObject.getJSONObject("wind");
                    double windSpeed = wind.getDouble("speed");
                    textViewWindSpeed.setText(windSpeed + " m/s");

                    String cityName = jsonObject.getString("name");
                    textViewCityName.setText(cityName);

                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    if (weatherArray.length() > 0) {
                        JSONObject weatherObject = weatherArray.getJSONObject(0);
                        String description = weatherObject.getString("description");
                        textViewCondition.setText(description);

                        String icon = weatherObject.getString("icon");
                        String iconUrl = String.format("https://openweathermap.org/img/wn/%s@2x.png", icon);
                        Picasso.get().load(iconUrl).into(weatherIcon);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Lỗi khi phân tích dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Không thể lấy dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNetworkSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Cài đặt mạng");
        alertDialog.setMessage("Ứng dụng không thể kết nối Internet. Bạn có muốn vào cài đặt mạng?");
        alertDialog.setPositiveButton("Cài đặt", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(intent);
        });
        alertDialog.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
}
