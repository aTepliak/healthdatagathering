
package com.example.android.healthdatagathering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.database.dao.HealthDataComplexDao;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.example.android.healthdatagathering.samsugshealth.SamsungSHealthCollector;
import com.example.android.healthdatagathering.service.DataTransmittingJobService;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/*
  This activity is set to non visible in AndroidManifest.xml
  This activity is needed for SaveToDatabaseJobService, because the Samsung JDK requires activity
  for cases of errors, and data collection can not be provided without accompanying activity
 */
public class SamsungSHealthActivity extends Activity {


    public static final String APP_TAG = "HealthDataGathering";
    private static Context sContext;
    private HealthDataStore mStore;
    private SamsungSHealthCollector mReporter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        sContext = getApplicationContext();
        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(sContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mStore = new HealthDataStore(sContext, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();
        finish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            mReporter = new SamsungSHealthCollector(mStore);
            if (isPermissionAcquired()) {
                mReporter.start(mStepCountObserver);

            } else {
                requestPermission();
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
            if (!isFinishing()) {
                mStore.connectService();
            }
        }
    };

    private void showPermissionAlarmDialog() {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(SamsungSHealthActivity.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(sContext);

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    alert.setMessage(R.string.msg_req_install);
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    alert.setMessage(R.string.msg_req_upgrade);
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    alert.setMessage(R.string.msg_req_enable);
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    alert.setMessage(R.string.msg_req_agree);
                    break;
                default:
                    alert.setMessage(R.string.msg_req_available);
                    break;
            }
        } else {
            alert.setMessage(R.string.msg_conn_not_available);
        }

        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (error.hasResolution()) {
                error.resolve(SamsungSHealthActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    private boolean isPermissionAcquired() {

        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(generatePermissionKeySet());
            return resultMap.entrySet().stream().allMatch(e -> e.getValue() == true);
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission request fails.", e);
        }
        return false;
    }

    private void requestPermission() {

        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(generatePermissionKeySet(), SamsungSHealthActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                            showPermissionAlarmDialog();
                        } else {
                            // Get the current step count and display it
                            mReporter.start(mStepCountObserver);
                        }
                    });
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission setting fails.", e);
        }
    }


    private Set<HealthPermissionManager.PermissionKey> generatePermissionKeySet() {
        Set<PermissionKey> pmsKeySet = new HashSet<>();
        // Add the read and write permissions to Permission KeySet
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FloorsClimbed.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.SleepStage.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.UvExposure.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.BodyTemperature.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        return pmsKeySet;
    }

    // In this observer instance the data is collected by the SaveToDataBaseJobService
    private SamsungSHealthCollector.StepCountObserver mStepCountObserver = steps -> {
        Log.d(APP_TAG, "Step reported in SamsungSHealthActivity: " + steps);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HealthDataAtomicDao atomicDao = AppDatabase.getInstance(HealthDataGatheringApp.getAppContext()).healthDataAtomicDao();
                mReporter.getAtomicDataForDb().forEach(atomicData -> {
                    atomicDao.insert(atomicData);
                });
                HealthDataComplexDao complexDao = AppDatabase.getInstance(HealthDataGatheringApp.getAppContext()).healthDataComplexDao();
                mReporter.getComplexDataForDb().forEach(complexData -> {
                    complexData.getAtomicValues().forEach(atomicData -> atomicDao.insert(atomicData));
                    complexDao.insert(complexData);
                });
            }
        });

    };

}
