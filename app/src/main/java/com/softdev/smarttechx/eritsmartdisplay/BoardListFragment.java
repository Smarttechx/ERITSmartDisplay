package com.softdev.smarttechx.eritsmartdisplay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softdev.smarttechx.eritsmartdisplay.data.SmartDisplayDB;
import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardListFragment extends Fragment {

    private static final String TAG = "DeviceListActivity";
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private SmartDisplayDB displayDB;
    ArrayList<SmartDisplay> smartBoardList;
    String putBoard;
    public BoardListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
