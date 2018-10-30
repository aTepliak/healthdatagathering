package com.example.android.healthdatagathering;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.samsugshealth.SamsungSHealthCollector;
import com.example.android.healthdatagathering.samsugshealth.SasmsungSHealthCollectorStarter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataTransmittingJobService extends JobService {
    /*
      REPLACE THE END-POINT URL HERE:
     */
    String urlAsString = "http://192.168.0.63:8080/collect/atomics";
    HealthDataStore.ConnectionListener mConnectionListener;
    SamsungSHealthCollector mReporter;
    private static final String APP_TAG = "Collecting scheduled";
    private static final int JOB_ID = 1;
    public static final long ONE_DAY_INTERVAL = 15 * 60 * 1000; // 24 * 60 * 60 * 1000L; // 1 Day
    private boolean allJobsFinished = false; // not going to be finished normally
    private SasmsungSHealthCollectorStarter starter = new SasmsungSHealthCollectorStarter();
    private static Context sContext;
    private HttpPostRequest httpPostRequest = new HttpPostRequest();
    SamsungSHealthCollector collector = new SamsungSHealthCollector(starter.getmStore());

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
        HealthDataAtomicDao atomicDao = AppDatabase.getInstance(this).healthDataAtomicDao();
        /* executing a task synchronously */
        starter.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //save to local smartphone database
                collector.getDataForDb().forEach(atomicData -> atomicDao.insert(atomicData));
            }
        }).start();
        Gson gson = new GsonBuilder()
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonAsString = gson.toJson(collector.getDataForDb());
        Log.i("HTTP ","start sending");
         httpPostRequest.execute(urlAsString, jsonAsString);
        Log.i("HTTP ","finished sending");

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


