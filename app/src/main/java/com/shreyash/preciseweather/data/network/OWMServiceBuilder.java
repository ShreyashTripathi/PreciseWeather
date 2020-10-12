package com.shreyash.preciseweather.data.network;

import com.google.gson.Gson;

import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class OWMServiceBuilder {
    private static OWMServiceBuilder serviceBuilder;
    private OWMServiceBuilder() {
    }

    public static OWMServiceBuilder getInstance(){
        if(serviceBuilder == null){
            serviceBuilder = new OWMServiceBuilder();
        }
        return serviceBuilder;
    }

    public WeatherService buildService(){
        String baseUrl = "https://api.openweathermap.org/";
        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(rxAdapter)
                .build();
        return retrofit.create(WeatherService.class);
    }
}


