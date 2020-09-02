package com.softdev.smarttechx.eritsmartdisplay.utils;

import android.content.Context;
import android.net.Uri;

import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.DigitalClockBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cyberman on 7/1/2017.
 */

public abstract class NetworkUtil extends Context {
    public static final String HTTP  = "http://";
    public static final String TAG = NetworkUtil.class.getSimpleName();
    public static final String WRITE_TO_DISPLAY = "writeDisplay";
    public static final String READ_DISPLAY = "readDisplay";
    public static final String BOARD_TYPE = "boardType";
    public static  String res=null;


    public static String buildSyncingUrl(PriceBoard priceBoard){
        String url = null;
       Uri.Builder builder = Uri.parse(HTTP + priceBoard.getIpAddress() ).buildUpon()
                .appendPath(READ_DISPLAY);

        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }
    public static String buildSyncingUrl(CustomBoard customBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + customBoard.getIpAddress() ).buildUpon()
                .appendPath(READ_DISPLAY);

        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }
    public static String buildSyncingUrl(MessageBoard msgBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + msgBoard.getIpAddress()).buildUpon()
                .appendPath(READ_DISPLAY);

        try {
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildSyncingUrl(DigitalClockBoard digitalClockBoard) {
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + digitalClockBoard.getIpAddress()).buildUpon()
                .appendPath(READ_DISPLAY);

        try {
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildPriceBoardConfigUrl(PriceBoard priceBoard) {
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + priceBoard.getIpAddress()).buildUpon()
                .appendPath(BOARD_TYPE);
        builder.appendPath(String.valueOf(priceBoard.getPriceBoardCode()) + priceBoard.getFormat());
        try {
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildMessageBoardConfigUrl(MessageBoard msgBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + msgBoard.getIpAddress() ).buildUpon()
                .appendPath(BOARD_TYPE);
        builder.appendPath(String.valueOf(msgBoard.getMessageBoardCode()) + msgBoard.getFormat());
        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String buildCustomBoardConfigUrl(CustomBoard customBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + customBoard.getIpAddress() ).buildUpon()
                .appendPath(BOARD_TYPE);
        builder.appendPath(String.valueOf(customBoard.getCustomBoardCode()) + customBoard.getFormat());
        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String buildClockBoardConfigUrl(DigitalClockBoard dClockBoard) {
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + dClockBoard.getIpAddress()).buildUpon()
                .appendPath(BOARD_TYPE);
        builder.appendPath(String.valueOf(dClockBoard.getDigitalClockCode()) + dClockBoard.getFormat());
        try {
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }
}
