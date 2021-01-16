package com.example.android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Android1";
    private static final int REQUEST_CODE = 1;

    private TextView city;
    private TextView currentTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCity = findViewById(R.id.btn_add_city);
        btnAddCity.setOnClickListener(v -> {
            Intent addCityActivityIntent = new Intent(MainActivity.this, AddCityActivity.class);
            startActivityForResult(addCityActivityIntent, REQUEST_CODE);
        });

        Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onCreate()");
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
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "onStart()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        city = findViewById(R.id.city);
        outState.putString("cityName", city.getText().toString());

        currentTemperature = findViewById(R.id.current_temperature);
        outState.putInt("currentTemperature", extractCurrentTemperature(currentTemperature.getText().toString()));

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
