package com.example.android.healthdatagathering;

import android.app.Application;
import android.content.Context;

public class HealthDataGatheringApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        HealthDataGatheringApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return HealthDataGatheringApp.context;
    }
}
