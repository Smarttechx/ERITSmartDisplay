package com.kryptkode.cyberman.eritsmartdisplay;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayDB;
import com.kryptkode.cyberman.eritsmartdisplay.models.CustomBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.SmartDisplay;
import com.kryptkode.cyberman.eritsmartdisplay.utils.GsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.kryptkode.cyberman.eritsmartdisplay.EritSmartDisplayActivity.CUSTOM;
import static com.kryptkode.cyberman.eritsmartdisplay.EritSmartDisplayActivity.MESSAGE;
import static com.kryptkode.cyberman.eritsmartdisplay.EritSmartDisplayActivity.PRICE;
import static com.kryptkode.cyberman.eritsmartdisplay.utils.NetworkUtil.buildSyncingUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebugFragment extends Fragment implements View.OnClickListener {

    private Button mFormat, mMemo, mCPU;
    public static final String BOARD_KEY = "board_type";
    private TextView mDebug;
    private PriceBoard priceBoard;
    private CustomBoard customBoard;
    private MessageBoard msgBoard;
    private SmartDisplay smartDisplay;
    int priceMSgNo;
    String ip_address, board;

    public DebugFragment() {
        // Required empty public constructor
    }

    public static DebugFragment getInstance() {
        return new DebugFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smartDisplay = new SmartDisplay();
        if (getArguments() != null) {
            board = getArguments().getString("board");
        }
        smartDisplay = GsonUtil.getGsonparser().fromJson(board, SmartDisplay.class);
        if (smartDisplay.getBoardType().equals(PRICE)) {
            priceBoard = new PriceBoard();
            priceBoard = smartDisplay.getPriceBoard();
            ip_address = priceBoard.getIpAddress();
            priceMSgNo = Integer.valueOf(priceBoard.getNoOfMsg());
        } else if (smartDisplay.getBoardType().equals(MESSAGE)) {
            msgBoard = new MessageBoard();
            msgBoard = smartDisplay.getMsgBoard();
            ip_address = msgBoard.getIpAddress();
        } else if (smartDisplay.getBoardType().equals(CUSTOM)) {
            customBoard = new CustomBoard();
            customBoard = smartDisplay.getCustomBoard();
            ip_address = customBoard.getIpAddress();
        }
        Toast.makeText(getActivity().getApplicationContext(), ip_address, Toast.LENGTH_SHORT).show();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        mFormat = (Button) view.findViewById(R.id.butFormat);
        mMemo = (Button) view.findViewById(R.id.butMem);
        mCPU = (Button) view.findViewById(R.id.butCPU);
        mDebug = (TextView) view.findViewById(R.id.debugView);
        return view;
    }

    public void onClick(View v) {
        if (v == mFormat) {

        } else if (v == mMemo) {

        } else if (v == mCPU) {


        }

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
            String error = e.getLocalizedMessage();
            if (error == null) {
                error = "error";
            }
            Log.d("InputStream", error);
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.


        @Override
        protected void onPostExecute(String result) {
            int ptr, ptr1, i;
            String dp = "";
            String ag = "";
            String ps = "";
            try {
                result = result.trim();
                result = result.replaceAll("\\s+", " ");
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }

            for (int j = 0; j < 8; j++) {
                if (result.contains("//M" + String.valueOf(j))) {
                    result = result.replace("//M" + String.valueOf(j), "//M" + String.valueOf(j) + "\n");
                }
            }
            if (result.contains("//PS")) {
                result = result.replace("//PS", "//PS\n");
            }

            if (result.contains("//AG")) {
                result = result.replace("//AG", "//AG\n");
            }
            if (result.contains("//DP")) {
                result =result.replace("//DP", "//DP\n");
            }
            if (result.contains("//ST")) {
                result = result.replace("//ST", "//ST\n");
            }
            mDebug.setText(result);

        }
    }

    public void sync() {
        String dataSync = null;
        try {
            if (priceBoard != null) {
                dataSync = buildSyncingUrl(priceBoard);
                Toast.makeText(getContext(), buildSyncingUrl(priceBoard), Toast.LENGTH_SHORT).show();
            }
            if (msgBoard != null) {
                dataSync = buildSyncingUrl(msgBoard);
                Toast.makeText(getContext(), buildSyncingUrl(msgBoard), Toast.LENGTH_SHORT).show();
            }
            if (customBoard != null) {
                dataSync = buildSyncingUrl(customBoard);
                Toast.makeText(getContext(), buildSyncingUrl(customBoard), Toast.LENGTH_SHORT).show();
            }
            new HttpAsyncTask().execute(dataSync);
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
        }
    }

}
