package com.shreyash.preciseweather.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shreyash.preciseweather.data.weather_data.WeatherResponse;
import com.shreyash.preciseweather.repository.WeatherDataRepo;
import com.shreyash.preciseweather.util.GetWeatherData;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<WeatherResponse> weatherResponseLiveData;

    public MainActivityViewModel() {
        weatherResponseLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<WeatherResponse> getWeatherData(String latitude, String longitude){
        WeatherDataRepo repo = new WeatherDataRepo(new GetWeatherData() {
            @Override
            public void onGetWeatherData(WeatherResponse weatherResponse) {
                if(weatherResponse != null && weatherResponse.getMain() != null)
                    Log.d("VM_",weatherResponse.getMain().getTemp()+"");
                weatherResponseLiveData.postValue(weatherResponse);
            }
        },latitude,longitude);

        return weatherResponseLiveData;
    }
}
