package com.example.openweather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class dailyForecastHolder extends RecyclerView.ViewHolder {
    TextView date;
    TextView tempmaxmin;
    TextView description;
    TextView condition;
    TextView uvIndex;
    TextView tempTime1;
    TextView tempTime2;
    TextView tempTime3;
    TextView tempTime4;
    ImageView icon;
    TextView cityName;

    dailyForecastHolder(@NonNull View view) {
        super(view);
        date = view.findViewById(R.id.dateText2);
        tempmaxmin = view.findViewById(R.id.tempMinMax);
        description = view.findViewById(R.id.descriptionText3);
        condition = view.findViewById(R.id.conditionText2);
        uvIndex = view.findViewById(R.id.uvIndexText2);
        tempTime1 = view.findViewById(R.id.textView8);
        tempTime2 = view.findViewById(R.id.textView9);
        tempTime3 = view.findViewById(R.id.textView10);
        tempTime4 = view.findViewById(R.id.textView11);
        icon = view.findViewById(R.id.imageView4);


    }


}


