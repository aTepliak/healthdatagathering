package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class HealthDataComponent <T> implements HealthData {


    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private boolean singleValue;

    private T value;

    ArrayList<T> values;


    //creating new entity
    @Ignore
    public HealthDataComponent(String name, boolean singleValue, T value, ArrayList<T> values, Date startTime, Date endTime) {
        this.name = name;
        this.singleValue = singleValue;
        this.value = value;
        this.values = values;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //reading from db
    public HealthDataComponent(long id, String name, boolean singleValue, T value, ArrayList<T> values, Date startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.singleValue = singleValue;
        this.value = value;
        this.values = values;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSingleValue() {
        return singleValue;
    }

    public void setSingleValue(boolean singleValue) {
        this.singleValue = singleValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ArrayList<T> getValues() {
        return values;
    }

    public void setValues(ArrayList<T> values) {
        this.values = values;
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

    private Date startTime;

    private Date endTime;


    @Override
    public String getStringRepresentation() {
        return null;
    }

    @Override
    public JSONObject getJSONRepresentation() {
        return null;
    }

    @Override
    public ArrayList getSeriesRepresentation() {
        return null;
    }
}
