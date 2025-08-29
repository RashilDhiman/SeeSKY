package com.abc.see_sky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    private static final String API_KEY = "cbadc2671a61479ba4d44526252701";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private TextView locationTextView, temperatureTextView, weatherConditionTextView, humidityTextView, windSpeedTextView;
    private ImageView weatherIcon;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Views
        locationTextView = findViewById(R.id.locationTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        weatherIcon = findViewById(R.id.weatherIcon);
        TextView viewMoreText = findViewById(R.id.viewMoreText);

        // Setup FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check and Request Location Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationAndWeather();
        }

        viewMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the Forecast page
                Intent intent = new Intent(Home.this, Forecast.class);

                // Start the new activity
                startActivity(intent);
            }
        });
    }


    private void getLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Get Last Known Location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                fetchWeatherData(location.getLatitude(), location.getLongitude());
            } else {
                locationTextView.setText("Location not available.");
            }
        });
    }

    private void fetchWeatherData(double latitude, double longitude) {
        // Retrofit setup for API request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        String locationQuery = latitude + "," + longitude;

        // Fetch weather data using Retrofit
        weatherApi.getWeather(locationQuery, API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        // Get weather condition (sunny, cloudy, etc.)
                        String condition = weatherResponse.getCurrent().getCondition().getText();

                        // Set dynamic background based on the condition
                        setDynamicBackground(condition);

                        // Update UI with weather data
                        locationTextView.setText(weatherResponse.getLocation().getName());
                        temperatureTextView.setText(weatherResponse.getCurrent().getTempC() + "°C");
                        weatherConditionTextView.setText(condition);
                        humidityTextView.setText("Humidity: " + weatherResponse.getCurrent().getHumidity() + "%");
                        windSpeedTextView.setText("Wind: " + weatherResponse.getCurrent().getWindMph() + " mph");

                        // Load weather icon using Picasso
                        Picasso.get().load("https:" + weatherResponse.getCurrent().getCondition().getIcon())
                                .into(weatherIcon);
                    }
                } else {
                    locationTextView.setText("Failed to retrieve weather data.");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                locationTextView.setText("Error: " + t.getMessage());
            }
        });
    }

    private void setDynamicBackground(String weatherCondition) {
        ConstraintLayout rootLayout = findViewById(R.id.rootLayout); // Replace with your root layout ID
        switch (weatherCondition.toLowerCase()) {
            case "sunny":
                rootLayout.setBackgroundResource(R.drawable.background_sunny);
                break;
            case "cloudy":
                rootLayout.setBackgroundResource(R.drawable.background_cloudy);
                break;
            case "rainy":
                rootLayout.setBackgroundResource(R.drawable.background_rainy);
                break;
            case "night":
                rootLayout.setBackgroundResource(R.drawable.background_night);
                break;
            default:
                rootLayout.setBackgroundResource(R.drawable.background_gradient); // Default background
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndWeather();
            } else {
                locationTextView.setText("Permission denied. Cannot access location.");
            }
        }
    }

}
