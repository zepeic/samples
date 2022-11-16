package com.example.openweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class dailyViewAdapter extends RecyclerView.Adapter<dailyViewHolder> {

    private List<dailyWeather> dailyWeathers;
    private MainActivity mainActivity;
    private boolean fahrenheit;

    dailyViewAdapter(List<dailyWeather> dailyWeathers, MainActivity mainActivity, boolean fahrenheit){
        this.dailyWeathers = dailyWeathers;
        this.mainActivity = mainActivity;
        this.fahrenheit = fahrenheit;


    }

    @NonNull
    @Override
    public dailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_weather_row,parent,false);



        return new dailyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull dailyViewHolder holder, int position ) {

        dailyWeather w = dailyWeathers.get(position);
        holder.temp.setText(w.getTemp());
        holder.description.setText(w.getDescription());
        holder.time.setText(w.getTime());
        holder.today.setText(w.getToday());
        String iconCode = "_" + w.getIcon();
        int iconResId = holder.icon.getResources().getIdentifier(iconCode,"drawable", mainActivity.getPackageName());
        holder.icon.setImageResource(iconResId);




    }
    @Override
    public int getItemCount() {
        return dailyWeathers.size();
    }
}