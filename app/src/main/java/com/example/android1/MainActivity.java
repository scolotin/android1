package com.example.android1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;

import android.os.Bundle;

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
}
