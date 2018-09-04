package com.example.android.healthdatagathering.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(indices = {@Index("id")})
public class HealthDataComposite implements HealthData {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;



    private Date startTime;

    private Date endTime;



    @Ignore
    public HealthDataComposite(String name, Date startTime, Date endTime) {
        this.name = name;

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public HealthDataComposite(long id, String name,  Date startTime, Date endTime) {
        this.id = id;
        this.name = name;

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
    public ArrayList<String> getStringSeriesRepresentation() {
        return null;
    }

    @Override
    public ArrayList<Float> getFloatSeriesRepresentation() {
        return null;
    }

}
