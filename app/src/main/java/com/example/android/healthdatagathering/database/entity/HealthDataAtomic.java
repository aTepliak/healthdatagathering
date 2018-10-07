package com.example.android.healthdatagathering.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
@Entity(indices = {@Index("id")})
public class HealthDataAtomic extends HealthDataComponentType {


    public void setId(long id) {
        this.id = id;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String stringValue;
    private Float floatValue;

    public HealthDataAtomic(String name, Date startTime, Date endTime, long id, String stringValue, float floatValue) {
        super(name, startTime, endTime);
        this.id = id;
        this.stringValue=stringValue;
        this.floatValue=floatValue;
    }
   @Ignore
    public HealthDataAtomic(String name, Date startTime, Date endTime,   float floatValue) {

        this(name, startTime,endTime, null, floatValue);
    }
    @Ignore
    public HealthDataAtomic(String name, Date startTime, Date endTime,   String stringValue) {
        this(name, startTime,endTime, stringValue, null);
    }
    @Ignore
    public HealthDataAtomic(String name, Date startTime, Date endTime,  String stringValue, Float floatValue) {
        super(name, startTime, endTime);
        this.id = id;
        this.stringValue = stringValue;
        this.floatValue = floatValue;
    }

    public long getId() {
        return id;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    @Ignore
    public HealthDataAtomic(String name, Date startTime, Date endTime) {
        super(name, startTime, endTime);
    }

    @Override
    public String getStringRepresentation() {
        return null;
    }

    @Override
    public JSONObject getJSONRepresentation() {
        return null;
    }


}
