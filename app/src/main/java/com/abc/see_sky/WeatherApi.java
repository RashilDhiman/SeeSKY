package com.abc.see_sky;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    // Define the endpoint to fetch the weather data
    @GET("current.json")
    Call<WeatherResponse> getWeather(
            @Query("q") String city,        // City name
            @Query("key") String apiKey     // API key
    );
}
