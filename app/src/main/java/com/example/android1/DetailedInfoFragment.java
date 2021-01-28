package com.example.android1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.android1.Models.CoordRequest;
import com.example.android1.Models.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android1.BuildConfig.OWM_KEY;

public class DetailedInfoFragment extends Fragment {
    private boolean isLandscapeOrientation;

    private final ArrayList<Forecast> hourlyForecasts = new ArrayList<>();
    private final ArrayList<Forecast> weeklyForecasts = new ArrayList<>();

    private int hourlySpeedWindIndex, hourlyHumidityIndex;
    private int weeklySpeedWindIndex, weeklyHumidityIndex;
    private boolean isWindSpeedEnabled, isHumidityEnabled;
    private boolean isNeedUpdateWindSpeed = false, isNeedUpdateHumidity = false;

    private static final String FORMAT_COORD_REQUEST = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=3&appid=%s";
    private static final String FORMAT_WEATHER_REQUEST = "https://api.openweathermap.org/data/2.5/onecall?lat=%.2f&lon=%.2f&units=metric&exclude=minutely,alerts&appid=%s";

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
                boolean newSettingValue = args.getBoolean("isWindSpeedEnabled", false);
                if (isWindSpeedEnabled != newSettingValue) {
                    isWindSpeedEnabled = newSettingValue;
                    isNeedUpdateWindSpeed = true;
                }

                newSettingValue = args.getBoolean("isHumidityEnabled", false);
                if (isHumidityEnabled != newSettingValue) {
                    isHumidityEnabled = newSettingValue;
                    isNeedUpdateHumidity = true;
                }

