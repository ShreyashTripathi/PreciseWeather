package com.shreyash.preciseweather.util;

import com.shreyash.preciseweather.data.weather_data.WeatherResponse;

public interface GetWeatherData {
    void onGetWeatherData(WeatherResponse weatherResponse);
}
