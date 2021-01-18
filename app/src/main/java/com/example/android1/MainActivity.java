package com.example.android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Android1";
    private static final int REQUEST_CODE = 1;

    private TextView city;
    private TextView currentTemperature;

    private boolean isFirstInfo = true;

    private final ArrayList<Forecast> hourlyForecasts = new ArrayList<>();
    private final ArrayList<Forecast> weeklyForecasts = new ArrayList<>();

    private ForecastAdapter hourlyForecastAdapter, weeklyForecastAdapter;
    private int hourlySpeedWindIndex, hourlyHumidityIndex;
    private int weeklySpeedWindIndex, weeklyHumidityIndex;
    private boolean isWindSpeedEnabled, isHumidityEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCity = findViewById(R.id.btn_add_city);
        if (btnAddCity != null) {
            btnAddCity.setOnClickListener(v -> {
                Intent addCityActivityIntent = new Intent(MainActivity.this, AddCityActivity.class);
                startActivityForResult(addCityActivityIntent, REQUEST_CODE);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            if (data != null) {
                city = findViewById(R.id.city);
                String cityName = data.getStringExtra("cityName");
                if (cityName != null && !cityName.isEmpty()) {
                    city.setText(cityName);
                    currentTemperature = findViewById(R.id.current_temperature);
                    currentTemperature.setText("-13 *C");
                }

                if (isFirstInfo) {
                    createHourlyForecast();
                    createWeeklyForecast();

                    RecyclerView hourlyForecast = findViewById(R.id.hourly_forecast_list);
                    hourlyForecastAdapter = new ForecastAdapter(this, hourlyForecasts, R.layout.hourly_forecast_list_item, Forecast.HOURLY_FORECAST);
                    hourlyForecast.setAdapter(hourlyForecastAdapter);

                    RecyclerView weeklyForecast = findViewById(R.id.weekly_forecast_list);
                    weeklyForecastAdapter = new ForecastAdapter(this, weeklyForecasts, R.layout.weekly_forecast_list_item, Forecast.WEEKLY_FORECAST);
                    weeklyForecast.setAdapter(weeklyForecastAdapter);

                    isFirstInfo = false;
                }

                if (isWindSpeedEnabled != data.getBooleanExtra("isWindSpeedEnabled", false)) {
                    isWindSpeedEnabled = data.getBooleanExtra("isWindSpeedEnabled", false);
                    setWindSpeedVisibility(isWindSpeedEnabled);
                    hourlyForecastAdapter.notifyDataSetChanged();
                    weeklyForecastAdapter.notifyDataSetChanged();
                }

                if (isHumidityEnabled != data.getBooleanExtra("isHumidityEnabled", false)) {
                    isHumidityEnabled = data.getBooleanExtra("isHumidityEnabled", false);
                    setHumidityVisibility(isHumidityEnabled);
                    hourlyForecastAdapter.notifyDataSetChanged();
                    weeklyForecastAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private String[] getHours() {
        String[] hours = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hours[i] = i + ":00";
        }
        return hours;
    }

    private String[] getDays(int firstDay) {
        String[] days = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            days[i] = Integer.toString(firstDay + i);
        }
        return days;
    }

    private String[] getHourlyTemperature() {
        String[] hourlyTemperatures = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlyTemperatures[i] = "-13";
        }
        return hourlyTemperatures;
    }

    private String[] getWeeklyTemperature() {
        String[] weeklyTemperatures = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklyTemperatures[i] = "-25";
        }
        return weeklyTemperatures;
    }

    private String[] getHourlyPrecipitation() {
        String[] hourlyPrecipitation = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlyPrecipitation[i] = "7%";
        }
        return hourlyPrecipitation;
    }

    private String[] getWeeklyPrecipitation() {
        String[] weeklyPrecipitation = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklyPrecipitation[i] = "0%";
        }
        return weeklyPrecipitation;
    }

    private String[] getHourlySpeedWind() {
        String[] hourlySpeedWind = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlySpeedWind[i] = "5";
        }
        return hourlySpeedWind;
    }

    private String[] getWeeklySpeedWind() {
        String[] weeklySpeedWind = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklySpeedWind[i] = "7";
        }
        return weeklySpeedWind;
    }

    private String[] getHourlyHumidity() {
        String[] hourlyHumidity = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlyHumidity[i] = "40%";
        }
        return hourlyHumidity;
    }

    private String[] getWeeklyHumidity() {
        String[] weeklyHumidity = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklyHumidity[i] = "20%";
        }
        return weeklyHumidity;
    }

    private void createHourlyForecast() {
        hourlyForecasts.add(new Forecast(R.drawable.clock, getHours()));
        hourlyForecasts.add(new Forecast(R.drawable.temperature, getHourlyTemperature()));
        hourlyForecasts.add(new Forecast(R.drawable.precipitation, getHourlyPrecipitation()));
    }

    private void createWeeklyForecast() {
        weeklyForecasts.add(new Forecast(R.drawable.calendar, getDays(1)));
        weeklyForecasts.add(new Forecast(R.drawable.temperature, getWeeklyTemperature()));
        weeklyForecasts.add(new Forecast(R.drawable.precipitation, getWeeklyPrecipitation()));
    }

    private void setWindSpeedVisibility(boolean visibility) {
        if (visibility) {
            Forecast hourlySpeedWind = new Forecast(R.drawable.wind, getHourlySpeedWind());
            hourlyForecasts.add(hourlySpeedWind);
            hourlySpeedWindIndex = hourlyForecasts.indexOf(hourlySpeedWind);

            Forecast weeklySpeedWind = new Forecast(R.drawable.wind, getWeeklySpeedWind());
            weeklyForecasts.add(weeklySpeedWind);
            weeklySpeedWindIndex = weeklyForecasts.indexOf(weeklySpeedWind);
        }
        else {
            hourlyForecasts.remove(hourlySpeedWindIndex);
            weeklyForecasts.remove(weeklySpeedWindIndex);
        }
    }

    private void setHumidityVisibility(boolean visibility) {
        if (visibility) {
            Forecast hourlyHumidity = new Forecast(R.drawable.humidity, getHourlyHumidity());
            hourlyForecasts.add(hourlyHumidity);
            hourlyHumidityIndex = hourlyForecasts.indexOf(hourlyHumidity);

            Forecast weeklyHumidity = new Forecast(R.drawable.humidity, getWeeklyHumidity());
            weeklyForecasts.add(weeklyHumidity);
            weeklyHumidityIndex = weeklyForecasts.indexOf(weeklyHumidity);
        }
        else {
            hourlyForecasts.remove(hourlyHumidityIndex);
            weeklyForecasts.remove(weeklyHumidityIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        city = findViewById(R.id.city);
        outState.putString("cityName", city.getText().toString());

        currentTemperature = findViewById(R.id.current_temperature);
        outState.putInt("currentTemperature", extractCurrentTemperature(currentTemperature.getText().toString()));

        RecyclerView hourlyForecast = findViewById(R.id.hourly_forecast_list);
        hourlyForecast.getLayoutManager().onSaveInstanceState();

        Toast.makeText(getApplicationContext(), "onSaveInstanceState()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onSaveInstanceState()");
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        city = findViewById(R.id.city);
        city.setText(savedInstanceState.getString("cityName"));

        currentTemperature = findViewById(R.id.current_temperature);
        currentTemperature.setText(String.format("%d *C", savedInstanceState.getInt("currentTemperature")));

        Toast.makeText(getApplicationContext(), "onRestoreInstanceState()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onRestoreInstanceState()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.settings):
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case (R.id.about_city):
                TextView cityName = findViewById(R.id.city);
                Uri request = Uri.parse("https://en.wikipedia.org/wiki/" + cityName.getText().toString());
                Intent getInfoIntent = new Intent(Intent.ACTION_VIEW, request);
                startActivity(getInfoIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int extractCurrentTemperature(String temperature) {
        return Integer.parseInt(temperature.split(" ")[0]);
    }
}
