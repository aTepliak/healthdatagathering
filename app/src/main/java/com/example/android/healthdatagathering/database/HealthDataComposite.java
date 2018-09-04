package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class HealthDataComposite implements HealthData {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private List<HealthDataComponent> components;

    private Date startTime;

    private Date endTime;



    @Ignore
    public HealthDataComposite(String name, List<HealthDataComponent> components, Date startTime, Date endTime) {
        this.name = name;
        this.components = components;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public HealthDataComposite(long id, String name, List<HealthDataComponent> components, Date startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.components = components;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId(){
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HealthDataComponent> getComponents() {
        return components;
    }

    public void setComponents(List<HealthDataComponent> components) {
        this.components = components;
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
