package com.example.openweather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDownloadRunnable implements Runnable{
    private final MainActivity mainActivity;
    private final boolean fahrenheit;
    private final double lat;
    private final double lon;


    //https://api.openweathermap.org/data/2.5/onecall?lat=#########&lon=#########
    private static final String weatherURL =  "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";
    private static final String iconUrl = "https://openweathermap.org/img/w/";

    WeatherDownloadRunnable(MainActivity mainActivity,double[] latlon, boolean fahrenheit) {
        this.mainActivity = mainActivity;
        this.lat= latlon[0];
        this.lon = latlon[1];
        this.fahrenheit = fahrenheit;
    }


    @Override
    public void run() {

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("lat", String.valueOf(lat));
        buildURL.appendQueryParameter("lon", String.valueOf(lon));
        buildURL.appendQueryParameter("units", (fahrenheit ? "imperial" : "metric"));
        buildURL.appendQueryParameter("appid", yourAPIKey);

        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                InputStream is = connection.getErrorStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                handleError(sb.toString());
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }



        } catch (Exception e) {

            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    public void handleError(String s) {
        String msg = "Error: ";
        try {
            JSONObject jObjMain = new JSONObject(s);
            msg += jObjMain.getString("message");

        } catch (JSONException e) {
            msg += e.getMessage();
        }

        String finalMsg = String.format("%s (%s)", msg, lat,lon);
        mainActivity.runOnUiThread(() -> mainActivity.handleError(finalMsg));
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    private mainWeather parseJSON(String s) {

        try {
            String tempAtTime1 = null;
            String tempAtTime2 = null;
            String tempAtTime3 = null;
            String tempAtTime4 = null;

            JSONObject jObjMain = new JSONObject(s);

            //Current
            JSONObject jCurrent = jObjMain.getJSONObject("current");

            // time section
            long dt = jCurrent.getLong("dt");
            String date = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(new Date(dt * 1000));

            long sunrise1 = jCurrent.getLong("sunrise");
            String sunrise = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(sunrise1 * 1000));

            long sunset2 = jCurrent.getLong("dt");
            String sunset = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(sunset2 * 1000));

            //Temp
            String temp = jCurrent.getString("temp")  ;
            String feelsLike = jCurrent.getString("feels_like");

            //humidity
            String humidity = jCurrent.getString("humidity");

            //uvi
            String UvIndex = jCurrent.getString("uvi");

            //Wind
            String windSpeed = jCurrent.getString("wind_speed");
            Double wind_degree = Double.parseDouble(jCurrent.getString("wind_deg"));
            String windDegree = getDirection(wind_degree);
            String wind = windDegree + " at " + windSpeed;

            //Visibility

            String visibility = jCurrent.getString("visibility");

            //Weather
            JSONArray weather = jCurrent.getJSONArray("weather");
            JSONObject jWeather = (JSONObject) weather.get(0);
            String condition = jWeather.getString("main");
            String description = jWeather.getString("description");
            String icon = jWeather.getString("icon");

            JSONArray hourly = jObjMain.getJSONArray("hourly");
            String timezone_offset = jObjMain.getString("timezone_offset");

            for(int i = 0 ; i < 40; i++) {
                JSONObject jHourly = (JSONObject) hourly.get(i);
                String dt2 = jHourly.getString("dt");
                Long time_offset = Long.parseLong(timezone_offset);
                Long datetime = Long.parseLong(dt2);
                LocalDateTime ldt =
                        LocalDateTime.ofEpochSecond(datetime+time_offset,0, ZoneOffset.UTC);
                DateTimeFormatter dtf =
                        DateTimeFormatter.ofPattern("hh:ss a",Locale.getDefault());
                String time = ldt.format(dtf);
                if(time.equals("08:00 AM")){
                    tempAtTime1 = jHourly.getString("temp");
                    tempAtTime1 = String.format("%.0f °",Double.parseDouble(tempAtTime1))+(fahrenheit ? "F" : "C");
                }
                if(time.equals("01:00 PM")){
                    tempAtTime2 = jHourly.getString("temp");
                    tempAtTime2 = String.format("%.0f °",Double.parseDouble(tempAtTime2))+(fahrenheit ? "F" : "C");
                }
                if(time.equals("05:00 PM")){
                    tempAtTime3 = jHourly.getString("temp");
                    tempAtTime3 = String.format("%.0f °",Double.parseDouble(tempAtTime3))+(fahrenheit ? "F" : "C");
                }
                if(time.equals("11:00 PM")){
                    tempAtTime4 = jHourly.getString("temp");
                    tempAtTime4 = String.format("%.0f °",Double.parseDouble(tempAtTime4))+(fahrenheit ? "F" : "C");
                }

            }








            return new mainWeather(date, sunrise , sunset, temp , feelsLike , humidity ,UvIndex,wind ,visibility ,condition,description,icon ,tempAtTime1,
                    tempAtTime2,tempAtTime3,tempAtTime4) ;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<dailyWeather> parseJSONArray(String s){
        String today;
        ArrayList<dailyWeather> dailyWeathers = new ArrayList<>();
        try{
            JSONObject jObjMain = new JSONObject(s);
            JSONArray hourly = jObjMain.getJSONArray("hourly");
            String timezone_offset = jObjMain.getString("timezone_offset");

            for(int i = 0 ; i < 48; i++){

                JSONObject jHourly = (JSONObject) hourly.get(i);

                String temp2 = jHourly.getString("temp");
                temp2 = String.format("%.0f °",Double.parseDouble(temp2))+(fahrenheit ? "F" : "C");
                String dt = jHourly.getString("dt");
                Long time_offset = Long.parseLong(timezone_offset);
                Long datetime = Long.parseLong(dt);
                LocalDateTime ldt =
                        LocalDateTime.ofEpochSecond(datetime+time_offset,0, ZoneOffset.UTC);
                DateTimeFormatter dtf =
                        DateTimeFormatter.ofPattern("hh:mm a",Locale.getDefault());
                String time = ldt.format(dtf);


                JSONArray hWeather = jHourly.getJSONArray("weather");
                JSONObject  hW= (JSONObject) hWeather.get(0);
                String description = hW.getString("description");
                String icon = hW.getString("icon");





                if (i == 0){
                    today = "Today";

                }
                else {
                    dtf = DateTimeFormatter.ofPattern("EEEE",Locale.getDefault());
                    today = ldt.format(dtf);
                }
                dailyWeathers.add(new dailyWeather(today,time,temp2,description,icon));
                }
            return dailyWeathers;





        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }

    private ArrayList<dailyForecasts> parseJSONDaily(String s){

        String today;
        ArrayList<dailyForecasts> forecasts = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray daily = jObjMain.getJSONArray("daily");
            String timezone_offset = jObjMain.getString("timezone_offset");
            for (int i = 0; i < 7; i++) {
                JSONObject jDaily = (JSONObject) daily.get(i);
                //time
                long dt = jDaily.getLong("dt");
                String date = new SimpleDateFormat("EEEE,MM/dd", Locale.getDefault()).format(new Date(dt * 1000));


                //temp
                JSONObject jTemp = jDaily.getJSONObject("temp");

                String tempMin = jTemp.getString("min");
                double tm = Double.parseDouble(tempMin);
                String tempMax = jTemp.getString("max");
                double tM = Double.parseDouble(tempMax);
                String temporary = (fahrenheit ? "F" : "C");
                String tempMinMax = String.format("%.0f°%s/%.0f°%s",tm,temporary,tM,temporary);



                String tempTime1 =  jTemp.getString("morn");

                double tt1 = Double.parseDouble(tempTime1);

                tempTime1 =  String.format("%.0f°",tt1) + (fahrenheit ? "F" : "C");

                String tempTime2 = jTemp.getString("day");

                double tt2 = Double.parseDouble(tempTime2);

                tempTime2 =  String.format("%.0f°",tt2) + (fahrenheit ? "F" : "C");

                String tempTime3 = jTemp.getString("eve");

                double tt3 = Double.parseDouble(tempTime3);

                tempTime3 =  String.format("%.0f°",tt3) + (fahrenheit ? "F" : "C");


                String tempTime4 = jTemp.getString("night");

                double tt4 = Double.parseDouble(tempTime4);

                tempTime4 =  String.format("%.0f°",tt4) + (fahrenheit ? "F" : "C");

                //Weather
                JSONArray jWeather = jDaily.getJSONArray("weather");
                JSONObject jW = (JSONObject) jWeather.get(0);
                String description = jW.getString("description");
                String icon = jW.getString("icon");

                //Condition
                String condition = jDaily.getString("pop");
                condition = "("+condition+"% precip.)";

                //uvi
                String uvIndex = jDaily.getString("uvi");
                uvIndex = "UV Index: "+ uvIndex;

                forecasts.add(new dailyForecasts(date, tempMinMax, description, condition, uvIndex, tempTime1, tempTime2, tempTime3, tempTime4, icon));

            }
            return forecasts;



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }
    public void handleResults(final String jsonString) {

        final mainWeather w = parseJSON(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData(w));
        final ArrayList<dailyWeather> w1 = parseJSONArray(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData2(w1));
        final ArrayList<dailyForecasts> w2 = parseJSONDaily(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData3(w2));


    }





}
