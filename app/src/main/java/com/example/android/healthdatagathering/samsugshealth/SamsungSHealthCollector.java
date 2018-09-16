package com.example.android.healthdatagathering.samsugshealth;



import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class SamsungSHealthCollector {
    private final HealthDataStore mStore;
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    private int todaySteps;
    public SamsungSHealthCollector(HealthDataStore store) {
        mStore = store;
    }

    public void start() {


        readTodayStepCount();
    }

    // Read the today's step count on demand
    private void readTodayStepCount() {

        readTodayData(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthConstants.StepCount.COUNT, HealthConstants.StepCount.START_TIME,HealthConstants.StepCount.TIME_OFFSET);
        readTodayData(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, HealthConstants.BloodGlucose.GLUCOSE, HealthConstants.BloodGlucose.START_TIME,HealthConstants.BloodGlucose.TIME_OFFSET);
        Log.i("LOOOOOOOOOOOOOOOOK", mListener.toString());
    }

    // Read the today's step count on demand
    private void readTodayData(String healthDataType, String data,  String start, String offset) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = startTime + ONE_DAY_IN_MILLIS;

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(healthDataType)
                .setProperties(new String[] {data})
                .setLocalTimeRange(start, offset,
                        startTime, endTime)
                .build();

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

    public int getTodaySteps() {
        return todaySteps;
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListener = result -> {
        int count = 0;

        try {
            for (HealthData data : result) {
                count += data.getInt(HealthConstants.StepCount.COUNT);

            }
            todaySteps=count;
        } finally {

            result.close();
        }


    };


}
