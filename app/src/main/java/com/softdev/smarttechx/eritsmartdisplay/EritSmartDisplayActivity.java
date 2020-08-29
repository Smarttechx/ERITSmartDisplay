package com.softdev.smarttechx.eritsmartdisplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.softdev.smarttechx.eritsmartdisplay.adapters.HomeAdapter;
import com.softdev.smarttechx.eritsmartdisplay.data.SmartDisplayDB;
import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;
import com.softdev.smarttechx.eritsmartdisplay.utils.HttpConnection;
import com.softdev.smarttechx.eritsmartdisplay.utils.NetworkUtil;
import com.softdev.smarttechx.eritsmartdisplay.utils.WifiHotspot;
import com.softdev.smarttechx.eritsmartdisplay.views.CustomSelectDisplayDialog;
import com.softdev.smarttechx.eritsmartdisplay.views.MessageSelectDisplayDialog;
import com.softdev.smarttechx.eritsmartdisplay.views.PriceSelectDisplayDialog;

import java.util.ArrayList;


public class EritSmartDisplayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener, PriceSelectDisplayDialog.PriceSelectDisplayDialogListener,
        MessageSelectDisplayDialog.SelectDisplayDialogListener, CustomSelectDisplayDialog.SelectCustomDisplayDialogListener {
    public static final String TAG = EritSmartDisplayActivity.class.getSimpleName();
    public static final String POSITION_KEY = "position";
    public static final String FRAG_TAG = "frag";
    public static final String HOME_TAG = "home";
    public static final String BACK_STACK_KEY = "hey_key";
    public static final String PRICE = "priceBoard";
    public static final String DIGITAL = "digitalBoard";
    public static final String MESSAGE = "msgBoard";
    public static final String CUSTOM = "customBoard";
    private int currentPosition;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private HomeFragment homeFragment;
    private PriceBoard priceBoard;
    private MessageBoard msgBoard;
    String title;
    private CustomBoard customBoard;
    private WifiHotspot wifiHotspot;
    private boolean doubleBackToExitPressedOnce;
    private SmartDisplayDB displayDB;
    private SmartDisplay displayBoard;
    ArrayList<SmartDisplay> smartBoardList;
    int ID;
    SharedPreferences Gp;
    private WifiHotspot mHotSpot;
    private HomeAdapter homeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erit_smart_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        Gp = getSharedPreferences("setting", MODE_PRIVATE);
        title = getString(R.string.dashboard);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(title);
        }
        mHotSpot = new WifiHotspot(this);
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
        //display the home fragment
        if (homeFragment == null) {
            homeFragment = HomeFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.home_root, homeFragment, HOME_TAG);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
        smartBoardList = new ArrayList<SmartDisplay>();
        displayDB = new SmartDisplayDB(this);
        smartBoardList = displayDB.loadDisplay();
        homeAdapter = new HomeAdapter(this, smartBoardList);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION_KEY);
            selectItem(currentPosition);
        } else {
            setNavItemChecked(0);
        }
        wifiHotspot = new WifiHotspot(this);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                super.onBackPressed();
                fragmentManager.popBackStack(BACK_STACK_KEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                currentPosition = 0;
                setActionBarTitle(currentPosition);
                setNavItemChecked(currentPosition);

            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ACTIVITY ");
        ID = Gp.getInt("ID", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        if (!wifiHotspot.isHotspotOn()) {
            Log.i(TAG, "onStart:  WIFI");
            wifiHotspot.setUpWifiHotspot(true);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        if (wifiHotspot.isHotspotOn()) {
            Log.i(TAG, "onStop: WIFI");
            wifiHotspot.setUpWifiHotspot(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wifiHotspot.isHotspotOn()) {
            Log.i(TAG, "onStop: WIFI");
            wifiHotspot.setUpWifiHotspot(false);
        }
        smartBoardList = displayDB.loadDisplay();
        if (smartBoardList.isEmpty()) {
            ID = 0;
        }
        SharedPreferences.Editor e = Gp.edit();
        e.putInt("ID", ID);
        e.apply();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectItem(0);

        } else if (id == R.id.nav_setting) {
            selectItem(1);

        } else if (id == R.id.nav_about) {
            selectItem(2);

        }else if (id == R.id.nav_debug) {
            //TODO Implement share
            Intent devicelist= new Intent(EritSmartDisplayActivity.this, DisplayListActivity.class);
            startActivity(devicelist);
           // selectItem(3);

        } else if (id == R.id.nav_share) {
            //TODO Implement share
            shareThisApp();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareThisApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Site");
        intent.putExtra(Intent.EXTRA_TEXT, "To cater for all your embedded systems needs, check out \n http://erit.com.ng");
        Intent chooserIntent = Intent.createChooser(intent, "Share with...");
        startActivity(chooserIntent);
    }


    private void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_root, fragment, FRAG_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(BACK_STACK_KEY);
        transaction.commit();
    }

    private void selectItem(int position) {
// update the main content by replacing fragments
        currentPosition = position;
        Fragment fragment = null;
        switch (position) {
            case 0:
                getSupportFragmentManager().popBackStack(BACK_STACK_KEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 1:
                fragment = new SettingsFragment();
                break;
            case 2:
                fragment = AboutFragment.getInstance();
                break;
            case 3:

               fragment = DebugFragment.getInstance();
                break;
            default:
                fragment = HomeFragment.getInstance();

        }
        if (position != 0) {
            displayFragment(fragment);
        }
//Set the action bar title
        setActionBarTitle(position);
        setNavItemChecked(position);
//Close the drawer
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setNavItemChecked(int postion) {
        navigationView.getMenu().getItem(postion).setChecked(true);
    }

    private void setActionBarTitle(int position) {

        switch (position) {
            case 0:
                title = getString(R.string.dashboard);
                break;
            case 1:
                title = getString(R.string.action_settings);
                break;
            case 2:
                title = getString(R.string.about);
                break;
            case 3:
                title = getString(R.string.debug);
                break;
            default:
                title = getString(R.string.app_name);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void onPriceDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard, boolean isEditing) {
        this.priceBoard = priceBoard;

        if (isEditing) {
            updatePriceBoard();
        } else {
            savePriceBoard();

        }
        dialog.dismiss();

    }

    private void savePriceBoard() {

        HttpConnection httpconnection = new HttpConnection();
        httpconnection.execute(NetworkUtil.buildPriceBoardConfigUrl(priceBoard));
       /* Toast.makeText(this, NetworkUtil.buildPriceBoardConfigUrl(priceBoard).toString(), Toast.LENGTH_SHORT).show();*/
        displayBoard = new SmartDisplay(PRICE, priceBoard);
        smartBoardList.add(displayBoard);
        displayDB.saveDisplays(smartBoardList);
        Fragment fragment = HomeFragment.getInstance();
        displayFragment(fragment);

    }

    private void updatePriceBoard() {
        HttpConnection httpconnection = new HttpConnection();
        httpconnection.execute(NetworkUtil.buildPriceBoardConfigUrl(priceBoard));
        // Toast.makeText(this, NetworkUtil.buildPriceBoardConfigUrl(priceBoard).toString(), Toast.LENGTH_SHORT).show();
        smartBoardList.get(priceBoard.getId()).setPriceBoard(priceBoard);
        displayDB.saveDisplays(smartBoardList);
        homeAdapter.notifyDataSetChanged();
        Fragment fragment = HomeFragment.getInstance();
        displayFragment(fragment);

    }

    @Override
    public void onMessageDialogPositiveButtonClicked(DialogFragment dialog, MessageBoard msgBoard, boolean isEditing) {
        this.msgBoard = msgBoard;
        if (isEditing) {
            updateMsgBoard();
        } else {
            saveMsgBoard();
        }
        dialog.dismiss();
    }

    private void saveMsgBoard() {
        HttpConnection httpconnection = new HttpConnection();
        httpconnection.execute(NetworkUtil.buildMessageBoardConfigUrl(msgBoard));
        //  Toast.makeText(this, NetworkUtil.buildMessageBoardConfigUrl(msgBoard).toString(), Toast.LENGTH_SHORT).show();
        displayBoard = new SmartDisplay(MESSAGE, msgBoard);
        smartBoardList.add(displayBoard);
        displayDB.saveDisplays(smartBoardList);
        Fragment fragment = HomeFragment.getInstance();
        displayFragment(fragment);
    }

    private void updateMsgBoard() {
        HttpConnection httpconnection = new HttpConnection();
        httpconnection.execute(NetworkUtil.buildMessageBoardConfigUrl(msgBoard));
        //Toast.makeText(this, NetworkUtil.buildMessageBoardConfigUrl(msgBoard).toString(), Toast.LENGTH_SHORT).show();
        smartBoardList.get(msgBoard.getId()).setMsgBoard(msgBoard);
        displayDB.saveDisplays(smartBoardList);
        homeAdapter.notifyDataSetChanged();
        Fragment fragment = HomeFragment.getInstance();
        displayFragment(fragment);
    }

    @Override
    public void onCustomPositiveButtonClicked(DialogFragment dialog, CustomBoard customBoard, boolean isEditing) {
        this.customBoard = customBoard;
        if (isEditing) {
            updateCustomBoard();
        } else {
            saveCustomBoard();
        }
        dialog.dismiss();
    }

    private void saveCustomBoard() {
        HttpConnection httpconnection = new HttpConnection();
        httpconnection.execute(NetworkUtil.buildCustomBoardConfigUrl(customBoard));
        //  Toast.makeText(this, NetworkUtil.buildCustomBoardConfigUrl(customBoard).toString(), Toast.LENGTH_SHORT).show();
        displayBoard = new SmartDisplay(CUSTOM, customBoard);
        smartBoardList.add(displayBoard);
        displayDB.saveDisplays(smartBoardList);
        Fragment fragment = HomeFragment.getInstance();
        displayFragment(fragment);
    }

    private void updateCustomBoard() {
        //Toast.makeText(this, NetworkUtil.buildCustomBoardConfigUrl(customBoard), Toast.LENGTH_SHORT).show();
        HttpConnection httpconnection = new HttpConnection();
        httpconnection.execute(NetworkUtil.buildCustomBoardConfigUrl(customBoard));
        smartBoardList.get(customBoard.getId()).setCustomBoard(customBoard);
        displayDB.saveDisplays(smartBoardList);
        homeAdapter.notifyDataSetChanged();
        Fragment fragment = HomeFragment.getInstance();
        displayFragment(fragment);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mHotSpot.startHotSpot(false);
            Intent exit = new Intent(Intent.ACTION_MAIN);
            exit.addCategory(Intent.CATEGORY_HOME);
            exit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            exit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exit);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
