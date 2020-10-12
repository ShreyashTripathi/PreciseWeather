package com.shreyash.preciseweather.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.shreyash.preciseweather.R;
import com.shreyash.preciseweather.data.weather_data.WeatherResponse;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel mainActivityViewModel;
    String latitude,longitude;
    TextView temp_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(MainActivity.this,new MainActivityModelFactory()).get(MainActivityViewModel.class);
        initializeUI();
        getLocationCoordinates();
        MutableLiveData<WeatherResponse> response = mainActivityViewModel.getWeatherData(latitude,longitude);
        response.observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                if (weatherResponse != null) {
                    String w = "Country: " +
                            weatherResponse.getSys().getCountry() +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.getMain().getTemp() +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.getMain().getTempMin() +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.getMain().getTempMax() +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.getMain().getHumidity() +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.getMain().getPressure();

                            temp_tv.setText(w);
                   }
                else{
                    temp_tv.setText(R.string.no_data_available);
                }

            }
        });
    }

    private void initializeUI() {
        temp_tv = findViewById(R.id.temp_tv);
    }

    private void getLocationCoordinates() {
        latitude = "35";
        longitude = "139";
    }
}