
package com.example.android.healthdatagathering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.example.android.healthdatagathering.charts.ChartExample;
import com.example.android.healthdatagathering.charts.ColumnChart;
import com.example.android.healthdatagathering.database.AppDatabase;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.example.android.healthdatagathering.samsugshealth.SamsungSHealthCollector;
import com.example.android.healthdatagathering.service.DataTransmittingJobService;
import com.example.android.healthdatagathering.service.SaveToDataBaseJobService;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import butterknife.ButterKnife;


public class MainActivity extends Activity {

    public static final String APP_TAG = "HealthDataGathering";
    private static Context sContext;
    /*@BindView(R.id.editHealthDateValue1)
    TextView mStepCountTv;*/

    private HealthDataStore mStore;
    private SamsungSHealthCollector mReporter;
    private DataTransmittingJobService dataTrasmittingJobService;
    private SaveToDataBaseJobService saveToDataBaseJobService;
    private HealthDataGatheringApp app1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);


        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(sContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataTrasmittingJobService = new DataTransmittingJobService();
        saveToDataBaseJobService = new SaveToDataBaseJobService();
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(sContext, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();
        //starting the job schedular once a day
        dataTrasmittingJobService.schedule(sContext, dataTrasmittingJobService.ONE_DAY_INTERVAL);
        saveToDataBaseJobService.schedule(sContext, saveToDataBaseJobService.ONE_DAY_INTERVAL);


        setContentView(R.layout.activity_main);
        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        String[] items = new String[]{"Select item","Steps", "Blood Glucose",   "Floors Climbed",  "Sleep Stage", "Heart Rate",
                "Caffeine Intake", "Diastolic Pressure",   "Systolic Pressure", "Distance"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        dynamicSpinner.setAdapter(adapter);


        //chartExample instances are only for presentation purposes
        ChartExample chartExample = new ChartExample();
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
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


        // from here on data is read "live" from db in AsyncTask class instance
        AnyChartView anyChartView3 = findViewById(R.id.any_chart_view3);
        APIlib.getInstance().setActiveAnyChartView(anyChartView3);
        anyChartView3.setVisibility(View.GONE);

        AnyChartView anyChartView4 = findViewById(R.id.any_chart_view4);
        APIlib.getInstance().setActiveAnyChartView(anyChartView4);
        anyChartView4.setVisibility(View.GONE);

        AnyChartView anyChartView5 = findViewById(R.id.any_chart_view5);
        APIlib.getInstance().setActiveAnyChartView(anyChartView5);
        anyChartView5.setVisibility(View.GONE);

        AnyChartView anyChartView6 = findViewById(R.id.any_chart_view6);
        APIlib.getInstance().setActiveAnyChartView(anyChartView6);
        anyChartView6.setVisibility(View.GONE);

        AnyChartView anyChartView7 = findViewById(R.id.any_chart_view7);
        APIlib.getInstance().setActiveAnyChartView(anyChartView7);
        anyChartView7.setVisibility(View.GONE);

        AnyChartView anyChartView8 = findViewById(R.id.any_chart_view8);
        APIlib.getInstance().setActiveAnyChartView(anyChartView8);
        anyChartView8.setVisibility(View.GONE);


        final AnyChartView[] current = {anyChartView};
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
                if (item.equals("Steps")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView3.setVisibility(View.VISIBLE);
                    APIlib.getInstance().setActiveAnyChartView(anyChartView3);
                    current[0] = anyChartView3;
                }
                if (item.equals("Diastolic Pressure")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView4.setVisibility(View.VISIBLE);
                    current[0] = anyChartView4;
                }
                if (item.equals("Systolic Pressure")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView5.setVisibility(View.VISIBLE);
                    current[0] = anyChartView5;
                }
                if (item.equals("Blood Glucose")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView6.setVisibility(View.VISIBLE);
                    current[0] = anyChartView6;
                }
                if (item.equals("Floors Climbed")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView7.setVisibility(View.VISIBLE);

                    current[0] = anyChartView7;
                }
                if (item.equals("Heart Rate")) {
                    current[0].setVisibility(View.GONE);
                    anyChartView8.setVisibility(View.VISIBLE);
                    current[0] = anyChartView8;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // to read the data from db asynchronously
        AsyncTask<Context, Void, List<HealthDataAtomic>> st = new AsyncTask<Context, Void, List<HealthDataAtomic>>() {
            @Override
            protected List<HealthDataAtomic> doInBackground(Context... context) {
                //android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE);
                HealthDataAtomicDao atomicDao = AppDatabase.getInstance(HealthDataGatheringApp.getAppContext()).healthDataAtomicDao();
                List<HealthDataAtomic> atomics = atomicDao.loadAllAtomic();
                return atomics;
            }

            @Override
            protected void onPostExecute(List<HealthDataAtomic> atomics) {
                super.onPostExecute(atomics);

                if (atomics != null) {
                    HashMap<String,Float> steps = getMap(atomics,"steps");
                    HashMap<String,Float> dyastolic = getMap(atomics,"diastolic");
                    HashMap<String,Float> systolic = getMap(atomics,"systolic");
                    HashMap<String,Float> bloodGlucose = getMap(atomics,"bloodGlucose");
                    HashMap<String,Float> floorsClimbed = getMap(atomics,"floorsClimbed");
                    HashMap<String,Float> heartRate = getMap(atomics,"heartRate");
                    anyChartView3.setChart(new ColumnChart(steps, "Steps", "", "", "steps").getCartesian());
                    anyChartView4.setChart(new ColumnChart(dyastolic, "Diastolic Blood Pressure", "", "", "mmHg").getCartesian());
                    anyChartView5.setChart(new ColumnChart(systolic, "Systolic Blood Pressure", "", "", "mmHg").getCartesian());
                    anyChartView6.setChart(new ColumnChart(bloodGlucose, "Blood Glucose", "", "", "mg/dL").getCartesian());
                    anyChartView7.setChart(new ColumnChart(floorsClimbed, "Floors Climbed", "", "", "floors").getCartesian());
                    anyChartView8.setChart(new ColumnChart(heartRate, "Heart Rate", "", "", "bit per minute").getCartesian());
                }
            }

            public HashMap<String, Float> getMap(List<HealthDataAtomic> atomics, String dataType) {
                HashMap<String, Float> mappedValues = new HashMap<>();
                List<Date> dates = atomics.stream().filter(atomic -> atomic.getName().equals(dataType)).map(atomic -> atomic.getStartTime()).collect(Collectors.toList());
                List<String> formattedDates = dates.stream().map(data -> {
                    DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                    String date = dateFormat.format(data);
                    return date;
                }).collect(Collectors.toList());
                Log.i("ATOMICS", Arrays.toString(formattedDates.toArray()));
                List<Float> values = atomics.stream().filter(atomic -> atomic.getName().equals(dataType)).map(atomic -> atomic.getFloatValue()).collect(Collectors.toList());
                for (int i = 0; i < values.size(); i++) {
                    mappedValues.put(formattedDates.get(i), values.get(i));
                }
                return mappedValues;
            }


        };
        st.execute();


    }


    @Override
    public void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }

     /*  mConnectionListener and functions below are needed to ask all the permissions from Samsung S Health and show error dialogs if
         permissions are not acquired
     */
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
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.CaffeineIntake.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.Exercise.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        return pmsKeySet;
    }


    private SamsungSHealthCollector.StepCountObserver mStepCountObserver = steps -> {
        Log.d(APP_TAG, "Step reported : " + steps);
        updateStepCountView(String.valueOf(steps));
    };

    private void updateStepCountView(final String count) {

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
