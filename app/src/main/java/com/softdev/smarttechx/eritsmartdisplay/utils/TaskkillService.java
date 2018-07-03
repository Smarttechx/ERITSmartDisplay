package com.softdev.smarttechx.eritsmartdisplay.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class TaskkillService extends Service {

    private WifiHotspot mHotSpot;

    public TaskkillService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        mHotSpot = new WifiHotspot(this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
           mHotSpot.setUpWifiHotspot(false);

        stopSelf();
    }

}

