package com.example.android.healthdatagathering.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.healthdatagathering.SamsungSHealthActivity;
import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.samsugshealth.SasmsungSHealthCollectorStarter;

public class SaveToDataBaseJobService extends JobService {

    private static final String APP_TAG = "Collecting scheduled";
    private static final int JOB_ID = 2;
    public static final long ONE_DAY_INTERVAL = 15 * 60 * 1000; // 24 * 60 * 60 * 1000L; // 1 Day
    private boolean allJobsFinished = false; // not going to be finished normally
    private SasmsungSHealthCollectorStarter starter = new SasmsungSHealthCollectorStarter();
    private static Context sContext;

    public static void schedule(Context context, long intervalMillis) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, SaveToDataBaseJobService.class);
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
        HealthDataAtomicDao atomicDao = AppDatabase.getInstance(this).healthDataAtomicDao();
        Intent dialogIntent = new Intent(this, SamsungSHealthActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(dialogIntent);
        starter.start();
        if (allJobsFinished) {
            // To finish a periodic JobService,
            // if must be cancelled  so it will not be scheduled anymore.
            SaveToDataBaseJobService.cancel(this);
        }
        // false when it is synchronous.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}


