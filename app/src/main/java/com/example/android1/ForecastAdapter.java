package com.example.android1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Forecast> forecasts;
    private final int resourceId;
    private final int typeForecast;

    public ForecastAdapter(Context context, List<Forecast> forecasts, int resourceId, int typeForecast) {
        this.resourceId = resourceId;
        this.inflater = LayoutInflater.from(context);
        this.forecasts = forecasts;
        this.typeForecast = typeForecast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resourceId, parent, false);
        return new ViewHolder(view, typeForecast);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        holder.iconImageView.setImageResource(forecast.getIcon());
        int valuesCount = holder.valuesTextViews.length;
        for (int i = 0; i < valuesCount; i++) {
            holder.valuesTextViews[i].setText(forecast.getValues()[i]);
        }
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconImageView;
        final TextView[] valuesTextViews;

        public ViewHolder(View view, int typeForecast) {
            super(view);
            iconImageView = view.findViewById(R.id.icon);
            valuesTextViews = new TextView[typeForecast];
            for (int i = 0; i < typeForecast; i++) {
                int resId = view.getResources().getIdentifier("item_" + i, "id", view.getContext().getPackageName());
                valuesTextViews[i] = view.findViewById(resId);
            }
        }
    }
}
