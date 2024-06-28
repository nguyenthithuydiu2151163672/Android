package com.example.myapplication.Model;

public class ForecastItem {

    private long date;
    private double minTemp;
    private double maxTemp;
    private String description;
    private String icon;

    public ForecastItem(long date, double minTemp, double maxTemp, String description, String icon) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.description = description;
        this.icon = icon;
    }

    public long getDate() {
        return date;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
