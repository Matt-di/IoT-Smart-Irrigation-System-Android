package com.mattih.controlirrigtion.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattih.controlirrigtion.activities.OnNewDataInsertedListener;
import com.mattih.controlirrigtion.models.BaseData;
import com.mattih.controlirrigtion.models.SensorData;
import com.mattih.controlirrigtion.models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyStorage {

    public static String MY_SHARED_STORAGE = "com.mattih.controlirrigtion.data.STORAGE";

    private static MyStorage mPreferences;
    OnNewDataInsertedListener onNewDataInsertedListener;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public MyStorage(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(MY_SHARED_STORAGE, Context.MODE_PRIVATE);
    }


    public static synchronized MyStorage getPreference(Context context) {
        if (mPreferences == null) {
            mPreferences = new MyStorage(context);
        }
        return mPreferences;
    }

    public void saveLogin(boolean status) {
        sharedPreferences.edit().putBoolean("user_login", status).apply();
    }

    public boolean checkLogin() {
        return sharedPreferences.getBoolean("user_login", false);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userStr = gson.toJson(user);
        editor.putString("sensor_data", userStr);
        editor.apply();
    }

    public List<SensorData> loadSensorResponse() {
        SharedPreferences preferences = mContext.getSharedPreferences(MY_SHARED_STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("sensor_data", null);
        Type type = new TypeToken<List<SensorData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ArrayList<BaseData> loadBaseData() {
        SharedPreferences preferences = mContext.getSharedPreferences(MY_SHARED_STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("base_data", null);
        Type type = new TypeToken<ArrayList<BaseData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveSensorData(List<SensorData> sensorData) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userStr = gson.toJson(sensorData);
        editor.putString("sensor_data", userStr);
        Date date = new Date();
        editor.putLong("last_updated", System.currentTimeMillis());
        editor.apply();
    }

    public long getLastUpdated() {
        SharedPreferences preferences = mContext.getSharedPreferences(MY_SHARED_STORAGE, Context.MODE_PRIVATE);
        return preferences.getLong("last_updated", -1);

    }

    public void saveBaseData(ArrayList<BaseData> baseDataResponse) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userStr = gson.toJson(baseDataResponse);
        editor.putString("base_data", userStr);
        editor.apply();
    }


    public void logoutUser() {
        sharedPreferences.edit().putBoolean("user_login", false).apply();
    }
}
