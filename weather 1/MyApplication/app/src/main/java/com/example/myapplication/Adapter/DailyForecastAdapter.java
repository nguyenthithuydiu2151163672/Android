package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.myapplication.R;
import com.example.myapplication.Model.Weather;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DailyForecastAdapter extends ArrayAdapter<Weather> {

    private Context context;
    private List<Weather> weatherList;

    public DailyForecastAdapter(Context context, List<Weather> weatherList) {
        super(context, R.layout.list_item_daily_forecast, weatherList);
        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_daily_forecast, parent, false);
        }

        Weather currentWeather = weatherList.get(position);

        TextView dateTextView = listItem.findViewById(R.id.textViewDate);
        TextView tempTextView = listItem.findViewById(R.id.textViewTemperature);
        TextView descTextView = listItem.findViewById(R.id.textViewDescription);
        ImageView iconImageView = listItem.findViewById(R.id.imageViewIcon);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());
        String date = dateFormat.format(currentWeather.getDt() * 1000L);

        dateTextView.setText(date);
        tempTextView.setText(String.format(Locale.getDefault(), "%.1f°C", currentWeather.getTemp()));
        descTextView.setText(currentWeather.getDescription());

        String iconUrl = String.format("https://openweathermap.org/img/wn/%s@2x.png", currentWeather.getIcon());
        Picasso.get().load(iconUrl).into(iconImageView);

        return listItem;
    }
}
