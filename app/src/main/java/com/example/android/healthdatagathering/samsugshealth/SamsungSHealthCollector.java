package com.example.android.healthdatagathering.samsugshealth;

import com.example.android.healthdatagathering.MainActivity;
import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class SamsungSHealthCollector {
    private final HealthDataStore mStore;
    private StepCountObserver mStepCountObserver;
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    float heartRate;
    long sleepStart;
    long sleepEnd;
    int steps;
    float glucoseValue;
    int healthDataCounter;
    int floorsClimbed;
    String floors;
    float oxygenSaturation;
    int sleepStage;
    float uvExplosure;
    float bodyTemperature;
    long startTime = getStartTimeOfToday();
    long endTime = startTime + ONE_DAY_IN_MILLIS;
    private final HealthResultHolder.ResultListener<ReadResult> mListener = result -> {
        try {
            for (HealthData data : result) {
                healthDataCounter++;
                float newGlucoseValue= data.getFloat(HealthConstants.BloodGlucose.GLUCOSE);
                if (newGlucoseValue!=0) glucoseValue= newGlucoseValue;
                floorsClimbed += ( floors =data.getString(HealthConstants.FloorsClimbed.FLOOR))!=null ? Integer.parseInt(floors):0 ;
                sleepStage +=  data.getInt(HealthConstants.SleepStage.STAGE) ;
                float newHeartRate = data.getFloat(HealthConstants.HeartRate.HEART_RATE);
                if(newHeartRate!=0) heartRate = newHeartRate;
                bodyTemperature = data.getFloat(HealthConstants.BodyTemperature.TEMPERATURE);
                uvExplosure = data.getFloat(HealthConstants.UvExposure.UV_INDEX);
                sleepStart += data.getFloat(HealthConstants.Sleep.START_TIME);
                sleepEnd += data.getFloat(HealthConstants.Sleep.END_TIME);
                Log.i("Glukose; heartrate; sleepEnd:", glucoseValue + "; " + heartRate + ";" + sleepEnd);
                steps += data.getInt(HealthConstants.StepCount.COUNT);
                Log.i("Steps:", String.valueOf(steps));
                Log.i("Floors climbed:", String.valueOf(floorsClimbed));
                oxygenSaturation+=data.getFloat(HealthConstants.OxygenSaturation.SPO2);
            }
        } finally {
            result.close();
        }

        if (mStepCountObserver != null) {
            mStepCountObserver.onChanged(steps);
        }
    };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d(MainActivity.APP_TAG, "Observer receives a data changed event");
            readTodayStepCount();
        }
    };

    public interface StepCountObserver {
        void onChanged(int count);
    }

    public SamsungSHealthCollector(HealthDataStore store) {
        mStore = store;
    }


    public void start(StepCountObserver listener) {
        mStepCountObserver = listener;
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);
        readTodayStepCount();
    }

    public void start() {
        readTodayStepCount();
    }

    // Read the today's step count on demand
    private void readTodayStepCount() {

        readTodayData(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthConstants.StepCount.COUNT, HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET);
        readTodayData(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, HealthConstants.BloodGlucose.GLUCOSE, HealthConstants.BloodGlucose.START_TIME, HealthConstants.BloodGlucose.TIME_OFFSET);
        readTodayData(HealthConstants.HeartRate.HEALTH_DATA_TYPE, HealthConstants.HeartRate.HEART_RATE, HealthConstants.HeartRate.START_TIME, HealthConstants.HeartRate.TIME_OFFSET);
        readTodayData(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthConstants.Sleep.START_TIME, HealthConstants.Sleep.START_TIME, HealthConstants.Sleep.TIME_OFFSET);
        readTodayData(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthConstants.Sleep.END_TIME, HealthConstants.HeartRate.START_TIME, HealthConstants.Sleep.TIME_OFFSET);
        readTodayData(HealthConstants.SleepStage.HEALTH_DATA_TYPE, HealthConstants.SleepStage.STAGE, HealthConstants.SleepStage.START_TIME, HealthConstants.SleepStage.TIME_OFFSET);
        readTodayData(HealthConstants.FloorsClimbed.HEALTH_DATA_TYPE, HealthConstants.FloorsClimbed.FLOOR, HealthConstants.FloorsClimbed.START_TIME, HealthConstants.FloorsClimbed.TIME_OFFSET);
        readTodayData(HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE, HealthConstants.OxygenSaturation.SPO2, HealthConstants.OxygenSaturation.START_TIME, HealthConstants.OxygenSaturation.TIME_OFFSET);
        readTodayData(HealthConstants.UvExposure.HEALTH_DATA_TYPE, HealthConstants.UvExposure.UV_INDEX, HealthConstants.UvExposure.START_TIME, HealthConstants.UvExposure.TIME_OFFSET);
        readTodayData(HealthConstants.BodyTemperature.HEALTH_DATA_TYPE, HealthConstants.BodyTemperature.TEMPERATURE, HealthConstants.BodyTemperature.START_TIME, HealthConstants.BodyTemperature.TIME_OFFSET);

    }

    // Read the today's step count on demand
    private void readTodayData(String healthDataType, String data, String start, String offset) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time


        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(healthDataType)
                .setProperties(new String[]{data})
                .setLocalTimeRange(start, offset,
                        startTime, endTime)
                .build();

        Log.i("BUILDING COUNT", resolver.read(request).toString());

        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {

        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }


    public int getFloorsClimbed() {
        return floorsClimbed;
    }

    public String getFloors() {
        return floors;
    }

    public float getOxygenSaturation() {
        return oxygenSaturation;
    }

    public int getSleepStage() {
        return sleepStage;
    }

    public float getUvExplosure() {
        return uvExplosure;
    }

    public float getBodyTemperature() {
        return bodyTemperature;
    }


    public float getGlucoseValue() {
        return glucoseValue;
    }


    public float getHeartRate() {
        return heartRate;
    }


    public long getSleepStart() {
        return   sleepStart;
    }


    public long getSleepEnd() {
        return  sleepEnd;
    }


    public String getSleepAsDateString(){
        if(sleepStart==sleepEnd)return  "no data available for today";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sleepTime = format.format(new Date(sleepStart ))  + "-" +
                format.format(new Date (sleepEnd  ));
        Log.i("Sleep time reported", sleepTime);
        return sleepTime;
    }

    public int getSteps() {
        return steps;
    }

    public  ArrayList<HealthDataAtomic> getDataForDb(){

        ArrayList<HealthDataAtomic> listOfAtomicEntities = new ArrayList<>();
            listOfAtomicEntities.add(new HealthDataAtomic("steps", new Date(startTime), new Date(endTime), getSteps()));
            listOfAtomicEntities.add(new HealthDataAtomic("bloodGlucose", new Date(startTime), new Date(endTime), getGlucoseValue()));
            listOfAtomicEntities.add(new HealthDataAtomic("floorsClimbed", new Date(startTime), new Date(endTime), getFloorsClimbed()));
            listOfAtomicEntities.add(new HealthDataAtomic("sleepStage", new Date(startTime), new Date(endTime), getSleepStage()));
            listOfAtomicEntities.add(new HealthDataAtomic("heartRate", new Date(startTime), new Date(endTime), getHeartRate()));
            listOfAtomicEntities.add(new HealthDataAtomic("bodyTemperature", new Date(startTime), new Date(endTime), getBodyTemperature()));
            listOfAtomicEntities.add(new HealthDataAtomic("uvExplosure", new Date(startTime), new Date(endTime), getUvExplosure()));
            listOfAtomicEntities.add(new HealthDataAtomic("sleepTime", new Date(getSleepStart()), new Date(getSleepEnd()), "sleep start"));
            listOfAtomicEntities.add(new HealthDataAtomic("oxgenSaturation", new Date(startTime), new Date(endTime), getOxygenSaturation()));

        return  listOfAtomicEntities;
    }

}