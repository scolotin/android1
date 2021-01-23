package com.example.android1;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailedInfoFragment extends Fragment {
    private boolean isLandscapeOrientation;

    private final ArrayList<Forecast> hourlyForecasts = new ArrayList<>();
    private final ArrayList<Forecast> weeklyForecasts = new ArrayList<>();

    private int hourlySpeedWindIndex, hourlyHumidityIndex;
    private int weeklySpeedWindIndex, weeklyHumidityIndex;
    private boolean isWindSpeedEnabled, isHumidityEnabled;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState == null) {
            setHasOptionsMenu(true);
        }

        return inflater.inflate(R.layout.detailed_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isLandscapeOrientation) {
            hideButton();
        }

        Bundle args = getArguments();
        if (args != null) {
            TextView city = view.findViewById(R.id.city);
            String cityName = args.getString("cityName");
            if (cityName != null && !cityName.isEmpty()) {
                city.setText(cityName);

                createHourlyForecast();
                createWeeklyForecast();

                ForecastAdapter hourlyForecastAdapter, weeklyForecastAdapter;
                RecyclerView hourlyForecast = view.findViewById(R.id.hourly_forecast_list);
                hourlyForecastAdapter = new ForecastAdapter(getContext(), hourlyForecasts, R.layout.hourly_forecast_list_item, Forecast.HOURLY_FORECAST);
                hourlyForecast.setAdapter(hourlyForecastAdapter);

                RecyclerView weeklyForecast = view.findViewById(R.id.weekly_forecast_list);
                weeklyForecastAdapter = new ForecastAdapter(getContext(), weeklyForecasts, R.layout.weekly_forecast_list_item, Forecast.WEEKLY_FORECAST);
                weeklyForecast.setAdapter(weeklyForecastAdapter);

                if (isWindSpeedEnabled != args.getBoolean("isWindSpeedEnabled", false)) {
                    isWindSpeedEnabled = args.getBoolean("isWindSpeedEnabled", false);
                    setWindSpeedVisibility(isWindSpeedEnabled);
                    hourlyForecastAdapter.notifyDataSetChanged();
                    weeklyForecastAdapter.notifyDataSetChanged();
                }

                if (isHumidityEnabled != args.getBoolean("isHumidityEnabled", false)) {
                    isHumidityEnabled = args.getBoolean("isHumidityEnabled", false);
                    setHumidityVisibility(isHumidityEnabled);
                    hourlyForecastAdapter.notifyDataSetChanged();
                    weeklyForecastAdapter.notifyDataSetChanged();
                }
            }
        }

        Button btnAddCity = view.findViewById(R.id.btn_add_city);
        if (btnAddCity != null && !isLandscapeOrientation) {
            btnAddCity.setOnClickListener(v -> {
                Bundle citiesArgs = new Bundle();
                citiesArgs.putBoolean("isWindSpeedEnabled", isWindSpeedEnabled);
                citiesArgs.putBoolean("isHumidityEnabled", isHumidityEnabled);
                CitiesFragment citiesFragment = new CitiesFragment();
                citiesFragment.setArguments(citiesArgs);
                FragmentTransaction citiesTransaction = getFragmentManager().beginTransaction();
                citiesTransaction.replace(R.id.main_container, citiesFragment);
                citiesTransaction.commit();
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.about_city):
                TextView cityName = getView().findViewById(R.id.city);
                Uri request = Uri.parse("https://en.wikipedia.org/wiki/" + cityName.getText().toString());
                Intent getInfoIntent = new Intent(Intent.ACTION_VIEW, request);
                startActivity(getInfoIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideButton() {
        Button btnAddCity = getView().findViewById(R.id.btn_add_city);
        btnAddCity.setVisibility(View.GONE);
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
}
