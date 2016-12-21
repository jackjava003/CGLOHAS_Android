package com.example.jack.cglohas._05_order;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by JACK on 2016/10/16.
 */

public class checkAddTask extends AsyncTask<Object, Void, String> {

    private final static String TAG = "InsertOrderTask";
    public static final String ADDRESS_VERIFY_URL = "https://maps.google.com/maps/api/geocode/json?sensor=false&language=zh-tw&region=tw&address=";
    @Override
    protected String doInBackground(Object... params) {
        String add = params[0].toString();
        String result = null;
        try {
            URL verifyUrl = new URL(ADDRESS_VERIFY_URL + add);
            Log.e(TAG,add);
            // Open Connection to URL
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

            // Add Request Header
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send Request
            conn.setDoOutput(false);
            conn.connect();

            // Response code return from server.
            int responseCode = conn.getResponseCode();

            // Get the InputStream from Connection to read data sent from the
            // server.
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.indexOf("status")!=-1){
                        sb.append(line);
                    }
                }
                br.close();
                conn.disconnect();
                result = sb.toString().substring(sb.toString().indexOf(":")+1,sb.toString().lastIndexOf("\""));
                result = result.substring(result.indexOf("\"")+1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result =  null;
        }
        Log.e(TAG,result);
        return result;
    }
}
