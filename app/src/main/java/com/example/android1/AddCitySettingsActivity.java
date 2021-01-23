package com.example.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

public class AddCitySettingsActivity extends AppCompatActivity {
    private CheckBox windSpeed, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_settings);

        findViews();
        windSpeed.setChecked(getIntent().getBooleanExtra("isWindSpeedEnabled", windSpeed.isChecked()));
        humidity.setChecked(getIntent().getBooleanExtra("isHumidityEnabled", humidity.isChecked()));

        Button btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> {
            Intent addCitySettingsIntent = new Intent();
            addCitySettingsIntent.putExtra("isWindSpeedEnabled", windSpeed.isChecked());
            addCitySettingsIntent.putExtra("isHumidityEnabled", humidity.isChecked());
            setResult(RESULT_OK, addCitySettingsIntent);
            finish();
        });
    }

    private void findViews() {
        windSpeed = findViewById(R.id.wind_speed);
        humidity = findViewById(R.id.humidity);
    }
}