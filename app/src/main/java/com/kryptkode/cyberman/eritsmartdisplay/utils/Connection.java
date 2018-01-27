package com.kryptkode.cyberman.eritsmartdisplay.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Cyberman on 7/1/2017.
 */

public class Connection {
    private static final String TAG = Connection.class.getSimpleName();
    public static  String res=null;

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
 public static boolean sendData(String url){
        String acknowledgement;
        Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            acknowledgement = response.body().toString();
            return !acknowledgement.isEmpty();
        } catch (Exception e) {
            Log.e(TAG, "getData: " + e.getLocalizedMessage());
        }
        return false; //if there is an error, return null
    }


  public static String sendDataRequest(String url){
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

