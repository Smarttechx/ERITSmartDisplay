package com.softdev.smarttechx.eritsmartdisplay.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Cyberman on 7/1/2017.
 */

public class Connection {
    private static final String TAG = "TEST";
    public static String res = null;

    public static String getData(String url){
        Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            return response.body().toString();
        } catch (Exception e) {
            Log.e(TAG, "getData: " + e.getLocalizedMessage());
        }
        return null; //if there is an error, return null
    }

    public InputStream ByPostMethod(String ServerURL, String PostParam) {

        InputStream DataInputStream = null;
        try {

            //Post parameters
            //Preparing
            URL url = new URL(ServerURL);

            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to POST
            cc.setRequestMethod("POST");
            //set it to true as we are connecting for input
            cc.setDoInput(true);
            //opens the communication link
            cc.connect();

            //Writing data (bytes) to the data output stream
            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            //flushes data output stream.
            dos.flush();
            dos.close();

            //Getting HTTP response code
            int response = cc.getResponseCode();

            //if response code is 200 / OK then read Inputstream
            //HttpURLConnection.HTTP_OK is equal to 200
            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
                Log.e(TAG, "getData: " + DataInputStream);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in GetData", e);
        }
        return DataInputStream;

    }

    public static String sendDataRequest(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        res= e.toString();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        res = response.body().string();
                    }
                });
      return res;
      } catch (Exception e) {
          Log.e(TAG, "getData: " + e.getLocalizedMessage());
      }
      return null; //
    }
	
    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //txtString.setText(s);
        }
    }

   

}

