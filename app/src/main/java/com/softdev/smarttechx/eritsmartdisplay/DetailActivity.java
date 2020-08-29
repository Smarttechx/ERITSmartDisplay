package com.softdev.smarttechx.eritsmartdisplay;

import android.content.Intent;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;

public class DetailActivity extends AppCompatActivity {
    private SmartDisplay smartDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        smartDisplay=new SmartDisplay();
        Intent getData= getIntent();
        Bundle bundle= getData.getBundleExtra("Board");
        String getBoard= bundle.getString("BOARD");
        smartDisplay = GsonUtil.getGsonparser().fromJson(getBoard,SmartDisplay.class);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detail_root, DetailFragment.getInstance(smartDisplay));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

    }
}
