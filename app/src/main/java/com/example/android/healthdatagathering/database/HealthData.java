package com.example.android.healthdatagathering.database;

import org.json.JSONObject;

import java.util.ArrayList;

public interface HealthData <T> {

    String getStringRepresentation();
    JSONObject getJSONRepresentation();
    ArrayList<T> getSeriesRepresentation();


}
