package com.mattih.controlirrigtion.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.43.42/Smart_Irrigation/api/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(String baseUrl) {
        if (mInstance == null) {
            mInstance = new RetrofitClient(baseUrl);
        }

        return mInstance;
    }

    public ApiService getMyApi() {
        return retrofit.create(ApiService.class);
    }
}
