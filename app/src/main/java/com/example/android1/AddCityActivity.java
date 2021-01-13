package com.example.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;

public class AddCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        Button btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> {
            SearchView searchView = findViewById(R.id.search);
            Intent cityNameIntent = new Intent();
            cityNameIntent.putExtra("cityName", searchView.getQuery().toString());
            setResult(RESULT_OK, cityNameIntent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_city_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_city_settings) {
            Intent addCitySettingsIntent = new Intent(AddCityActivity.this, AddCitySettingsActivity.class);
            addCitySettingsIntent.putExtra("citySettings", getIntent().getExtras().getSerializable("citySettings"));
            startActivity(addCitySettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}