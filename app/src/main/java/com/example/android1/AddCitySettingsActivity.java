package com.example.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;

public class AddCitySettingsActivity extends AppCompatActivity {
    private CitySettings citySettings;
    private CheckBox windSpeed, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_settings);

        citySettings = (CitySettings) getIntent().getExtras().getSerializable("citySettings");

        windSpeed = findViewById(R.id.wind_speed);
        windSpeed.setChecked(citySettings.isCheckedWindSpeed());

        humidity = findViewById(R.id.humidity);
        humidity.setChecked(citySettings.isCheckedHumidity());
    }

    @Override
    protected void onPause() {
        super.onPause();

        citySettings.setCheckedWindSpeed(windSpeed.isChecked());
        citySettings.setCheckedHumidity(humidity.isChecked());
    }
}