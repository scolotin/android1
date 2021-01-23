package com.example.android1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ForecastSettingsFragment extends Fragment {
    private CheckBox windSpeed, humidity;
    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forecast_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        findViews(view);

        args = getArguments();
        if (args != null) {
            setForecastSettings();
        }

        Button btnApply = view.findViewById(R.id.btn_apply);
        if (btnApply != null) {
            btnApply.setOnClickListener(v -> {
                getForecastSettings();

                Fragment fragment;
                if (isLandscapeOrientation) {
                    fragment = new DetailedInfoFragment();
                }
                else {
                    fragment = new CitiesFragment();
                }
                backToFragment(fragment, R.id.main_container);
            });
        }
    }

    private void backToFragment(Fragment fragment, int containerViewId) {
        fragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }

    private void setForecastSettings() {
        windSpeed.setChecked(args.getBoolean("isWindSpeedEnabled"));
        humidity.setChecked(args.getBoolean("isHumidityEnabled"));
    }

    private void getForecastSettings() {
        args.putBoolean("isWindSpeedEnabled", windSpeed.isChecked());
        args.putBoolean("isHumidityEnabled", humidity.isChecked());
    }

    private void findViews(View view) {
        windSpeed = view.findViewById(R.id.wind_speed);
        humidity = view.findViewById(R.id.humidity);
    }
}
