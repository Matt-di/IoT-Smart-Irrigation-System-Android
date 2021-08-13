package com.mattih.controlirrigtion.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SensorsResponse {

    @SerializedName("datas")
    @Expose
    private List<SensorData> datas = null;
    @SerializedName("totalData")
    @Expose
    private Integer totalData;

    public List<SensorData> getDatas() {
        return datas;
    }

    public void setDatas(List<SensorData> datas) {
        this.datas = datas;
    }

    public Integer getTotalData() {
        return totalData;
    }

    public void setTotalData(Integer totalData) {
        this.totalData = totalData;
    }

}
