package com.mattih.controlirrigtion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.models.WeatherForecastModel;

import java.util.ArrayList;

public class WeatherForecasatAdapter extends RecyclerView.Adapter<WeatherForecasatAdapter.WeatherHolder> {
    private ArrayList<WeatherForecastModel> weatherForecastModels;
    private Context mContext;

    public WeatherForecasatAdapter(Context mContext, ArrayList<WeatherForecastModel> weatherForecastModels) {
        this.mContext = mContext;
        this.weatherForecastModels = weatherForecastModels;

    }

    @NonNull
    @Override
    public WeatherForecasatAdapter.WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_list_holder, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecasatAdapter.WeatherHolder holder, int position) {
//        WeatherForecastModel weatherForecastModel = weatherForecastModels.get(position);
//        holder.CurrentMax.setText(weatherForecastModel.getMax());
//        holder.CurrentMin.setText(weatherForecastModel.getMin());
//        holder.CurrentStatus.setText(weatherForecastModel.getStatus());
//        holder.CurrentDate.setText(weatherForecastModel.getDate());
        holder.CurrentStatusImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_hot));

    }

    @Override
    public int getItemCount() {
        if (weatherForecastModels == null || weatherForecastModels.isEmpty()) return 0;
        return weatherForecastModels.size();
    }

    static class WeatherHolder extends RecyclerView.ViewHolder {
        TextView CurrentDate, CurrentStatus, CurrentMax, CurrentMin;
        ImageView CurrentStatusImg;

        WeatherHolder(@NonNull View view) {
            super(view);
            CurrentDate = view.findViewById(R.id.tv_currentDate);
            CurrentStatus = view.findViewById(R.id.tv_currentStatus);
            CurrentMax = view.findViewById(R.id.tv_max);
            CurrentMin = view.findViewById(R.id.tv_min);
            CurrentStatusImg = view.findViewById(R.id.img_currentStatus);
        }
    }
}
