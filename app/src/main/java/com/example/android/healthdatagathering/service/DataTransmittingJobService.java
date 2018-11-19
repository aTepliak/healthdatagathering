package com.example.android.healthdatagathering.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.android.healthdatagathering.HealthDataGatheringApp;
import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.database.dao.HealthDataComplexDao;
import com.example.android.healthdatagathering.samsugshealth.SasmsungSHealthCollectorStarter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataTransmittingJobService extends JobService {
    private String urlAtomicAsString = HealthDataGatheringApp.getUrlAtomicAsString();
    private String urlComplexAsString = HealthDataGatheringApp.getUrlComplexAsString();
    private static final String APP_TAG = "Collecting scheduled";
    private static final int JOB_ID = 1;
    public static final long ONE_DAY_INTERVAL = 15 * 60 * 1000; // 24 * 60 * 60 * 1000L; // 1 Day
    private boolean allJobsFinished = false; // not going to be finished normally
    private static Context sContext;


    public static void schedule(Context context, long intervalMillis) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, DataTransmittingJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(intervalMillis);
        jobScheduler.schedule(builder.build());
        sContext = context;
    }

    public static void cancel(Context context) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HealthDataAtomicDao atomicDao = AppDatabase.getInstance(HealthDataGatheringApp.getAppContext()).healthDataAtomicDao();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
                try {
                    String atomicJsonAsString = gson.toJson(atomicDao.loadAllAtomic());
                    Log.i("HTTP ", "start sending atomic");
                    new HttpPostRequest().execute(urlAtomicAsString, atomicJsonAsString);
                } catch (Exception e) {
                    System.out.print("Atomic table is empty");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HealthDataComplexDao complexDao = AppDatabase.getInstance(HealthDataGatheringApp.getAppContext()).healthDataComplexDao();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
                try {
                    Log.i("HTTP ", "start sending complex");
                    String complexJsonAsString = gson.toJson(complexDao.loadAllComplex());
                    new HttpPostRequest().execute(urlComplexAsString, complexJsonAsString);
                } catch (Exception e) {
                    System.out.print("Http request for a complex data exception thrown:" + e.getMessage());
                }
            }
        }).start();
        if (allJobsFinished) {
            // To finish a periodic JobService,
            // if must be cancelled  so it will not be scheduled anymore.
            DataTransmittingJobService.cancel(this);
        }
        // false when it is synchronous.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}


