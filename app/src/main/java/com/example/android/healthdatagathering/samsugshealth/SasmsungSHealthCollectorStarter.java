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
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.android.healthdatagathering.MainActivity.APP_TAG;

public class SasmsungSHealthCollectorStarter  {
    public static final String COLLECOR_TAG = "Collecting HEALTH DATA HEREEEEEEE";
    private HealthDataStore mStore;
    private SamsungSHealthCollector mReporter;
    private DataTransmittingJobService dataTrasmittingJobService;
     HealthDataStore.ConnectionListener mConnectionListener;
    public void start()

    {
        Log.i(COLLECOR_TAG, "started");

        // Create a HealthDataStore instance and set its listener
       // getmConnectionListener();
        mStore = new HealthDataStore(HealthDataGatheringApp.getAppContext(), mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();

        mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            mReporter = new SamsungSHealthCollector(mStore);
            if (isPermissionAcquired()) {
               // mReporter.start(mStepCountObserver);

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

        Log.i(COLLECOR_TAG, String.valueOf(mReporter.getSteps() ));
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



