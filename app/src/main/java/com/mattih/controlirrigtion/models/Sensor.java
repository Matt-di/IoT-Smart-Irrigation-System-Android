package com.mattih.controlirrigtion.models;

public class Sensor {

    private int id;
    private String soilMoisture;
    private String temperature;
    private String humidity;
    private String motorStatus;
    private String updatedDate;


    public Sensor(int id, String soilMoisture, String temperature, String humidity, String motorStatus, String updatedDate) {
        this.id = id;
        this.soilMoisture = soilMoisture;
        this.temperature = temperature;
        this.humidity = humidity;
        this.motorStatus = motorStatus;
        this.updatedDate = updatedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(String soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getMotorStatus() {
        return motorStatus;
    }

    public void setMotorStatus(String motorStatus) {
        this.motorStatus = motorStatus;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
