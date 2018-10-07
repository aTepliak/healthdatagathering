package com.example.android.healthdatagathering.samsugshealth;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.example.android.healthdatagathering.DataTransmittingJobService;
import com.example.android.healthdatagathering.HealthDataGatheringApp;
import com.example.android.healthdatagathering.MainActivity;
import com.example.android.healthdatagathering.R;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.android.healthdatagathering.MainActivity.APP_TAG;

public class SasmsungSHealthCollectorStarter  {
    public static final String COLLECOR_TAG = "Collecting HEALTH DATA";

    public HealthDataStore getmStore() {
        return mStore;
    }

    private HealthDataStore mStore;



    private SamsungSHealthCollector mReporter;
    private DataTransmittingJobService dataTrasmittingJobService;

    public HealthDataStore.ConnectionListener getmConnectionListener() {
        return mConnectionListener;
    }

    private  HealthDataStore.ConnectionListener   mConnectionListener;
    public SamsungSHealthCollector getmReporter() {
        return mReporter;
    }
     public void start()

    {

        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(HealthDataGatheringApp.getAppContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(COLLECOR_TAG, "started");

        // Create a HealthDataStore instance and set its listener
       // getmConnectionListener();
        mStore = new HealthDataStore(HealthDataGatheringApp.getAppContext(), mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService( 15*60*1000);

         mConnectionListener = new HealthDataStore.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected!!!!!!!!!!!!!!!!!!!!!.");
            mReporter = new SamsungSHealthCollector(mStore);

            if (isPermissionAcquired()) {
               // mReporter.start(mStepCountObserver);
                 mReporter.start();
                Log.i(COLLECOR_TAG, String.valueOf(mReporter.getGlucoseValue() ));
            }
         else {
                 requestPermission();
             }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");

        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");

        }


          };


       // mConnectionListener.onConnected();

    }




        private boolean isPermissionAcquired() {

            HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
            try {
                // Check whether the permissions that this application needs are acquired
                Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(generatePermissionKeySet());
                return  resultMap.entrySet().stream().allMatch(e -> e.getValue() == true);
            } catch (Exception e) {
                Log.e(APP_TAG, "Permission request fails.", e);
            }
            return false;
        }


    private void requestPermission() {
        HealthPermissionManager.PermissionKey permKey = new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ);
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(Collections.singleton(permKey), new MainActivity())
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                           Log.i(APP_TAG, "Permission needed");
                        } else {
                            // Get the current step count and display it
                            mReporter.start();
                        }
                    });
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission setting fails.", e);
        }
    }


        private Set<HealthPermissionManager.PermissionKey> generatePermissionKeySet() {
            Set<HealthPermissionManager.PermissionKey> pmsKeySet = new HashSet<>();

            // Add the read and write permissions to Permission KeySet
            pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
            pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
            pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
            //pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FloorsClimbed.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
            pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
            pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.SleepStage.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
            pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));

            return pmsKeySet;
        }


}



