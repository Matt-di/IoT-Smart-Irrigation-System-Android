package com.mattih.controlirrigtion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.models.BaseData;

import java.util.ArrayList;

public class BaseDataAdapter extends RecyclerView.Adapter<BaseDataAdapter.MyViewHolder> {

    private ArrayList<BaseData> dataList;
    private String[] stringNames;
    private Context mContext;

    public BaseDataAdapter(ArrayList<BaseData> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        stringNames = mContext.getResources().getStringArray(R.array.base_data_names);
    }

    public ArrayList<BaseData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<BaseData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.textViewDataName.setText(stringNames[position]);
        if (dataList != null) {
            BaseData baseData = dataList.get(position);

            if (baseData.getName().equals("motor_status")) {
                holder.textViewDataValue.setText(baseData.getValue().equals("0") ? "Closed" : "Opened");
            } else if (baseData.getName().equals("control_mode")) {
                holder.textViewDataValue.setText(baseData.getValue().equals("0") ? "Automatic (Arduino)" : "User (Manual)");

            } else holder.textViewDataValue.setText(baseData.getValue());
        }
    }

    @Override
    public int getItemCount() {
        if (dataList != null) return dataList.size();
        return stringNames.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDataId, textViewDataName, textViewDataValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //textViewDataId = itemView.findViewById(R.id.tv_dataId);
            textViewDataName = itemView.findViewById(R.id.tv_dataName);
            textViewDataValue = itemView.findViewById(R.id.tv_dataValue);
        }
    }
}
