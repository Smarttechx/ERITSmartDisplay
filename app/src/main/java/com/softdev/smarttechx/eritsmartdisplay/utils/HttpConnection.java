package com.softdev.smarttechx.eritsmartdisplay.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by SMARTTECHX on 9/12/2017.
 */

public class HttpConnection extends AsyncTask<String, Void, String> {

        Context context;
        String msg="";
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.


        @Override
        protected void onPostExecute(String result) {
		super.onPostExecute(result);
            Log.d("msgfromboard", result);
            setMessageFromBoard(result);
        }



	  public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            URL urlObj = new URL(url);

            // make GET request to the given URL
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.connect();

            // receive response as inputStream
            inputStream = urlConnection.getInputStream();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);

            else
                result = "Did not work!";

        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public  String getMessageFromBoard(){
        return this.msg;
    }
	public void setMessageFromBoard(String resp){
		this.msg=resp;
	}
}
