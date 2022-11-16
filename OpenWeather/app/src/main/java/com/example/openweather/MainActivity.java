package com.example.openweather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean fahrenheit = true;
    private SharedPreferences.Editor editor;
    private String cities = "Chicago,US";
    private String locale;
    private double[] latlon;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView;
    private dailyViewAdapter mAdapter;
    private dailyForecastAdapter fAdapter;
    private dailyWeather w;
    private final ArrayList<dailyWeather> dailyWeathers = new ArrayList<>();
    private final ArrayList<dailyForecasts> forecasts = new ArrayList<>();
    private boolean flag =fahrenheit;
    private ActivityResultLauncher<Intent> activityResultLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(hasNetworkConnection() == true) {

            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleResult);

            cities = cities.trim().replaceAll(", ", ",");


            locale = getLocationName(cities);
            latlon = getLatLon(locale);


            WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(this, latlon, fahrenheit);
            Thread t = new Thread(loaderTaskRunnable);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }





            recyclerView = findViewById(R.id.recycler);
            mAdapter = new dailyViewAdapter(dailyWeathers, this, fahrenheit);

            recyclerView.setAdapter(mAdapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(this
                    , LinearLayoutManager.HORIZONTAL, false));

            recyclerView = findViewById((R.id.recycler));


          doDownload();

        }
        if(hasNetworkConnection()==false){
            TextView city = findViewById(R.id.cityText);
            city.setText("No Internet Connection");
            ImageView image = findViewById(R.id.weatherImage);
            image.setImageIcon(null);


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(hasNetworkConnection()) {
            getMenuInflater().inflate(R.menu.menu, menu);
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            if (!sharedPref.contains("FAHRENHEIT")) {
                editor.putBoolean("FAHRENHEIT", true);
                editor.apply();
            }
        }
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(hasNetworkConnection() ==false) {
        }
        else {
            if (item.getItemId() == R.id.location) {
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);
                builder.setNegativeButton("Cancel", (dialog, id) -> {

                });
                builder.setPositiveButton("Ok", (dialog, id) -> {
                    String cityName = et.getText().toString().trim().replaceAll(", ", ",");
                    cityName = getLocationName(cityName);
                    latlon = getLatLon(cityName);
                    Log.d(TAG, "Complete Download2: Loading JSON File from" + dailyWeathers.size());
                    cities = cityName;
                    dailyWeathers.clear();
                    doDownload();
                    mAdapter = new dailyViewAdapter(dailyWeathers, this, fahrenheit);
                    recyclerView.setAdapter(mAdapter);
                    Log.d(TAG, "Complete Download3: Loading JSON File from" + dailyWeathers.size());

                });
                builder.setMessage("For US locations, enter as 'City', or 'City,State'\n\nFor international locations enter as 'City,Country'");
                builder.setTitle("Enter a Location");
                AlertDialog dialog = builder.create();
                dialog.show();

            } else if (item.getItemId() == R.id.fahrenheitButton) {
                if (fahrenheit) {
                    item.setIcon(R.drawable.units_c);
                    fahrenheit = false;

                    doDownload();

                    mAdapter = new dailyViewAdapter(dailyWeathers, this, fahrenheit);
                    recyclerView.setAdapter(mAdapter);

                    return true;

                }
                if (fahrenheit == false) {
                    item.setIcon(R.drawable.units_f);
                    fahrenheit = true;

                    doDownload();

                    mAdapter = new dailyViewAdapter(dailyWeathers, this, fahrenheit);

                    recyclerView.setAdapter(mAdapter);

                    return true;
                }
                return true;

            } else if (item.getItemId() == R.id.sevenDay) {

                Intent intent = new Intent(MainActivity.this, dailyForecast.class);

                doDownload();
                if (forecasts != null)
                    intent.putExtra("theForecast", forecasts);
                intent.putExtra("fahrenheit", fahrenheit);
                intent.putExtra("city", cities);
                MainActivity.this.startActivity(intent);


                return true;


            }


        }
        return super.onOptionsItemSelected(item);
    }





    public void handleError(String s) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Data Problem")
                .setMessage(s)
                .setPositiveButton("OK", (dialogInterface, i) -> {})
                .create().show();




    }

    private void doDownload() {

        String cityName = cities.trim().replaceAll(", ", ",");
        cities = getLocationName(cityName);
        latlon = getLatLon(cities);

        WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(this, latlon, fahrenheit);
        Thread t  = new Thread(loaderTaskRunnable);
        t.start();
        try{
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void updateData(mainWeather weather) {
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView city = findViewById(R.id.cityText);
        city.setText(cities);

        TextView temp = findViewById(R.id.tempText);
        temp.setText(String.format("%.0f°" + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getTemp())));

        TextView date = findViewById(R.id.dateText);
        date.setText(weather.getDate());

        TextView feelsLike = findViewById(R.id.feelsLikeText);
        feelsLike.setText(String.format("Feels like %02.0f° "+ (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getFeelsLike())));

        TextView description = findViewById(R.id.descriptionText);
        description.setText(String.format("%s (%s)", weather.getDescription(),weather.getCondition()));

        TextView wind = findViewById(R.id.windsText);
        wind.setText(String.format("Winds: " + weather.getWind() + " "  + (fahrenheit ? "mph" : "mps") ));

        TextView sunset = findViewById(R.id.sunsetText);
        sunset.setText(String.format("Sunset: %s",weather.getSunset()));

        TextView sunrise = findViewById(R.id.sunriseText);
        sunrise.setText(String.format("Sunrise: %s",weather.getSunrise()));

        TextView uvIndex = findViewById(R.id.uvIndexText);
        uvIndex.setText(String.format("UV Index: %s",weather.getUvIndex()));

        TextView humidity = findViewById(R.id.humidityText);
        humidity.setText(String.format("Humidity: %02.0f"  ,Double.parseDouble(weather.getHumidity()))+"%");

        TextView visibility = findViewById(R.id.visibilityText);
        visibility.setText(String.format("Visibility: %.0f mi",(Double.parseDouble(weather.getVisibility()))*.000621));

        ImageView image = findViewById(R.id.weatherImage);
        String iconCode = "_" + weather.getIcon();
        int iconResId = image.getResources().getIdentifier(iconCode,"drawable",getPackageName());
        image.setImageResource(iconResId);

        TextView temp3 = findViewById(R.id.tempText3);
        temp3.setText(weather.getTempAtTime1());

        TextView temp4 = findViewById(R.id.tempText4);
        temp4.setText(weather.getTempAtTime2());

        TextView temp5 = findViewById(R.id.tempText5);
        temp5.setText(weather.getTempAtTime3());

        TextView temp6 = findViewById(R.id.tempText6);
        temp6.setText(weather.getTempAtTime4());

        TextView time1 = findViewById(R.id.textView);
        time1.setText("8am");
        TextView time2 = findViewById(R.id.textView5);
        time2.setText("1pm");
        TextView time3 = findViewById(R.id.textView6);
        time3.setText("5pm");
        TextView time4 = findViewById(R.id.textView7);
        time4.setText("11pm");
    }
    public void updateData2(ArrayList<dailyWeather> weather) {
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        dailyWeathers.clear();
        dailyWeathers.addAll(weather);
        mAdapter = new dailyViewAdapter(dailyWeathers, this ,fahrenheit);
        recyclerView.setAdapter(mAdapter);

    }

    public void updateData3(ArrayList<dailyForecasts> weather){
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
            forecasts.clear();

            forecasts.addAll(weather);


    }
    private void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {

            return;
        }
    }
    private String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            locale = p1 + ", " + p2;
            return locale;
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            double lat = address.get(0).getLatitude();
            double lon = address.get(0).getLongitude();

            return new double[]{lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }


    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }


    }




