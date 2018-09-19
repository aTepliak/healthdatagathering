package com.example.android.healthdatagathering.samsugshealth;

import com.example.android.healthdatagathering.MainActivity;
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
import java.util.Calendar;
import java.util.Date;
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

    private final HealthResultHolder.ResultListener<ReadResult> mListener = result -> {



        try {
            for (HealthData data : result) {
                healthDataCounter++;
                float newGlucoseValue= data.getFloat(HealthConstants.BloodGlucose.GLUCOSE);
                if (newGlucoseValue!=0) glucoseValue= newGlucoseValue;
                float newHeartRate = data.getFloat(HealthConstants.HeartRate.HEART_RATE);
                if(newHeartRate!=0) heartRate = newHeartRate;
                sleepStart += data.getFloat(HealthConstants.Sleep.START_TIME);
                sleepEnd += data.getFloat(HealthConstants.Sleep.END_TIME);
                Log.i("Glukose; heartrate; sleepEnd", glucoseValue + "; " + heartRate + ";" + sleepEnd);

                steps += data.getInt(HealthConstants.StepCount.COUNT);
                Log.i("Steps", String.valueOf(steps));
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
        readTodayData(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthConstants.Sleep.START_TIME, HealthConstants.HeartRate.START_TIME, HealthConstants.HeartRate.TIME_OFFSET);
        readTodayData(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthConstants.Sleep.END_TIME, HealthConstants.HeartRate.START_TIME, HealthConstants.HeartRate.TIME_OFFSET);

    }

    // Read the today's step count on demand
    private void readTodayData(String healthDataType, String data, String start, String offset) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = startTime + ONE_DAY_IN_MILLIS;

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sleepTime = format.format(new Date(sleepStart ))  + "-" +
                format.format(new Date (sleepEnd  ));
        Log.i("Sleep time reported", sleepTime);
        return sleepTime;
    }

    public int getSteps() {
        return steps;
    }


}




