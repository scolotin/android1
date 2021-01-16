package com.example.android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

public class AddCityActivity extends AppCompatActivity {
    private SearchView searchView;
    private String cityNameRequest;

    private static boolean isWindSpeedEnabled, isHumidityEnabled;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        Button btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> {
            searchView = findViewById(R.id.search);
            if (!searchView.getQuery().toString().isEmpty()) {
                cityNameRequest = searchView.getQuery().toString();
            }

            Intent cityParametersIntent = createCityParametersIntent();
            setResult(RESULT_OK, cityParametersIntent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_city_menu, menu);

        if (cityNameRequest != null && !cityNameRequest.isEmpty()) {
            MenuItem searchMenuItem = menu.findItem(R.id.search);
            searchView = searchMenuItem.getActionView().findViewById(R.id.search);
            searchView.setIconified(false);
            searchView.setQuery(cityNameRequest, true);
            searchView.clearFocus();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_city_settings) {
            Intent addCitySettingsIntent = new Intent(AddCityActivity.this, AddCitySettingsActivity.class);
            addCitySettingsIntent.putExtra("isWindSpeedEnabled", isWindSpeedEnabled);
            addCitySettingsIntent.putExtra("isHumidityEnabled", isHumidityEnabled);
            startActivityForResult(addCitySettingsIntent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            if (data != null) {
                isWindSpeedEnabled = data.getBooleanExtra("isWindSpeedEnabled", isWindSpeedEnabled);
                isHumidityEnabled = data.getBooleanExtra("isHumidityEnabled", isHumidityEnabled);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        searchView = findViewById(R.id.search);
        outState.putString("cityNameRequest", searchView.getQuery().toString());
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        cityNameRequest = savedInstanceState.getString("cityNameRequest");
    }

    public void onClick(View view) {
        TextView selectCity = (TextView) view;
        cityNameRequest = selectCity.getText().toString();

        Intent cityParametersIntent = createCityParametersIntent();
        setResult(RESULT_OK, cityParametersIntent);
        finish();
    }

    private Intent createCityParametersIntent() {
        Intent cityNameIntent = new Intent();
        cityNameIntent.putExtra("cityName", cityNameRequest);
        cityNameIntent.putExtra("isWindSpeedEnabled", isWindSpeedEnabled);
        cityNameIntent.putExtra("isHumidityEnabled", isHumidityEnabled);
        return cityNameIntent;
    }
}