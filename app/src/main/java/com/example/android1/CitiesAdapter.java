package com.example.android1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {
    private final String[] citiesName;
    private OnItemClickListener itemClickListener;

    public CitiesAdapter(String[] citiesName) {
        this.citiesName = citiesName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cities_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(citiesName[position]);
    }

    @Override
    public int getItemCount() {
        return citiesName.length;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;

            textView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
