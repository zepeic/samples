package com.example.openweather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class dailyViewHolder extends RecyclerView.ViewHolder {
    TextView today;
    TextView time;
    TextView temp;
    ImageView icon;
    TextView description;


    dailyViewHolder(@NonNull View view){
        super(view);
        today = view.findViewById(R.id.today);
        time = view.findViewById(R.id.timeText);
        temp = view.findViewById(R.id.tempText2);
        icon = view.findViewById(R.id.imageView2);
        description = view.findViewById(R.id.descriptionText2);

    }





}
