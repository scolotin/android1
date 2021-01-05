package com.example.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private boolean isVisibleAddCityMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCity = findViewById(R.id.btn_add_city);
        btnAddCity.setOnClickListener(v -> {
            setContentView(R.layout.add_city);
            isVisibleAddCityMenu = true;
            invalidateOptionsMenu();
        });
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
                setContentView(R.layout.settings);
                return true;

            case R.id.add_city_settings:
                setContentView(R.layout.add_city_settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}