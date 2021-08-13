package com.mattih.controlirrigtion.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseDataResponse {

    @SerializedName("baseDatas")
    @Expose
    private List<BaseData> baseDatas = null;


    public List<BaseData> getBaseData() {
        return baseDatas;
    }

    public void setBaseData(List<BaseData> baseDatas) {
        this.baseDatas = baseDatas;
    }


}
