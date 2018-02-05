package com.softdev.smarttechx.eritsmartdisplay;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class DetailFragmentHelper {

    public static void dismissKeyboard(Context context, View view) {
        //hide the soft keyboard
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public static String generateDummyMessages(int i) {
        String[] messStrings = {"Cur lanista manducare?",
                "The unconditional blessing of pain is to desire with love.",
                "Everyone loves the pepperiness of melon mousse enameld with heated radish sprouts.",
                "Never mark a gull.",
                "Walk never like a mysterious transporter.",
                "Aliens view on wind at starfleet headquarters!",
                "A wonderful form of emptiness is the totality.",
                "The particle is more spacecraft now than green people. biological and pedantically colorful."
        };

        return messStrings[i - 1];
    }

    public static void saveMessages(Context context, PriceBoard priceBoard) {



    }

    public static void displayLoadingIndicatiors(View[] views, boolean shouldDisplay) {
        for (View view : views) {
            if(shouldDisplay){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.INVISIBLE);
            }
        }
    }
}
