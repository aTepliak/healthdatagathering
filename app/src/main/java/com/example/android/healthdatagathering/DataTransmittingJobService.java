package com.example.android.healthdatagathering;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.android.healthdatagathering.samsugshealth.SamsungSHealthCollector;
import com.example.android.healthdatagathering.samsugshealth.SasmsungSHealthCollectorStarter;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;

public class DataTransmittingJobService extends JobService {
    HealthDataStore.ConnectionListener mConnectionListener;
    SamsungSHealthCollector mReporter;
     private static final String APP_TAG = "Collecting scheduled";
     private static final int JOB_ID = 1;
    public static final long ONE_DAY_INTERVAL =  15* 60 * 1000; // 24 * 60 * 60 * 1000L; // 1 Day
    private  boolean allJobsFinished = false; // not going to be finished normally
    private SasmsungSHealthCollectorStarter starter = new SasmsungSHealthCollectorStarter();

    public static void schedule(Context context, long intervalMillis) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, DataTransmittingJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(intervalMillis);
        jobScheduler.schedule(builder.build());
    }

    public static void cancel(Context context) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        /* executing a task synchronously */
      // starter.start();


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


