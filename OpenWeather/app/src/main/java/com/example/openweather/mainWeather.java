package com.example.openweather;

import android.graphics.Bitmap;

public class mainWeather {



    private final String temp;
    private final String date;
    private final String description;
    private final String condition;
    private final String uvIndex;
    private final String humidity;
    private final String visibility;
    private final String wind;
    private final String tempAtTime1;
    private final String tempAtTime2;
    private final String tempAtTime3;
    private final String tempAtTime4;


    private final String sunset;
    private final String sunrise;
    private final String feelsLike;
    private final String icon;


    mainWeather( String date, String sunrise ,String sunset,
                String temp, String feelsLike , String humidity , String UvIndex, String wind, String visibility,
                String condition , String description ,String icon,String tempAtTime1,String tempAtTime2, String tempAtTime3,String tempAtTime4) {

        this.uvIndex = UvIndex;
        this.description = description;
        this.temp = temp;
        this.feelsLike =feelsLike;
        this.humidity = humidity;
        this.wind = wind;
        this.date = date;
        //this.bitmap = bitmap;
        this.visibility = visibility;
        this.tempAtTime1 = tempAtTime1;
        this.tempAtTime2 = tempAtTime2;
        this.tempAtTime3 = tempAtTime3;
        this.tempAtTime4 = tempAtTime4;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.condition = condition;
        this.icon = icon;
    }




    String getUvIndex() {
        return uvIndex;
    }

    String getDescription() {
        return description;
    }

    String getTemp() {
        return temp;
    }

    String getHumidity() {
        return humidity;
    }

    String getWind() {
        return wind;

    }

    String getDate() {
        return date;
    }

  //  Bitmap getBitmap() {
    //    return bitmap;
    //}


    String getVisibility() {
        return visibility;
    }


    String getTempAtTime1() {
        return tempAtTime1;
    }


    String getTempAtTime2() {
        return tempAtTime2;
    }

    String getTempAtTime3() {
        return tempAtTime3;
    }
    String getTempAtTime4() {
        return tempAtTime4;
    }

    String getSunset(){
        return sunset;
    }
    String getSunrise(){
        return sunrise;
    }
    String getFeelsLike() {
        return feelsLike;
    }
    String getCondition() {
        return condition;
    }
    String getIcon() {return icon;}



}



