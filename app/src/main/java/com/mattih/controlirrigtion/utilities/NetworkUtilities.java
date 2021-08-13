package com.mattih.controlirrigtion.utilities;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.mattih.controlirrigtion.api.ApiService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtilities {
    private static final long cacheSize = 6 * 1024 * 1024;
    public static String TAG = "com.mattihh.controlirrigation";
    private static String BASE_URL = "http://192.168.43.42/Smart_Irrigation/api/";
    private static String SENSOR_URL = "sensors";
    private static String BASEDATA_URL = "base-data";
    private static String HEADER_PRGMA = "Pragma";
    private static String HEADER_CACHE_CONTROL = "Cache-Control";
    private static NetworkUtilities instance;
    private Context mContext;

    public NetworkUtilities(Context mContext) {
        this.mContext = mContext;
    }

    public static NetworkUtilities getInstance(Context mContext) {
        if (instance == null)
            instance = new NetworkUtilities(mContext);
        return instance;
    }

    private static Retrofit retrofit() {
        Retrofit build = new Retrofit.Builder()
                .baseUrl("http://192.168.43.42/Smart_Irrigation/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return build;
    }

    private static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                //.cache(cache())
                .addInterceptor(httpLoggingInterceptor())
                .addNetworkInterceptor(networkInterceptor())
                .build();
    }

    private static Interceptor offlineInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d(TAG, "Offline interceptor called");
                Request request = chain.request();
//                if (!MainActivity.getInstance().hasNetwork())
//                {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();

                // }
                return chain.proceed(request);

            }
        };
    }

    private static Interceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, "Http: log " + message);
            }
        });
        return httpLoggingInterceptor;
    }

    private static Interceptor networkInterceptor() {
        return chain -> {
            Log.d(TAG, "network interceptor called");
            Response response = chain.proceed(chain.request());
            String cacheControl = response.header("Cache-Control");

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                CacheControl cacheControll = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build();
                return response.newBuilder()
                        .removeHeader(HEADER_PRGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControll.toString())
                        .build();
            } else {
                return response;
            }

        };

    }

    public static URL buildURL(String strinUrl) {
        Uri bulit = Uri.parse(BASE_URL + SENSOR_URL + "/read");
        URL url = null;
        try {
            url = new URL(bulit.toString());
        } catch (MalformedURLException malE) {
            Log.d(TAG, malE.getMessage());
        }

        return url;
    }

//    private static Cache cache() {
//        return new Cache(new File(MainActivity.getInstance().getCacheDir(),"cashefile"),cacheSize);
//    }

    public static ApiService getApiService() {
        return retrofit().create(ApiService.class);
    }

    public boolean hasConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }


}