                try {
                    String coordQueryString = String.format(FORMAT_COORD_REQUEST, cityName, OWM_KEY);
                    URL url = new URL(coordQueryString);
                    Handler handler = new Handler();

                    new Thread(() -> {
                        HttpURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setReadTimeout(10000);
                            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String result = getLines(in);
                            Gson gson = new Gson();
                            final CoordRequest[] coordRequest = gson.fromJson(result, CoordRequest[].class);
                            urlConnection.disconnect();

                            @SuppressLint("DefaultLocale")
                            String weatherQueryString = String.format(FORMAT_WEATHER_REQUEST, coordRequest[0].getLat(), coordRequest[0].getLon(), OWM_KEY);
                            URL getWeatherUrl = new URL(weatherQueryString);
                            urlConnection = (HttpURLConnection) getWeatherUrl.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setReadTimeout(10000);
                            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            result = getLines(in);
                            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                            handler.post(() -> {
                                city.setText(cityName);
                                displayWeather(weatherRequest);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                        }
                    }).start();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
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
                citiesTransaction.addToBackStack("");
                citiesTransaction.commit();
            });
        }
    }

    private String getLines(BufferedReader in) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = in.readLine();
                if (tempVariable == null) {
                    break;
                }
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData.toString();
    }

    @SuppressLint("DefaultLocale")
    private void displayWeather(WeatherRequest weatherRequest) {
        TextView currentTemperature = getView().findViewById(R.id.current_temperature);
        currentTemperature.setText(String.format("%.0f", weatherRequest.getCurrent().getTemp()));

        createHourlyForecast(weatherRequest);
        createWeeklyForecast(weatherRequest);

        if (isNeedUpdateWindSpeed) {
            setWindSpeedVisibility(isWindSpeedEnabled, weatherRequest);
        }

        if (isNeedUpdateHumidity) {
            setHumidityVisibility(isHumidityEnabled, weatherRequest);
        }

        ForecastAdapter hourlyForecastAdapter, weeklyForecastAdapter;
        RecyclerView hourlyForecast = getView().findViewById(R.id.hourly_forecast_list);
        hourlyForecastAdapter = new ForecastAdapter(getContext(), hourlyForecasts, R.layout.hourly_forecast_list_item, Forecast.HOURLY_FORECAST);
        hourlyForecast.setAdapter(hourlyForecastAdapter);

        RecyclerView weeklyForecast = getView().findViewById(R.id.weekly_forecast_list);
        weeklyForecastAdapter = new ForecastAdapter(getContext(), weeklyForecasts, R.layout.weekly_forecast_list_item, Forecast.WEEKLY_FORECAST);
        weeklyForecast.setAdapter(weeklyForecastAdapter);
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

    @SuppressLint("DefaultLocale")
    private String[] getHourlyTemperature(WeatherRequest weatherRequest) {
        String[] hourlyTemperatures = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlyTemperatures[i] = String.format("%2.0f", weatherRequest.getHourly()[i].getTemp());
        }
        return hourlyTemperatures;
    }

    @SuppressLint("DefaultLocale")
    private String[] getWeeklyTemperature(WeatherRequest weatherRequest) {
        String[] weeklyTemperatures = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklyTemperatures[i] = String.format("%2.0f", weatherRequest.getDaily()[i].getTemp().getDay());
        }
        return weeklyTemperatures;
    }

    @SuppressLint("DefaultLocale")
    private String[] getHourlyPrecipitation(WeatherRequest weatherRequest) {
        String[] hourlyPrecipitation = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlyPrecipitation[i] = String.format("%2.0f", weatherRequest.getHourly()[i].getPop());
        }
        return hourlyPrecipitation;
    }

    @SuppressLint("DefaultLocale")
    private String[] getWeeklyPrecipitation(WeatherRequest weatherRequest) {
        String[] weeklyPrecipitation = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklyPrecipitation[i] = String.format("%2.0f", weatherRequest.getDaily()[i].getPop());
        }
        return weeklyPrecipitation;
    }

    @SuppressLint("DefaultLocale")
    private String[] getHourlySpeedWind(WeatherRequest weatherRequest) {
        String[] hourlySpeedWind = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlySpeedWind[i] = String.format("%2.0f", weatherRequest.getHourly()[i].getWind_speed());
        }
        return hourlySpeedWind;
    }

    @SuppressLint("DefaultLocale")
    private String[] getWeeklySpeedWind(WeatherRequest weatherRequest) {
        String[] weeklySpeedWind = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklySpeedWind[i] = String.format("%2.0f", weatherRequest.getDaily()[i].getWind_speed());
        }
        return weeklySpeedWind;
    }

    @SuppressLint("DefaultLocale")
    private String[] getHourlyHumidity(WeatherRequest weatherRequest) {
        String[] hourlyHumidity = new String[Forecast.HOURLY_FORECAST];
        for (int i = 0; i < Forecast.HOURLY_FORECAST; i++) {
            hourlyHumidity[i] = String.format("%2.0f", weatherRequest.getHourly()[i].getHumidity());
        }
        return hourlyHumidity;
    }

    @SuppressLint("DefaultLocale")
    private String[] getWeeklyHumidity(WeatherRequest weatherRequest) {
        String[] weeklyHumidity = new String[Forecast.WEEKLY_FORECAST];
        for (int i = 0; i < Forecast.WEEKLY_FORECAST; i++) {
            weeklyHumidity[i] = String.format("%2.0f", weatherRequest.getDaily()[i].getHumidity());
        }
        return weeklyHumidity;
    }

    private void setWindSpeedVisibility(boolean visibility, WeatherRequest weatherRequest) {
        if (visibility) {
            Forecast hourlySpeedWind = new Forecast(R.drawable.wind, getHourlySpeedWind(weatherRequest));
            hourlyForecasts.add(hourlySpeedWind);
            hourlySpeedWindIndex = hourlyForecasts.indexOf(hourlySpeedWind);

            Forecast weeklySpeedWind = new Forecast(R.drawable.wind, getWeeklySpeedWind(weatherRequest));
            weeklyForecasts.add(weeklySpeedWind);
            weeklySpeedWindIndex = weeklyForecasts.indexOf(weeklySpeedWind);
        }
        else {
            hourlyForecasts.remove(hourlySpeedWindIndex);
            weeklyForecasts.remove(weeklySpeedWindIndex);
        }
    }

    private void setHumidityVisibility(boolean visibility, WeatherRequest weatherRequest) {
        if (visibility) {
            Forecast hourlyHumidity = new Forecast(R.drawable.humidity, getHourlyHumidity(weatherRequest));
            hourlyForecasts.add(hourlyHumidity);
            hourlyHumidityIndex = hourlyForecasts.indexOf(hourlyHumidity);

            Forecast weeklyHumidity = new Forecast(R.drawable.humidity, getWeeklyHumidity(weatherRequest));
            weeklyForecasts.add(weeklyHumidity);
            weeklyHumidityIndex = weeklyForecasts.indexOf(weeklyHumidity);
        }
        else {
            hourlyForecasts.remove(hourlyHumidityIndex);
            weeklyForecasts.remove(weeklyHumidityIndex);
        }
    }

    private void createHourlyForecast(WeatherRequest weatherRequest) {
        hourlyForecasts.add(new Forecast(R.drawable.clock, getHours()));
        hourlyForecasts.add(new Forecast(R.drawable.temperature, getHourlyTemperature(weatherRequest)));
        hourlyForecasts.add(new Forecast(R.drawable.precipitation, getHourlyPrecipitation(weatherRequest)));
    }

    private void createWeeklyForecast(WeatherRequest weatherRequest) {
        weeklyForecasts.add(new Forecast(R.drawable.calendar, getDays(1)));
        weeklyForecasts.add(new Forecast(R.drawable.temperature, getWeeklyTemperature(weatherRequest)));
        weeklyForecasts.add(new Forecast(R.drawable.precipitation, getWeeklyPrecipitation(weatherRequest)));
    }

//    @Override
//    public void onBackPressed() {
//        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
