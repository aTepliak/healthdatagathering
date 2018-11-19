package com.example.android.healthdatagathering;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class HealthDataGatheringApp extends Application {
    private static Context context;
    /*
      REPLACE THE END-POINT URL HERE:
     */
    private static String urlAtomicAsString = "http://192.168.43.63:8080/collect/atomics";

    private static String urlComplexAsString = "http://192.168.43.63:8080/collect/complex";

    public void onCreate() {
        super.onCreate();
        HealthDataGatheringApp.context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return HealthDataGatheringApp.context;
    }

    public static String getUrlAtomicAsString() {
        return urlAtomicAsString;
    }

    public static String getUrlComplexAsString() {
        return urlComplexAsString;
    }


}
