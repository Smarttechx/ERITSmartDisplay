package com.softdev.smarttechx.eritsmartdisplay;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private SmartDisplay smartDisplay;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        smartDisplay = new SmartDisplay();
        Intent getData = getIntent();
        Bundle bundle= getData.getBundleExtra("Board");
        assert bundle != null;
        String getBoard= bundle.getString("BOARD");

        smartDisplay = GsonUtil.getGsonparser().fromJson(getBoard,SmartDisplay.class);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detail_root, DetailFragment.getInstance(smartDisplay));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

    }
}
