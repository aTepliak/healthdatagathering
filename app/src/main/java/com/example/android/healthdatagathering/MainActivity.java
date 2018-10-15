
package com.example.android.healthdatagathering;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.charts.Cartesian;
import com.example.android.healthdatagathering.charts.ChartExample;
import com.example.android.healthdatagathering.charts.ColumnChart;


import com.example.android.healthdatagathering.charts.LineChart;
import com.example.android.healthdatagathering.charts.PieChart;
import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.example.android.healthdatagathering.samsugshealth.SamsungSHealthCollector;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChartView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.util.Collections;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {


    public static final String APP_TAG = "HealthDataGathering";
    private static Context sContext;
    @BindView(R.id.editHealthDateValue1)
    TextView mStepCountTv;
    @BindView(R.id.editHealthDateValue2)
    TextView mBloodSugar;
    @BindView(R.id.editHealthDateValue3)
    TextView mHeartRate;
    @BindView(R.id.editHealthDateValue4)
    TextView mSleepTime;
    @BindView(R.id.getData)
    Button getDataButton;
    private HealthDataStore mStore;
    private SamsungSHealthCollector mReporter;
    private DataTransmittingJobService dataTrasmittingJobService;
    private HealthDataGatheringApp app1;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        // app1 = new  HealthDataGatheringApp();
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(sContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataTrasmittingJobService = new DataTransmittingJobService();
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(sContext, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();

        //starting the job schedular once a day
        dataTrasmittingJobService.schedule(sContext, dataTrasmittingJobService.ONE_DAY_INTERVAL);


        ChartExample chartExample = new ChartExample();
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        anyChartView.setChart(chartExample.getBloodSugarExample());
        anyChartView.setVisibility(View.GONE);


        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);
        anyChartView1.setChart(chartExample.getSleepExample());
        anyChartView1.setVisibility(View.GONE);

        AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);
        APIlib.getInstance().setActiveAnyChartView(anyChartView2);
        anyChartView2.setChart(chartExample.getHeartRateExample());
        anyChartView2.setVisibility(View.GONE);


        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        String[] items = new String[]{"Steps", "Blood Glucose", "Blood Pressure", "Floors Climbed", "Sleep", "Sleep Stage", "Heart Rate",
        "Caffeine Intake", "Blood Pressure", "Exercise", "Walking", "Water" ,"Calories", "Distance"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        dynamicSpinner.setAdapter(adapter);
        final AnyChartView[] current = {anyChartView2};
        dynamicSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                String item = (String) parent.getItemAtPosition(position);

                if (item.equals("Caffeine Intake")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView.setVisibility(View.VISIBLE);
                    current[0] = anyChartView;
                }
                if (item.equals("Sleep Stage")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView1.setVisibility(View.VISIBLE);
                    current[0] = anyChartView1;
                }
                if (item.equals("Distance")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView2.setVisibility(View.VISIBLE);
                    current[0] = anyChartView2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }


    @OnClick(R.id.getData)
    public void setGetDataButton(Button button) {
        runOnUiThread(() -> mHeartRate.setText(String.valueOf(mReporter.getHeartRate())));
        runOnUiThread(() -> mBloodSugar.setText(String.valueOf(mReporter.getGlucoseValue())));
        runOnUiThread(() -> mStepCountTv.setText(String.valueOf(mReporter.getSteps())));
        runOnUiThread(() -> mSleepTime.setText(mReporter.getSleepAsDateString()));


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
                error.resolve(MainActivity.this);
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
            pmsManager.requestPermissions(generatePermissionKeySet(), MainActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

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


    private SamsungSHealthCollector.StepCountObserver mStepCountObserver = steps -> {
        Log.d(APP_TAG, "Step reported : " + steps);
        updateStepCountView(String.valueOf(steps));
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
