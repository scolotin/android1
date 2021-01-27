package com.example.android1.Models;

public class WeatherRequest {
    private Current current;
    private HourForecast[] hourly;
    private WeekForecast[] daily;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public HourForecast[] getHourly() {
        return hourly;
    }

    public void setHourly(HourForecast[] hourly) {
        this.hourly = hourly;
    }

    public WeekForecast[] getDaily() {
        return daily;
    }

    public void setDaily(WeekForecast[] daily) {
        this.daily = daily;
    }
}
