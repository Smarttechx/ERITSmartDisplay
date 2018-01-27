package com.kryptkode.cyberman.eritsmartdisplay.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.kryptkode.cyberman.eritsmartdisplay.models.SmartDisplay;
import com.kryptkode.cyberman.eritsmartdisplay.utils.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SMARTTECHX on 9/18/2017.
 */

public class SmartDisplayDB {
    private static final String SHARED_PREFS_FILE = "shared_prefs_file";
    private static final String BOARDS = "boards";
    private static final String LISTS = "lists";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SharedPreferences device_pref;
    private SharedPreferences.Editor  device_editor;
    // Context
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public SmartDisplayDB(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(SHARED_PREFS_FILE, PRIVATE_MODE);
        editor = pref.edit();
        device_pref = _context.getSharedPreferences(SHARED_PREFS_FILE, PRIVATE_MODE);
        device_editor = device_pref.edit();
    }

    public void saveDisplays(ArrayList<SmartDisplay> smartBoardList) {

        try {
            editor.putString(BOARDS, ObjectSerializer.serialize(smartBoardList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();


    }

    public ArrayList<SmartDisplay> loadDisplay() {
        ArrayList<SmartDisplay> smartBoardList = new ArrayList<>();
        if (pref != null) {
            try {
                smartBoardList = (ArrayList<SmartDisplay>) ObjectSerializer.deserialize(pref.getString(BOARDS,
                        ObjectSerializer.serialize(new ArrayList<SmartDisplay>())));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            smartBoardList = new ArrayList<SmartDisplay>();
        }
        return smartBoardList;
    }

    public void saveList(ArrayList<String> deviceList) {

        try {
            device_editor.putString(LISTS, ObjectSerializer.serialize(deviceList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        device_editor.commit();


    }

    public ArrayList<String> loadDevice() {
        ArrayList<String> deviceList = new ArrayList<>();
        if (device_pref != null) {
            try {
                deviceList= (ArrayList<String>) ObjectSerializer.deserialize(device_pref.getString(LISTS,
                        ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            deviceList = new ArrayList<String>();
        }
        return deviceList;
    }

}