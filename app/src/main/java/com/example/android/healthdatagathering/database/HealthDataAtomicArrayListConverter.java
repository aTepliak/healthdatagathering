package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.healthdatagathering.database.entity.HealthData;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HealthDataAtomicArrayListConverter {
    @TypeConverter
    public static ArrayList<HealthDataAtomic> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<HealthDataAtomic> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
