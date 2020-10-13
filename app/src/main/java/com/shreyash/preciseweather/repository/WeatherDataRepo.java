package com.shreyash.preciseweather.repository;

import android.util.Log;

import com.shreyash.preciseweather.data.network.OWMServiceBuilder;
import com.shreyash.preciseweather.data.network.WeatherService;
import com.shreyash.preciseweather.data.weather_data.WeatherResponse;
import com.shreyash.preciseweather.util.GetWeatherData;

import java.util.ArrayList;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherDataRepo {
    private static final String API_KEY = "43fe0ffee8d3dd5ecbb695fe3c767958";
    private static final String UNITS = "metric";
    private GetWeatherData getWeatherData;

    public WeatherDataRepo(GetWeatherData getWeatherData,String latitude, String longitude) {
        this.getWeatherData = getWeatherData;
        getWeatherData(latitude, longitude);
    }

    public WeatherDataRepo(GetWeatherData getWeatherData,String cityName){
        this.getWeatherData = getWeatherData;
        getWeatherData(cityName);
    }

    private void getWeatherData(String cityName) {
        OWMServiceBuilder serviceBuilder = OWMServiceBuilder.getInstance();
        WeatherService w_service = serviceBuilder.buildService();
        Observable<WeatherResponse> call = w_service.getCityCurrentWeatherData(cityName,API_KEY);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        if(weatherResponse != null){
                            Log.println(Log.DEBUG,"Repo","City data: "+weatherResponse.getMain().getTemp()+"\n");
                        }
                        getWeatherData.onGetWeatherData(weatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.println(Log.ERROR,"Repo CCW",e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Repo","OnComplete invoked");
                    }
                });
    }

    public void getWeatherData(String latitude, String longitude){
        OWMServiceBuilder service_builder = OWMServiceBuilder.getInstance();
        WeatherService w_service = service_builder.buildService();
        Observable<WeatherResponse> call = w_service.getCurrentWeatherData(latitude,longitude,API_KEY,UNITS);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        if(weatherResponse != null)
                            Log.println(Log.DEBUG,"Repo",weatherResponse.getMain().getTemp()+"\n");
                            getWeatherData.onGetWeatherData(weatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.println(Log.ERROR,"Repo",e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Repo","OnComplete invoked");

                    }
                });

    }
}
