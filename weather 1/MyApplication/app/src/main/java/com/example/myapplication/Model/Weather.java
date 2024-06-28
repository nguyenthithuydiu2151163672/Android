package com.example.myapplication.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather implements Serializable {
    private long dt;
    private double temp;
    private int humidity;
    private String description;
    private String icon;

    public Weather(long dt, double temp, int humidity, String description, String icon) {
        this.dt = dt;
        this.temp = temp;
        this.humidity = humidity;
        this.description = description;
        this.icon = icon;
    }

    public long getDt() {
        return dt;
    }

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", new Locale("vi", "VN"));
        String dateString = dateFormat.format(new Date(dt * 1000));
        return String.format("Ngày: %s\nNhiệt độ: %.1f°C\nĐộ ẩm: %d%%\nMô tả: %s",
                dateString, temp, humidity, description);
    }
}
