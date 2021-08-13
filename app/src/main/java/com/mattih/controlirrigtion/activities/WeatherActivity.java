package com.mattih.controlirrigtion.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.adapter.WeatherForecasatAdapter;
import com.mattih.controlirrigtion.api.RetrofitClient;
import com.mattih.controlirrigtion.models.LoginResponse;
import com.mattih.controlirrigtion.models.WeatherForecastModel;
import com.mattih.controlirrigtion.utilities.BaseUtils;
import com.mattih.controlirrigtion.utilities.NetworkUtilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements LocationListener {


    private static final int PERM_REQ_CODE = 11;
    LocationManager locationManager;
    String pro;
    double lat = 0.0;
    double lon = 0.0;
    EditText editTextLatitude, editTextLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        RecyclerView mRecyclerView = findViewById(R.id.rv_weather);
        editTextLatitude = findViewById(R.id.et_latitiude);
        editTextLongitude = findViewById(R.id.et_longitude);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        pro = locationManager.getBestProvider(new Criteria(), false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        WeatherForecasatAdapter adapter = new WeatherForecasatAdapter(getApplicationContext(), getFake());
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

    }

    private ArrayList<WeatherForecastModel> getFake() {
        ArrayList<WeatherForecastModel> models = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            models.add(new WeatherForecastModel());
        }

        return models;
    }

    boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    void requestPermis() {
        ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
        }, PERM_REQ_CODE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkPermission();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getLocation(View view) {
        if (checkPermission())
            locationManager.requestLocationUpdates(pro, 400, 1, this::onLocationChanged);
        else requestPermis();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        editTextLongitude.setText(String.format("%s", lon));
        editTextLatitude.setText(String.format("%s", lat));
    }


    public void updateLocation(View view) {
        if (!editTextLongitude.getText().toString().equals("") || !editTextLatitude.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please provide input", Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkUtilities.getInstance(getApplicationContext()).hasConnection()) {
                Call<LoginResponse> call = RetrofitClient.getInstance(BaseUtils.BASE_URL2).getMyApi().updateLocation(editTextLatitude.getText().toString(), editTextLongitude.getText().toString());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        assert response.body() != null;
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Some error ocurred", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No connection, please connect to internet", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
