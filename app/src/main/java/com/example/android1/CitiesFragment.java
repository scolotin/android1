package com.example.android1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
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
    private boolean isLandscapeOrientation;
    private String requestedCityName;
    private SearchView searchView;
    private boolean isWindSpeedEnabled, isHumidityEnabled;
    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState == null) {
            setHasOptionsMenu(true);
        }
        return inflater.inflate(R.layout.cities_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isLandscapeOrientation) {
            hideButton();
        }

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
            if (!isLandscapeOrientation) {
                setToSearchView(requestedCityName);
            }
            else {
                args.putString("cityName", requestedCityName);
                DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
                backToFragment(detailedInfoFragment, R.id.main_container);
            }
        });

        Button btnApply = view.findViewById(R.id.btn_apply);
        if (btnApply != null && !isLandscapeOrientation) {
            btnApply.setOnClickListener(v -> {
                updateRequestedCityName();
                args.putString("cityName", requestedCityName);
                DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
                backToFragment(detailedInfoFragment, R.id.main_container);
            });
        }
    }

    private void setToSearchView(String text) {
        searchView.setIconified(false);
        searchView.setQuery(text, true);
        searchView.clearFocus();
    }

    private void updateRequestedCityName() {
        if (!searchView.getQuery().toString().isEmpty()) {
            requestedCityName = searchView.getQuery().toString();
        }
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

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = searchItem.getActionView().findViewById(R.id.search);
        if (isLandscapeOrientation) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    args.putString("cityName", query);
                    DetailedInfoFragment detailedInfoFragment = new DetailedInfoFragment();
                    backToFragment(detailedInfoFragment, R.id.main_container);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_city_settings) {
            ForecastSettingsFragment forecastSettingsFragment = new ForecastSettingsFragment();
            backToFragment(forecastSettingsFragment, R.id.main_container);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideButton() {
        Button btnApply = getView().findViewById(R.id.btn_apply);
        btnApply.setVisibility(View.GONE);
    }

    private void backToFragment(Fragment fragment, int containerViewId) {
        fragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }
}
