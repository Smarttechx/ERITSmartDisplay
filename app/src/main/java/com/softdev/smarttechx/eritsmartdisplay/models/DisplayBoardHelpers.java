package com.softdev.smarttechx.eritsmartdisplay.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.TreeMap;

import static com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard.MSG;
import static com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard.AGO;
import static com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard.DPK;
import static com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard.PMS;


/**
 * Created by Cyberman on 8/30/2017.
 */

public class DisplayBoardHelpers {
    public static final int PRICE_BOARD_DEC = 50;
    public static final int MESSAGE_BOARD_DEC = 100;
    public static final int CUSTOMIZE_BOARD_DEC = 150;

    public static final int DIGITAL_BOARD_DEC = 200;

    public static final String TAG = DisplayBoardHelpers.class.getSimpleName();
    Context context;

    public static TreeMap<String, String> parseMessageString(String data, int numOfMessagees) {
        TreeMap<String, String> messagesMap = new TreeMap<>();
        int index1, index2;
        for (int i = 1; i <= numOfMessagees; i++) {
            if (i == numOfMessagees) {
                index1 = data.indexOf(MSG + i); // //M1
                if (index1 != -1) {
                    messagesMap.put(MSG + i, data.substring(index1 + 4));
                    Log.i(TAG, "parseMessageString: " + index1);
                    Log.i(TAG, "parseMessageString: " + messagesMap.get(MSG + i));
                }
            } else {

                index1 = data.indexOf(MSG + i); // //M1
                index2 = data.indexOf(MSG + (i + 1)); // //M2

                if (index1 != -1 && index2 != -1) {
                    messagesMap.put(MSG + i, data.substring(index1 + 4, index2));
                    Log.i(TAG, "parseMessageString: " + messagesMap.get(MSG + i));
                }
            }

        }
        Log.i(TAG, "parseMessageString: " + messagesMap.get(MSG + 1));
        return messagesMap;
    }

    public static TreeMap<String, String> parsePriceString(String data) {
        //data format should be //A <data> //D <data> //P <data>
        //data should be in alphabetical order
        TreeMap<String, String> priceValuesTreeMap = new TreeMap<>();
        int index1, index2;
        String pmsData = "000:00";
        String dpkData = "000:00";
        String agoData = "000:00";
        if (!TextUtils.isEmpty(data)) {

            index1 = data.indexOf(AGO);
            index2 = data.indexOf(DPK);
            if (index1 != -1 && index2 != -1) {
                agoData = data.substring(index1 + 3, index2);
            }

            index1 = data.indexOf(DPK);
            index2 = data.indexOf(PMS);
            if (index1 != -1 && index2 != -1) {
                dpkData = data.substring(index1 + 3, index2);
            }

            index2 = data.indexOf(PMS);
            if (index2 != -1) {
                Log.i(TAG, "parsePriceString: " + index2);
                pmsData = data.substring(index2 + 3);
            }
        }
        priceValuesTreeMap.put(AGO, agoData);
        priceValuesTreeMap.put(DPK, dpkData);
        priceValuesTreeMap.put(PMS, pmsData);

        return priceValuesTreeMap;
    }

    public static String createMessageSendFormat(TreeMap<String, String> messagesTreeMap) {
        String messages = "";
        for (String key : messagesTreeMap.keySet()) {
            String msgTmap = messagesTreeMap.get(key);
            messages += key + " " + msgTmap + "";
           /* for(int i=1; i<=8;i++){
                if(key.equals("//M"+String.valueOf(i))){
                    messages += key + " " + msgTmap+" \n";
                    break;
                }
                else{
                    messages+="//M"+String.valueOf(i)+" "+ msgTmap+" \n";
                }
            }
*/
        }
        return messages;
    }

    public static String generateMesssageBoardCode(MessageBoard.MessageBoardType messageBoardType){
        Log.i(TAG, "generateMesssageBoardCode: " + Integer.toHexString(MESSAGE_BOARD_DEC + messageBoardType.getNumberOfCascades()));
        return Integer.toHexString(MESSAGE_BOARD_DEC + messageBoardType.getNumberOfCascades());
    }

    public static String generatePriceBoardCode(PriceBoard.PriceBoardType priceBoardType) {
        Log.i(TAG, "generatePriceBoardCode: " + Integer.toHexString(PRICE_BOARD_DEC + priceBoardType.getNumberOfCascades()));
        return Integer.toHexString(PRICE_BOARD_DEC + priceBoardType.getNumberOfCascades());
    }

    public static String generateCustomizeBoardCode(CustomBoard.CustomBoardType customBoardType) {
        Log.i(TAG, "generateCustomizeBoardCode: " + Integer.toHexString(CUSTOMIZE_BOARD_DEC + customBoardType.getNumberOfCascades()));
        return Integer.toHexString(CUSTOMIZE_BOARD_DEC + customBoardType.getNumberOfCascades());
    }

    public static String generateDigitalClockBoardCode(DigitalClockBoard.DigitalClockType digitalClockType) {
        Log.i(TAG, "generateCustomizeBoardCode: " + Integer.toHexString(DIGITAL_BOARD_DEC + digitalClockType.getNumberOfCascades()));
        return Integer.toHexString(DIGITAL_BOARD_DEC + digitalClockType.getNumberOfCascades());
    }


}
