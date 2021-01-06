package com.example.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean isVisibleAddCityMenu = false;

    private static int layoutId = R.layout.activity_main;

    private static final String LOG_TAG = "Android1";
    private static final CitySettings citySettings = CitySettings.getInstance();

    private TextView currentTemperature;
    private CheckBox windSpeed, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onCreate()");

        Button btnAddCity = findViewById(R.id.btn_add_city);
        if (btnAddCity != null) {
            btnAddCity.setOnClickListener(v -> {
                layoutId = R.layout.add_city;
                setContentView(layoutId);

                isVisibleAddCityMenu = true;
                invalidateOptionsMenu();
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onStart()");
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Toast.makeText(getApplicationContext(), "onRestoreInstanceState()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onRestoreInstanceState()");

        currentTemperature = findViewById(R.id.current_temperature);
        if (currentTemperature != null) {
            currentTemperature.setText(String.format("%d *C", savedInstanceState.getInt("currentTemperature")));
        }

        windSpeed = findViewById(R.id.wind_speed);
        humidity = findViewById(R.id.humidity);
        if (windSpeed != null && humidity != null) {
            windSpeed.setChecked(citySettings.isCheckedWindSpeed());
            humidity.setChecked(citySettings.isCheckedHumidity());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onPause()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        currentTemperature = findViewById(R.id.current_temperature);
        if (currentTemperature != null) {
            outState.putInt("currentTemperature", extractCurrentTemperature(currentTemperature.getText().toString()));
        }

        windSpeed = findViewById(R.id.wind_speed);
        humidity = findViewById(R.id.humidity);
        if (windSpeed != null && humidity != null) {
            citySettings.setCheckedWindSpeed(windSpeed.isChecked());
            citySettings.setCheckedHumidity(humidity.isChecked());
        }

        Toast.makeText(getApplicationContext(), "onSaveInstanceState()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (isVisibleAddCityMenu) {
            inflater.inflate(R.menu.add_city_menu, menu);
        }
        else {
            inflater.inflate(R.menu.main_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                layoutId = R.layout.settings;
                break;

            case R.id.add_city_settings:
                layoutId = R.layout.add_city_settings;
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        setContentView(layoutId);

        return true;
    }

    private int extractCurrentTemperature(String temperature) {
        return Integer.parseInt(temperature.split(" ")[0]);
    }
}
