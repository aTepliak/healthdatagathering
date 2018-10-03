package com.example.android.healthdatagathering.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
@Entity(indices = {@Index("id")})
public class HealthDataComplex extends HealthDataComponentType {


    public long getId() {
        return id;
    }

    public ArrayList<HealthDataAtomic> getAtomicValues() {
        return atomicValues;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    ArrayList<HealthDataAtomic> atomicValues;

    public HealthDataComplex(String name, Date startTime, Date endTime, long id, ArrayList<HealthDataAtomic> atomicValues) {
        super(name, startTime, endTime);
        this.id = id;
        this.atomicValues = atomicValues;
    }
    @Ignore
    public HealthDataComplex(String name, Date startTime, Date endTime,   ArrayList<HealthDataAtomic> atomicValues) {
        super(name, startTime, endTime);
        this.atomicValues = atomicValues;
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
