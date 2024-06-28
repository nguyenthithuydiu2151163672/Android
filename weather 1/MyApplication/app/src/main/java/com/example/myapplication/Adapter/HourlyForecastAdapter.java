package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Class.DownloadImageTask;
import com.example.myapplication.Model.Weather;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HourlyForecastAdapter extends BaseAdapter {
    private Context context;
    private List<Weather> weatherList;

    public HourlyForecastAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_hourly_forecast, parent, false);
        }

        Weather weather = weatherList.get(position);

        ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
        TextView textViewTime = convertView.findViewById(R.id.textViewTime);
        TextView textViewTemperature = convertView.findViewById(R.id.textViewTemperature);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);

        // Đặt icon cho thời tiết
        String iconUrl = String.format("https://openweathermap.org/img/wn/%s@2x.png", weather.getIcon());
        new DownloadImageTask(imageViewIcon).execute(iconUrl);

        // Định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(weather.getDt() * 1000L));

        textViewTime.setText(formattedTime);
        textViewTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", weather.getTemp()));
        textViewDescription.setText(weather.getDescription());

        return convertView;
    }
}
