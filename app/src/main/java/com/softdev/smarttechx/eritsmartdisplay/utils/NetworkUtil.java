package com.softdev.smarttechx.eritsmartdisplay.utils;

import android.content.Context;
import android.net.Uri;

import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
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
        Uri.Builder builder = Uri.parse(HTTP + msgBoard.getIpAddress() ).buildUpon()
                .appendPath(READ_DISPLAY);

        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String buildPriceBoardConfigUrl(PriceBoard priceBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + priceBoard.getIpAddress() ).buildUpon()
                .appendPath(BOARD_TYPE);
            builder.appendPath(String.valueOf(priceBoard.getPriceBoardCode())+String.valueOf(priceBoard.getFormat()));
        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String buildMessageBoardConfigUrl(MessageBoard msgBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + msgBoard.getIpAddress() ).buildUpon()
                .appendPath(BOARD_TYPE);
        builder.appendPath(String.valueOf(msgBoard.getMessageBoardCode())+String.valueOf(msgBoard.getFormat()));
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
            builder.appendPath(String.valueOf(customBoard.getCustomBoardCode())+String.valueOf(customBoard.getFormat()));
        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }
}
