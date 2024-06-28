package com.example.myapplication.Class;

public class WeatherForecastItem {
    private String time;
    private double temperature;
    private String description;
    private String icon;

    public WeatherForecastItem(String time, double temperature, String description, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    // Getters and Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
