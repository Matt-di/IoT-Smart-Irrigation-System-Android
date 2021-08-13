package com.mattih.controlirrigtion.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SensorData {

    @SerializedName("data_id")
    @Expose
    private Integer dataId;
    @SerializedName("soilMoisture")
    @Expose
    private Double soilMoisture;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("motorStatus")
    @Expose
    private Integer motorStatus;
    @SerializedName("recommendation")
    @Expose
    private Object recommendation;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Double getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMotorStatus() {
        return motorStatus;
    }

    public void setMotorStatus(Integer motorStatus) {
        this.motorStatus = motorStatus;
    }

    public Object getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Object recommendation) {
        this.recommendation = recommendation;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}
