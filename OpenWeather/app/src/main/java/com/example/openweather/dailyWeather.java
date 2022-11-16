package com.example.openweather;

public class dailyWeather {
    private final String today;
    private final String temp2;
    private final String time;
    private final String description;
    private final String icon;


    dailyWeather(String today, String time, String temp2, String description, String icon){
        this.temp2 = temp2;
        this.time = time;
        this.today = today;
        this.description = description;
        this.icon = icon;
    }

    public String getTemp() {
        return temp2;
    }
    public String getTime(){
        return time;

    }
    public String getToday() {
        return today;
    }
    public String getDescription(){
        return description;
    }
    public String getIcon(){
        return icon;
    }
}

