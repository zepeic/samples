package com.example.openweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class dailyForecastAdapter extends RecyclerView.Adapter<dailyForecastHolder> {

    private ArrayList<dailyForecasts> forecasts;
    private dailyForecast mainActivity;
    private boolean fahrenheit;

    dailyForecastAdapter(ArrayList<dailyForecasts> forecasts, dailyForecast mainActivity , boolean fahrenheit){
        this.forecasts = forecasts;
        this.fahrenheit = fahrenheit;
        this.mainActivity = mainActivity;
    }



    @NonNull
    @Override
    public dailyForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_forecast_row,parent,false);



        return new dailyForecastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dailyForecastHolder holder, int position) {
        dailyForecasts d = forecasts.get(position);
        holder.date.setText(d.getDate());
        holder.tempmaxmin.setText(d.getTempmaxmin());
        holder.description.setText(d.getDescription());
        holder.condition.setText(d.getCondition());
        holder.uvIndex.setText(d.getUvIndex());
        holder.tempTime1.setText(d.getTemptime1());
        holder.tempTime2.setText(d.getTemptime2());
        holder.tempTime3.setText(d.getTemptime3());
        holder.tempTime4.setText(d.getTemptime4());
        String iconCode = "_" + d.getIcon();
        int iconResId = holder.icon.getResources().getIdentifier(iconCode,"drawable", mainActivity.getPackageName());
        holder.icon.setImageResource(iconResId);

    }


    @Override
    public int getItemCount() {
        return forecasts.size();
    }



}
