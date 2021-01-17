package com.example.android1;

public class Forecast {
    private int icon;
    private String[] values;

    public static final int HOURLY_FORECAST = 24;
    public static final int WEEKLY_FORECAST = 7;

    public Forecast(int icon, String[] values) {
        this.icon = icon;
        this.values = values;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
