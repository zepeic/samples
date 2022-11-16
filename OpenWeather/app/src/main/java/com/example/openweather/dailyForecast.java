package com.example.openweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class dailyForecast extends AppCompatActivity implements Serializable {
    private RecyclerView recyclerView;
    private dailyForecastAdapter mAdapter;
    private ArrayList<dailyForecasts> forecasts = new ArrayList<>();
    private boolean fahrenheit;
    private MainActivity daily;
    private String cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.daily_forecast);
        Intent intent = getIntent();;
        ArrayList<dailyForecasts> temp = (ArrayList<dailyForecasts>) intent.getSerializableExtra("theForecast");
        fahrenheit = (boolean) intent.getSerializableExtra("fahrenheit");
        cities = (String) intent.getSerializableExtra("city");
        setTitle(cities);

        forecasts = temp;
        recyclerView = findViewById(R.id.recycler2);
        mAdapter = new dailyForecastAdapter(forecasts,this,fahrenheit);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));

        recyclerView = findViewById((R.id.recycler2));








    }
}
