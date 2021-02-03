package com.example.android1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CitiesFragment extends Fragment {
    private String requestedCityName;
    private boolean isWindSpeedEnabled, isHumidityEnabled;
    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            setHasOptionsMenu(true);
        }
        return inflater.inflate(R.layout.cities_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        args = getArguments();
        if (args != null) {
            getForecastSettings();
        }
        else {
            args = new Bundle();
            putForecastSettings();
        }

        RecyclerView citiesList = view.findViewById(R.id.cities_list);
        citiesList.setHasFixedSize(true);
        String[] cities = getResources().getStringArray(R.array.cities);
        CitiesAdapter citiesListAdapter = new CitiesAdapter(cities);
        citiesList.setAdapter(citiesListAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.separator));
        citiesList.addItemDecoration(itemDecoration);

        citiesListAdapter.setItemClickListener((v, position) -> {
            requestedCityName = ((TextView) v).getText().toString();
            args.putString("cityName", requestedCityName);
            DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
            backToFragment(detailedInfoFragment);
        });
    }

    private void putForecastSettings() {
        args.putBoolean("isWindSpeedEnabled", isWindSpeedEnabled);
        args.putBoolean("isHumidityEnabled", isHumidityEnabled);
    }

    private void getForecastSettings() {
        isWindSpeedEnabled = args.getBoolean("isWindSpeedEnabled");
        isHumidityEnabled = args.getBoolean("isHumidityEnabled");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_city_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.search):
                CitiesDialogBuilderFragment citiesDBF = new CitiesDialogBuilderFragment();
                citiesDBF.show(getChildFragmentManager(), null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDialogResult(String resultDialog) {
        requestedCityName = resultDialog;

        args.putString("cityName", requestedCityName);
        DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
        backToFragment(detailedInfoFragment);
    }

    private void backToFragment(Fragment fragment) {
        fragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }
}
