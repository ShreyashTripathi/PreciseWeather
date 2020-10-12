package com.shreyash.preciseweather.data.network;

import com.shreyash.preciseweather.data.weather_data.WeatherResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherService {
    @GET("data/2.5/weather?")
    Observable<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String app_id);
}
