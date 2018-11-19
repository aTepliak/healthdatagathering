package com.example.android.healthdatagathering.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@Entity(indices = {@Index(value = {"name", "startTime"},
        unique = true)})
public class HealthDataComplex extends HealthDataComponentType {


    @PrimaryKey(autoGenerate = true)
    private long id;

    private ArrayList<HealthDataAtomic> atomicValues;

    public HealthDataComplex(String name, Date startTime, Date endTime, ArrayList<HealthDataAtomic> atomicValues) {
        super(name, startTime, endTime);
        this.atomicValues = atomicValues;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAtomicValues(ArrayList<HealthDataAtomic> atomicValues) {
        this.atomicValues = atomicValues;
    }

    public ArrayList<HealthDataAtomic> getAtomicValues() {
        return atomicValues;
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
