package com.example.android.healthdatagathering;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.android.healthdatagathering.samsugshealth.SamsungSHealthCollector;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;

import java.util.Collections;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String APP_TAG = "SimpleHealth";

    @BindView(R.id.editHealthDateValue1)
    TextView mStepCountTv;

    private HealthDataStore mStore;
    private SamsungSHealthCollector mReporter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(this, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();
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

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

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
                error.resolve(MainActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    private boolean isPermissionAcquired() {
        HealthPermissionManager.PermissionKey permKey = new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ);
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(Collections.singleton(permKey));
            return resultMap.get(permKey);
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
            pmsManager.requestPermissions(Collections.singleton(permKey), MainActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                            updateStepCountView("");
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

    private SamsungSHealthCollector.StepCountObserver mStepCountObserver = count -> {
        Log.d(APP_TAG, "Step reported : " + count);
        updateStepCountView(String.valueOf(count));
    };

    private void updateStepCountView(final String count) {
        runOnUiThread(() -> mStepCountTv.setText(count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == R.id.connect) {
            requestPermission();
        }

        return true;
    }


}
