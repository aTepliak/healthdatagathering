package com.example.android.healthdatagathering.googlefit;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

public class GoogleFitHealthCollector extends Activity {


    private final String LOGTAG = "---- FIT TEST";
    private final int REQUEST_OAUTH = 101;
    private GoogleApiClient mClient;

    private interface Callback {
        void OK();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        isAvailable(new Callback() {
            @Override
            public void OK() {
                Log.i(LOGTAG, "Google Services and Fit installed and updated");

                requestAuthorization(new Callback() {
                    @Override
                    public void OK() {
                        Log.i(LOGTAG, "Got authorization from Google Fit");
                    }
                });
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_OAUTH) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(LOGTAG, "Got authorisation from Google Fit");
                if (!mClient.isConnected() && !mClient.isConnecting()) {
                    Log.d(LOGTAG, "Re-trying connection with Fit");
                    mClient.connect();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user cancelled the login dialog before selecting any action.
                Log.e(LOGTAG, "User cancelled the dialog");
            } else Log.e(LOGTAG, "Authorisation failed, result code " + resultCode);
        }
    }

    private void isAvailable(final Callback callback) {
        //first check that the Google APIs are available
        GoogleApiAvailability gapi = GoogleApiAvailability.getInstance();
        int apiresult = gapi.isGooglePlayServicesAvailable(this);
        if (apiresult != ConnectionResult.SUCCESS) {
            Log.e(LOGTAG, "Gogole Services not installed or obsolete");
        } else {
            // check that Google Fit is actually installed
            PackageManager pm = getPackageManager();
            try {
                pm.getPackageInfo("com.google.android.apps.fitness", PackageManager.GET_ACTIVITIES);
                // Success return object
                callback.OK();
            } catch (PackageManager.NameNotFoundException e) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.fitness")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness")));
                }
                Log.e(LOGTAG, "Google Fit not installed");
            }
        }
    }

    private void requestAuthorization(final Callback callback) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(Fitness.HISTORY_API);
        builder.addApi(Fitness.CONFIG_API);
        builder.addApi(Fitness.SESSIONS_API);
        builder.addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE));
        builder.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE));
        builder.addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE));
        builder.addScope(new Scope(Scopes.FITNESS_NUTRITION_READ_WRITE));

        builder.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                //createCustomDatatypes();
                callback.OK();
            }

            @Override
            public void onConnectionSuspended(int i) {
                String message = "";
                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                    message = "connection lost, network lost";
                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                    message = "connection lost, service disconnected";
                } else message = "connection lost, code: " + i;
                Log.e(LOGTAG, message);
            }
        });

        builder.addOnConnectionFailedListener(
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        if (!result.hasResolution()) {
                            Log.e(LOGTAG, "Connection failure has no resolution: " + result.getErrorMessage());
                        } else {
                            // The failure has a resolution. Resolve it.
                            // Called typically when the app is not yet authorized, and an
                            // authorization dialog is displayed to the user.
                            try {
                                Log.i(LOGTAG, "Attempting to resolve failed connection");
                                result.startResolutionForResult(GoogleFitHealthCollector.this, REQUEST_OAUTH);
                            } catch (IntentSender.SendIntentException e) {
                                Log.e(LOGTAG, "Exception while starting resolution activity", e);
                            }
                        }
                    }
                }
        );
        mClient = builder.build();
        mClient.connect();
    }

}
