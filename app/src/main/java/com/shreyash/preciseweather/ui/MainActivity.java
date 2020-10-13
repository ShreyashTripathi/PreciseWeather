package com.shreyash.preciseweather.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shreyash.preciseweather.R;
import com.shreyash.preciseweather.data.weather_data.Weather;
import com.shreyash.preciseweather.data.weather_data.WeatherResponse;
import com.shreyash.preciseweather.location.FetchAddressTask;
import com.squareup.picasso.Picasso;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {

    private static final int REQUEST_LOCATION_PERMISSION = 101;
    private MainActivityViewModel mainActivityViewModel;
    private String latitude,longitude;
    private TextView temp_tv,date_time_tv,max_temp_tv,min_temp_tv,feels_like_tv,weather_tv,current_location_tv;
    private ImageView weather_iv,location_pin_iv;
    private boolean mTrackingLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private TextView humidity_tv,pressure_tv,speed_tv,sunrise_tv,sunset_tv;
    //private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(MainActivity.this,new MainActivityModelFactory()).get(MainActivityViewModel.class);
        initializeUI();
        setCurrentLocation();

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "City searched: " + query, Toast.LENGTH_SHORT).show();
                mSearchedCityWeather = true;
                setSearchedCityWeather(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

    }

    private void initializeUI() {
        temp_tv = findViewById(R.id.temp_tv);
        date_time_tv = findViewById(R.id.date_time_tv);
        max_temp_tv = findViewById(R.id.max_temp_tv);
        min_temp_tv = findViewById(R.id.min_temp_tv);
        feels_like_tv = findViewById(R.id.feels_like_temp_tv);
        weather_iv = findViewById(R.id.weather_iv);
        weather_tv = findViewById(R.id.weather_tv);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //searchView = findViewById(R.id.city_name_search);
        location_pin_iv = findViewById(R.id.location_pin_iv);
        current_location_tv = findViewById(R.id.current_location_tv);
        humidity_tv = findViewById(R.id.humidity_value);
        pressure_tv = findViewById(R.id.pressure_value);
        speed_tv = findViewById(R.id.wind_speed_value);
        sunrise_tv = findViewById(R.id.sunrise_value);
        sunset_tv = findViewById(R.id.sunset_value);
    }

   /* private void setSearchedCityWeather(String cityName) {
        MutableLiveData<WeatherResponse> response = mainActivityViewModel.getWeatherData(cityName);
        response.observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                if (weatherResponse != null) {
                    setTextTv(weatherResponse);
                }
                else{
                    temp_tv.setText(R.string.no_data_available);
                }
            }
        });
    }*/

    private void setWeatherData() {
        MutableLiveData<WeatherResponse> response = mainActivityViewModel.getWeatherData(latitude,longitude);
        response.observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                if (weatherResponse != null) {
                    setTextTv(weatherResponse);
                }
                else{
                    temp_tv.setText(R.string.no_data_available);
                }
            }
        });
    }


    //----------------------------- Get Location START-------------------------

    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(),
                            mLocationCallback,
                            null /* Looper */);

                Toast.makeText(this, "Location being set!", Toast.LENGTH_SHORT).show();
        }
    }


    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mTrackingLocation = false;
            Toast.makeText(this, "Stopped tracking location!", Toast.LENGTH_SHORT).show();
            //progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {// If the permission is granted, get the location, otherwise,
            // show a Toast
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                startTrackingLocation();
            } else {
                Toast.makeText(this,
                        "Permission_denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mTrackingLocation) {
            startTrackingLocation();
        } else {
            stopTrackingLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTrackingLocation) {
            stopTrackingLocation();
            mTrackingLocation = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTrackingLocation) {
            startTrackingLocation();
        }

    }

    private void setCurrentLocation() {

        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                // If tracking is turned on, reverse geocode into an address
                if (mTrackingLocation) {
                    longitude = ((int)locationResult.getLastLocation().getLongitude())+"";
                    latitude = ((int)locationResult.getLastLocation().getLatitude())+"";
                    Log.d("MainActivity",latitude+" : "+longitude);
                    //Toast.makeText(MainActivity.this, "Location set", Toast.LENGTH_SHORT).show();
                    setWeatherData();
                    if (mTrackingLocation) {
                        new FetchAddressTask(MainActivity.this, MainActivity.this)
                                .execute(locationResult.getLastLocation());
                    }
                }
            }
        };
    }

    @Override
    public void onTaskCompleted(String result) {
        String resAddress = String.format("%s,%s", result.split(",", 0)[1], result.split(",", 0)[2]);
        current_location_tv.setText(resAddress);
    }


    //----------------------------- Get Location END -------------------------

    private void setTextTv(WeatherResponse weatherResponse) {
        String minTemp = weatherResponse.getMain().getTempMin()+" \u00B0";
        String maxTemp = weatherResponse.getMain().getTempMax()+" \u00B0";
        String temp = weatherResponse.getMain().getTemp()+" \u00B0C";
        String feelsLike = "Feels like "+weatherResponse.getMain().getFeelsLike()+" \u00B0";
        List<Weather> weatherList = weatherResponse.getWeather();
        Weather weather = weatherList.get(0);
        String w_desc = weather.getDescription();
        String w_icon = weather.getIcon();
        String humidity = weatherResponse.getMain().getHumidity()+" %";
        String pressure = weatherResponse.getMain().getPressure()+" Pa";
        String windSpeed = weatherResponse.getWind().getSpeed()+" m/sec";
        String sunrise = weatherResponse.getSys().getSunrise()+" UTC";
        String sunset = weatherResponse.getSys().getSunset()+" UTC";

        //String s_r = getTime(sunrise);
        //String s_s = getTime(sunset);

        current_location_tv.setText(weatherResponse.getName());
        min_temp_tv.setText(minTemp);
        max_temp_tv.setText(maxTemp);
        temp_tv.setText(temp);
        feels_like_tv.setText(feelsLike);
        weather_tv.setText(w_desc);
        String img_url="https://openweathermap.org/img/wn/"+w_icon+"@2x.png";
        Log.d("w_icon",img_url);
        Picasso.get().load(img_url).placeholder(R.drawable.ic_baseline_wb_sunny_24).into(weather_iv);
        String currentDate = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        date_time_tv.setText(String.format("%s, %s", currentDate, currentTime));
        humidity_tv.setText(humidity);
        pressure_tv.setText(pressure);
        speed_tv.setText(windSpeed);
        sunrise_tv.setText(sunrise);
        sunset_tv.setText(sunset);
    }

    /*String getTime(String time) throws ParseException {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = utcFormat.parse(time);

        DateFormat pstFormat = new SimpleDateFormat("hh:mm aa");
        pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        return pstFormat.format(date);
    }
*/
    private void disableViews(View view) {
        view.setVisibility(View.GONE);
    }
    private void enableViews(View view){
        view.setVisibility(View.VISIBLE);
    }

}
