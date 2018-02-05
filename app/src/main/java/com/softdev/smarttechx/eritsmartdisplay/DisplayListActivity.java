package com.softdev.smarttechx.eritsmartdisplay;

import android.app.Activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.softdev.smarttechx.eritsmartdisplay.data.SmartDisplayDB;
import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;


import java.util.ArrayList;


public class DisplayListActivity extends AppCompatActivity {
   // private LeDeviceListAdapter mLeDeviceListAdapter;
    private static final String TAG = "DeviceListActivity";
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private SmartDisplayDB displayDB;
    ArrayList<SmartDisplay> smartBoardList;
    String putBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);
        smartBoardList = new ArrayList<SmartDisplay>();
        displayDB = new SmartDisplayDB(this);
        smartBoardList=displayDB.loadDisplay();
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        setResult(Activity.RESULT_CANCELED);

        for (SmartDisplay display:smartBoardList) {
            if(display.getCustomBoard()!=null){
                mNewDevicesArrayAdapter.add(display.getCustomBoard().getName()+ "\t\t" + display.getCustomBoard().getIpAddress());
            }else if(display.getMsgBoard()!=null){
                mNewDevicesArrayAdapter.add(display.getMsgBoard().getName()+ "\t\t" + display.getMsgBoard().getIpAddress());
            }else if(display.getPriceBoard()!=null){
                mNewDevicesArrayAdapter.add(display.getPriceBoard().getName()+ "\t\t" + display.getPriceBoard().getIpAddress());
            }
            putBoard = GsonUtil.getGsonparser().toJson(display);
            mNewDevicesArrayAdapter.notifyDataSetChanged();
        }

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        // Get the local Bluetooth adapter
    }

    private void displayFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
       // FragmentTransaction transaction = manager.beginTransaction();
      //

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Start device discover with the BluetoothAdapter
     */

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("board",putBoard);
            DebugFragment newFrag = new DebugFragment();
            newFrag.setArguments(bundle);
            displayFragment(newFrag);
            finish();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
        mNewDevicesArrayAdapter.clear();
    }

}
