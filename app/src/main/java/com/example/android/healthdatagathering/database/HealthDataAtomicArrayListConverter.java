package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HealthDataAtomicArrayListConverter {
    @TypeConverter
    public static ArrayList<HealthDataAtomic> fromString(String json) {
        Type listType = new TypeToken<ArrayList<HealthDataAtomic>>() {
        }.getType();
        return new Gson().fromJson(json, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<HealthDataAtomic> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
