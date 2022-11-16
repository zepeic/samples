package com.example.openweather;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class dailyForecasts implements Serializable {
    private final String date;
    private final String tempmaxmin;
    private final String description;
    private final String condition;
    private final String uvIndex;
    private final String temptime1;
    private final String temptime2;
    private final String temptime3;
    private final String temptime4;
    private final String icon;

    dailyForecasts(String date,String tempmaxmin,String description,String condition,String uvIndex,String temptime1,String temptime2,String temptime3,String temptime4,String icon){
        this.date=  date;
        this.tempmaxmin = tempmaxmin;
        this.description = description;
        this.condition = condition;
        this.uvIndex = uvIndex;
        this.temptime1 = temptime1;
        this.temptime2  = temptime2;
        this.temptime3  = temptime3;
        this.temptime4 = temptime4;
        this.icon = icon;

    }

    public String getDate() {
        return date;

    }

    public String getTempmaxmin() {
        return tempmaxmin;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public String getTemptime1() {
        return temptime1;
    }

    public String getTemptime2() {
        return temptime2;
    }

    public String getTemptime3() {
        return temptime3;
    }
    public String getTemptime4() {
        return temptime4;
    }

    public String getIcon() {
        return icon;
    }
}

