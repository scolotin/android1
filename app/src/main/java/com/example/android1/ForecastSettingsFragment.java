package com.example.android1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

public class ForecastSettingsFragment extends Fragment {
    boolean isLandscapeOrientation;
    private CheckBox windSpeed, humidity;
    private Bundle args;
    private Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        return inflater.inflate(R.layout.forecast_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        args = getArguments();
        if (args != null) {
            setForecastSettings();
        }

        createSnackbar(view);
        windSpeed.setOnClickListener(v -> snackbar.show());

        humidity.setOnClickListener(v -> snackbar.show());
    }

    private void snackbarAction() {
        if (args == null) {
            args = new Bundle();
        }
        getForecastSettings();

        Fragment fragment;
        if (isLandscapeOrientation) {
            fragment = new DetailedInfoFragment();
        }
        else {
            fragment = new CitiesFragment();
        }
        backToFragment(fragment, R.id.main_container);
    }

    private void createSnackbar(View view) {
        snackbar = Snackbar.make(view, R.string.request_to_apply, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_apply, v -> snackbarAction());
    }

    private void backToFragment(Fragment fragment, int containerViewId) {
        fragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.addToBackStack("");
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
