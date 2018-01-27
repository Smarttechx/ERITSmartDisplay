package com.kryptkode.cyberman.eritsmartdisplay.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by SMARTTECHX on 11/29/2017.
 */

public class GsonUtil {
    private static Gson gson;
    public static  Gson getGsonparser(){
        if(null==gson){
            GsonBuilder builder = new GsonBuilder();
            gson= builder.create();
        }
        return gson;
    }
}
