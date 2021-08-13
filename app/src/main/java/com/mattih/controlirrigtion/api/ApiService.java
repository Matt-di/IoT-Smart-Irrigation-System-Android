package com.mattih.controlirrigtion.api;

import com.google.gson.JsonObject;
import com.mattih.controlirrigtion.models.BaseDataResponse;
import com.mattih.controlirrigtion.models.LoginResponse;
import com.mattih.controlirrigtion.models.SensorsResponse;
import com.mattih.controlirrigtion.models.WeatherForecastModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    @GET("sensors/read")
    Call<SensorsResponse> getSensors();

    @GET("base-data/read")
    Call<BaseDataResponse> getBaseData();

    @GET("forecast")
    Call<WeatherForecastModel> getWeatherForecast(@Query("lat") String latitude, @Query("lon") String longitude, @Query("appid") String apiKey);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/update_location")
    Call<LoginResponse> updateLocation(
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @Headers("Content-Type: application/json")
    @PUT("base-data/update")
    Call<LoginResponse> updateBaseData(@Body JsonObject body);
}
