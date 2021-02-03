package com.example.android1;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.res.Configuration;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscapeOrientation) {
            landscapeOrientationInit();
        }
        else {
            portraitOrientationInit();
        }

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (navigateFragment(id)){
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });

    }

    @SuppressLint("NonConstantResourceId")
    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_settings:
                ForecastSettingsFragment forecastSettingsFragment = new ForecastSettingsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, forecastSettingsFragment);
                transaction.addToBackStack("");
                transaction.commit();

                return true;
            case R.id.action_about:
                showAbout();
                return true;
        }
        return false;
    }

    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.about_app).setMessage(R.string.about_text);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void landscapeOrientationInit() {
        DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
        FragmentTransaction updateDetailedInfoTransaction = getSupportFragmentManager().beginTransaction();
        updateDetailedInfoTransaction.add(R.id.main_container, detailedInfoFragment);
        updateDetailedInfoTransaction.commit();

        CitiesFragment citiesFragment = new CitiesFragment();
        FragmentTransaction citiesTransaction = getSupportFragmentManager().beginTransaction();
        citiesTransaction.add(R.id.right_container, citiesFragment);
        citiesTransaction.commit();
    }

    private void portraitOrientationInit() {
        DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
        FragmentTransaction updateDetailedInfoTransaction = getSupportFragmentManager().beginTransaction();
        updateDetailedInfoTransaction.add(R.id.main_container, detailedInfoFragment);
        updateDetailedInfoTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
