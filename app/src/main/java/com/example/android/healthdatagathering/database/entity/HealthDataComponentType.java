package com.example.android.healthdatagathering.database.entity;

import org.json.JSONObject;

import java.util.Date;


public abstract class HealthDataComponentType implements HealthData {

    private String name;
    private boolean numericValue;
    private Date startTime;
    private Date endTime;

    public HealthDataComponentType(String name, Date startTime, Date endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setNumericValue(boolean numericValue) {
        this.numericValue = numericValue;
    }

    public boolean isNumericValue() {
        return numericValue;
    }

    @Override
    abstract public String getStringRepresentation();

    @Override
    abstract public JSONObject getJSONRepresentation();


}
