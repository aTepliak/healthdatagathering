
package com.example.android.healthdatagathering.database;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.arch.persistence.room.TypeConverter;


import com.google.gson.*;

import java.util.ArrayList;

public class FloatArrayListConverter {

    @TypeConverter
    public static ArrayList<Float> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Float> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}