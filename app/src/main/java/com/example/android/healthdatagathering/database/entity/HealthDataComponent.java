package com.example.android.healthdatagathering.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

@Entity(indices = {@Index("id")})
public class HealthDataComponent   implements HealthData {


    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private boolean singleValue;

    public boolean isNumericValue() {
        return numericValue;
    }

    public void setNumericValue(boolean numericValue) {
        this.numericValue = numericValue;
    }

    private boolean numericValue;

    private Float floatValue;

    private ArrayList<Float> floatValues;

    private String stringValue;

    private ArrayList<String>stringValues;

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    public ArrayList<Float> getFloatValues() {
        return floatValues;
    }

    public void setFloatValues(ArrayList<Float> floatValues) {
        this.floatValues = floatValues;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public ArrayList<String> getStringValues() {
        return stringValues;
    }

    public void setStringValues(ArrayList<String> stringValues) {
        this.stringValues = stringValues;
    }

    //creating new entity

    public HealthDataComponent(String name, boolean singleValue, boolean numericValue, Float floatValue,
                               ArrayList<Float> floatValues, String stringValue, ArrayList<String> stringValues, Date  startTime, Date endTime) {
        this.name = name;
        this.singleValue = singleValue;
        this.stringValue = stringValue;
        this.floatValue= floatValue;
        this.stringValues= stringValues;
        this.floatValues= floatValues;
        this.startTime = startTime;
        this.endTime = endTime;
    }

   /* //reading from db
    public HealthDataComponent(long id, String name, boolean singleValue, boolean numericValue, Float floatValue,
                               ArrayList<Float> flaotArrayList, String stringValue, ArrayList<String> stringValuesDate, Date  startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.singleValue = singleValue;
        this.stringValue = stringValue;
        this.floatValue= floatValue;
        this.stringValues= stringValues;
        this.floatValues= floatValues;
        this.startTime = startTime;
        this.endTime = endTime;
    }
*/
    @Ignore
    public HealthDataComponent(  String name,  Float floatValue, Date  startTime, Date endTime) {
   this  ( name, true,true, floatValue, null, null, null,   startTime,   endTime)   ;
    }


    @Ignore
    public HealthDataComponent(  String name,   Date  startTime, Date endTime, ArrayList<Float> floatValues) {
        this  ( name, false,true, null, floatValues, null, null,   startTime,   endTime)   ;
    }


    @Ignore
    public HealthDataComponent(  String name,  String stringValue, Date  startTime, Date endTime) {
        this  ( name, true,false, null, null, stringValue, null,   startTime,   endTime)   ;
    }

    @Ignore
    public HealthDataComponent(  String name,  ArrayList<String> stringValues, Date  startTime, Date endTime) {
        this  ( name, false,true, null, null, null, stringValues,   startTime,   endTime)   ;
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
    public ArrayList<String> getStringSeriesRepresentation() {
        return null;
    }

    @Override
    public ArrayList<Float> getFloatSeriesRepresentation() {
        return null;
    }

}
