package com.example.android.healthdatagathering.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(strings[1]);
            wr.flush();
            wr.close();
            Log.i("HTTP POST", strings[0] + "; " + strings[1]);
            Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
            Log.i("MSG", httpURLConnection.getResponseMessage());
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "done";
    }

}

