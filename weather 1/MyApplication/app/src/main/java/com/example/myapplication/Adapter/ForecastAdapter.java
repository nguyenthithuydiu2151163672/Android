package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private JSONArray forecastList;

    public ForecastAdapter(JSONArray forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject forecast = forecastList.getJSONObject(position);
            long dt = forecast.getLong("dt");
            String date = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(dt * 1000));

            JSONObject main = forecast.getJSONObject("main");
            double temp = main.getDouble("temp");

            JSONObject weatherObject = forecast.getJSONArray("weather").getJSONObject(0);
            String description = weatherObject.getString("description");
            String icon = weatherObject.getString("icon");

            holder.textViewDate.setText(date);
            holder.textViewTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", temp));
            holder.textViewCondition.setText(description);

            String iconUrl = String.format("https://openweathermap.org/img/wn/10d@2x.png", icon);
            Picasso.get().load(iconUrl).into(holder.weatherIcon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return forecastList.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDate;
        public TextView textViewTemperature;
        public TextView textViewCondition;
        public ImageView weatherIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTemperature = itemView.findViewById(R.id.textViewTemperature);
            textViewCondition = itemView.findViewById(R.id.textViewCondition);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }
}
