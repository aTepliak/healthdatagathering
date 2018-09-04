package com.example.android.healthdatagathering.database.entity;

import org.json.JSONObject;

import java.util.ArrayList;

public interface HealthData    {

    String getStringRepresentation();
    JSONObject getJSONRepresentation();
    ArrayList<String> getStringSeriesRepresentation();
    ArrayList<Float> getFloatSeriesRepresentation();


}
