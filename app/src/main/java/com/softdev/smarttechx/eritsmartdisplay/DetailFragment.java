package com.softdev.smarttechx.eritsmartdisplay;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.RequiresApi;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.DigitalClockBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;
import com.softdev.smarttechx.eritsmartdisplay.utils.HttpRequestUtil;
import com.softdev.smarttechx.eritsmartdisplay.utils.NumberAwareStringComparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.CUSTOM;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.DIGITAL;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.MESSAGE;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.PRICE;
import static com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard.MSG;
import static com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard.AGO;
import static com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard.DPK;
import static com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard.PMS;
import static com.softdev.smarttechx.eritsmartdisplay.utils.NetworkUtil.buildSyncingUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements TextView.OnEditorActionListener, TextWatcher {
    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER_ID = 400;
    public static final String BOARD_KEY = "board_type";

    private AppCompatSpinner messagesSpinner;
    private EditText messageEditText;
    private TextInputLayout messagesTextInputLayout;
    private EditText pmsThreeEditText;
    private EditText dpkThreeEditText;
    private EditText agoThreeEditText;
    private EditText pmsTwoEditText;
    private EditText dpkTwoEditText;
    private EditText agoTwoEditText;
    private EditText brtEditText;
    private EditText spdEditText;
    private ProgressBar loadingIndicatorProgressBar;
    private TextView loadingIndicatorTextView;
    private FloatingActionButton fab;
    private FloatingActionButton saveFloatingActionButton;
    private RelativeLayout priceRoot;
    SimpleDateFormat dfdata = new SimpleDateFormat("dd/MM/yyyy");
    private LinearLayout msgLayout;
    private Switch invSwitch;
    private LinearLayout digitalclockView;
    private Handler mHandler = new Handler();
    private TextView dateView;
    private PriceBoard priceBoard;
    private CustomBoard customBoard;
    private MessageBoard msgBoard;
    private TreeMap<String, String> messagesTreeMap = new TreeMap<>();
    private SmartDisplay smartDisplay;
    private int currentSelection;
    private int previousSelection;
    private boolean isFirstSelection;
    private boolean hasReloaded;
    private boolean firstTime = true;
    String formatTimeDate;

    private Fade fade;
    private ViewGroup rootViewGroup;
    int priceMSgNo;
    String invertValue;
    String brtValue, spdValue;

    private LinearLayout settingLayout;
    private Switch settingSwitch;
    private CheckBox dateTime;
    Calendar getCal = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("*HH:mm:ss*dd/MM/yy*");
    AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //get the message associated with the spinner and display on the edittext
            previousSelection = currentSelection;
            if (!isFirstSelection) {
                saveMessage(previousSelection + 1);
            }
            currentSelection = position;
            String key = MSG + (position + 1);
            String message = messagesTreeMap.containsKey(key) ? messagesTreeMap.get(key) : "";
            assert message != null;
            messageEditText.setText(message.trim());
            if (!TextUtils.isEmpty(message.trim())) {
                messagesTextInputLayout.setHint(getString(R.string.content_of_message) + " " + (position + 1));
            } else {
                messagesTextInputLayout.setHint(getString(R.string.enter_the_message) + " " + (position + 1));
            }
            if (messagesSpinner.getSelectedItemPosition() == 0) {
                String key1 = MSG + 1;
                String message1 = messagesTreeMap.containsKey(key1) ? messagesTreeMap.get(key1) : "";
                messageEditText.setText(message1);
            }
            isFirstSelection = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private static final String ALLOW_CHAR = "";
    private DigitalClockBoard digitalBoard;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        int id = v.getId();
        switch (id) {
            case R.id.edit_enter_message:
                TransitionManager.beginDelayedTransition(rootViewGroup, fade);
                saveMessage(messagesSpinner.getSelectedItemPosition() + 1);
                saveFloatingActionButton.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    private String baseURL;
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            String data = null;
            if (v == fab) {
                if (invSwitch.isChecked()) {
                    invertValue = "1";
                } else {
                    invertValue = "0";
                }
                if (brtEditText.getText().toString().length() == 0) {
                    brtValue = "00";
                } else if (brtEditText.getText().toString().length() == 1) {
                    brtValue = "0" + brtEditText.getText().toString();

                } else {
                    brtValue = brtEditText.getText().toString();
                }
                if (spdEditText.getText().toString().length() == 0) {
                    spdValue = "00";
                } else if (spdEditText.getText().toString().length() == 1) {
                    spdValue = "0" + spdEditText.getText().toString();
                } else {
                    spdValue = spdEditText.getText().toString();

                }
                if (dateTime.isChecked()) {
                    formatTimeDate = df.format(getCal.getTime());
                } else {
                    formatTimeDate = "";
                }
                if (pmsTwoEditText.getText().toString().length() == 0) {
                    pmsTwoEditText.setText("00");
                }
                if (agoTwoEditText.getText().toString().length() == 0) {
                    agoTwoEditText.setText("00");
                }
                if (dpkTwoEditText.getText().toString().length() == 0) {
                    dpkTwoEditText.setText("00");
                }
                brtEditText.setText(brtValue);
                spdEditText.setText(spdValue);
                if (priceBoard != null) {
                    priceBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                    priceBoard.setPriceValuesMap(createPriceTreeMap());
                    baseURL = "http://" + priceBoard.getIpAddress();
                    if (priceMSgNo > 0) {
                        priceBoard.setMsgsMap(messagesTreeMap);
                        data = "/writeDisplay" + priceBoard.createMessageSendFormat()
                                + priceBoard.createPriceSendFormat() + "//ST" + priceBoard.getBSI() + "//TD" + formatTimeDate;
                    } else {
                        data = "/writeDisplay" + priceBoard.createPriceSendFormat() + "//ST" + priceBoard.getBSI() + "//TD" + formatTimeDate;
                    }

                }
                if (msgBoard != null) {
                    msgBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                    msgBoard.setMessagesMap(messagesTreeMap);
                    baseURL = "http://" + msgBoard.getIpAddress();
                    data = "/writeDisplay" + msgBoard.createMessageSendFormat() + "//ST" + msgBoard.getBSI() + "//TD" + formatTimeDate;
                    //DetailFragmentHelper.saveMessages(getContext(), priceBoard);
                }
                if (customBoard != null) {
                    customBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                    customBoard.setCustomMap(messagesTreeMap);
                    baseURL = "http://" + customBoard.getIpAddress();
                    data = "/writeDisplay" + customBoard.createMessageSendFormat() + "//ST" + customBoard.getBSI() + "//TD" + formatTimeDate;
                    //DetailFragmentHelper.saveMessages(getContext(), priceBoard);
                }
                if (digitalBoard == null) {
                    assert data != null;
                    data = data.trim();
                    data = data.replaceAll("\\s+", " ");
                    for (int j = 1; j <= 8; j++) {
                        if (j < 8) {
                            if (data.contains("//M" + j)) {
                                data = data.replace("//M" + (j + 1), " //M" + (j + 1));
                            } else if (!data.contains("//M" + j)) {
                                data = data + " //M" + j;
                            }
                        }
                    }
                } else {
                    baseURL = "http://" + digitalBoard.getIpAddress();
                    Log.v("URL", baseURL);
                    digitalBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                    data = "/writeDisplay" + "//ST" + digitalBoard.getBSI() + "//TD" + formatTimeDate;
                }


             /*   Connection postData= new Connection();
                postData.ByPostMethod(baseURL,data);*/

                HttpRequestUtil httpconnection = new HttpRequestUtil();
                Log.v("URL", baseURL);
                Log.v("DiplayData", data);
                httpconnection.execute(baseURL, "POST " + data);
                Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.fragment_root), "Saving...", Snackbar.LENGTH_LONG).show();

                //TODO Implement send
                //Use the Connection.java class and the NetworkUtils.java classes
                //Use .createPriceSendFormat and createMessageSendFormat on the priceBoardInstance to create the formats
                // //M1 <message one> //M2 <Message 2> .. and //A<ago_price>//D<dpk_price> //P<pms_price>
            }
            if (v == saveFloatingActionButton) {
                TransitionManager.beginDelayedTransition(rootViewGroup, fade);
                saveMessage(messagesSpinner.getSelectedItemPosition() + 1);
                saveFloatingActionButton.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }


        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            TransitionManager.beginDelayedTransition(rootViewGroup, fade);
            if (!hasReloaded) {
                saveFloatingActionButton.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                hasReloaded = false;

                if (firstTime) {
                    saveFloatingActionButton.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    firstTime = false;
                }
            }
        } else {
            saveFloatingActionButton.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);

        }

    }


    public interface DetailFragmentListener {
        void onSyncButtonClicked();

        void onSaveButtonClicked(PriceBoard priceBoard);
    }

    public static DetailFragment getInstance(SmartDisplay displayBoard) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        String putBoard = GsonUtil.getGsonparser().toJson(displayBoard);
        Log.v("putBoard", putBoard);
        bundle.putString(BOARD_KEY, putBoard);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        String getBoard = bundle.getString(BOARD_KEY);
        smartDisplay = GsonUtil.getGsonparser().fromJson(getBoard, SmartDisplay.class);
        if (smartDisplay.getBoardType().equals(PRICE)) {
            priceBoard = new PriceBoard();
            priceBoard = smartDisplay.getPriceBoard();
            priceMSgNo = Integer.parseInt(priceBoard.getNoOfMsg());
        } else if (smartDisplay.getBoardType().equals(MESSAGE)) {
            msgBoard = new MessageBoard();
            msgBoard = smartDisplay.getMsgBoard();
        } else if (smartDisplay.getBoardType().equals(CUSTOM)) {
            customBoard = new CustomBoard();
            customBoard = smartDisplay.getCustomBoard();
        } else if (smartDisplay.getBoardType().equals(DIGITAL)) {
            digitalBoard = new DigitalClockBoard();
            digitalBoard = smartDisplay.getDigitalBoard();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void saveMessage(int postion) {
        String key = MSG + postion;
        String text = messageEditText.getText().toString() + " ";
        messagesTreeMap.put(key, text);
        DetailFragmentHelper.dismissKeyboard(getContext(), getView());
        if (!isFirstSelection) {
            // Toast.makeText(getContext(), R.string.temp_saved, Toast.LENGTH_LONG).show();
        }
    }

    public void saveSyncMessage(int postion, String msg) {
        String key = MSG + postion;
        String text = msg + " ";
        messagesTreeMap.put(key, text);
        DetailFragmentHelper.dismissKeyboard(getContext(), getView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        isFirstSelection = true;
        messagesSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        messagesTextInputLayout = (TextInputLayout) view.findViewById(R.id.edit_enter_message_text_input_layout);
        messageEditText = (EditText) view.findViewById(R.id.edit_enter_message);
        dpkThreeEditText = (EditText) view.findViewById(R.id.dpk_000);
        dpkTwoEditText = (EditText) view.findViewById(R.id.dpk_00);
        pmsThreeEditText = (EditText) view.findViewById(R.id.pms_000);
        pmsTwoEditText = (EditText) view.findViewById(R.id.pms_00);
        agoThreeEditText = (EditText) view.findViewById(R.id.ago_000);
        agoTwoEditText = (EditText) view.findViewById(R.id.ago_00);

        dateView = view.findViewById(R.id.dateView);
        brtEditText = (EditText) view.findViewById(R.id.BRT00);
        spdEditText = (EditText) view.findViewById(R.id.SPD00);
        invSwitch = (Switch) view.findViewById(R.id.switchinv);
        msgLayout = (LinearLayout) view.findViewById(R.id.msgView);
        settingLayout = (LinearLayout) view.findViewById(R.id.settingLayout);
        settingSwitch = (Switch) view.findViewById(R.id.switchSetting);

        priceRoot = (RelativeLayout) view.findViewById(R.id.price_root);
        digitalclockView = view.findViewById(R.id.digitalclockView);
        messagesSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        messageEditText.setOnEditorActionListener(this);
        messageEditText.addTextChangedListener(this);
        messageEditText.setImeActionLabel(getString(R.string.save), EditorInfo.IME_ACTION_DONE);

        loadingIndicatorProgressBar = (ProgressBar) view.findViewById(R.id.loading_indicator);
        loadingIndicatorTextView = (TextView) view.findViewById(R.id.tv_loading);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        saveFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.saveFloatingActionButton);
        dateTime = (CheckBox) view.findViewById(R.id.checkTimeDate);
        fab.setOnClickListener(fabClickListener);
        saveFloatingActionButton.setOnClickListener(fabClickListener);
        settingLayout.setVisibility(View.GONE);
        settingSwitch.setChecked(false);
        invSwitch.setChecked(false);
        dateTime.setChecked(false);
        dateView.setText(dfdata.format(getCal.getTime()));
        dateTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    formatTimeDate = df.format(getCal.getTime());

                } else {
                    formatTimeDate = "";
                }
            }
        });

        settingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingLayout.setVisibility(View.VISIBLE);
                } else {
                    settingLayout.setVisibility(View.GONE);
                }
            }
        });
        invSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    invSwitch.setText("INVERT");
                    invertValue = "1";

                } else {

                    invSwitch.setText("NOT INVERT");
                    invertValue = "0";

                }
            }
        });

        fade = new Fade();
        fade.setDuration(500);
        fade.addTarget(fab);
        fade.addTarget(saveFloatingActionButton);
        rootViewGroup = (ViewGroup) view.findViewById(R.id.fragment_root);
        return view;
    }

    private void setUpSpinner() {
        if (priceBoard != null) {
            int priceMSgNo = Integer.parseInt(priceBoard.getNoOfMsg());
            if (priceMSgNo > 0) {
                if (priceBoard.getMsgsMap() != null) {
                    messagesTreeMap = priceBoard.getMsgsMap();
                    //  Log.i(TAG, "setUpSpinner: " + priceBoard.getMessagesMap().toString());
                } else {
                    messagesTreeMap = new TreeMap<>(new NumberAwareStringComparator());
                    for (int i = 1; i <= priceMSgNo; i++) {
                        messagesTreeMap.put(MSG + i, "");
                    }
                }
                List<String> spinnerEntries = new ArrayList<>();
                for (int i = 1; i <= priceMSgNo; i++) {
                    spinnerEntries.add(getString(R.string.message_x, i));
                }
                Log.v(TAG, "Array-->" + spinnerEntries.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_spinner_item, spinnerEntries
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                messagesSpinner.setAdapter(adapter);
                messagesSpinner.setSelection(currentSelection, true);
            } else {
                msgLayout.setVisibility(View.GONE);
            }
        } else if (msgBoard != null) {
            priceRoot.setVisibility(View.GONE);
            int MsgNo = Integer.valueOf(msgBoard.getNoOfMsg());
            if (MsgNo > 0) {
                if (msgBoard.getMessagesMap() != null) {
                    messagesTreeMap = msgBoard.getMessagesMap();
                    //  Log.i(TAG, "setUpSpinner: " + priceBoard.getMessagesMap().toString());
                } else {
                    messagesTreeMap = new TreeMap<>(new NumberAwareStringComparator());
                    for (int i = 1; i <= MsgNo; i++) {
                        messagesTreeMap.put(MSG + i, "");
                    }
                }
                List<String> spinnerEntries = new ArrayList<>();
                for (int i = 1; i <= MsgNo; i++) {
                    spinnerEntries.add(getString(R.string.message_x, i));
                }
                Log.v(TAG, "Array-->" + spinnerEntries.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_spinner_item, spinnerEntries
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                messagesSpinner.setAdapter(adapter);
                messagesSpinner.setSelection(currentSelection, true);
            }
        } else if (customBoard != null) {
            priceRoot.setVisibility(View.GONE);

            if (customBoard.getCustomMap() != null) {
                messagesTreeMap = customBoard.getCustomMap();
                //  Log.i(TAG, "setUpSpinner: " + priceBoard.getMessagesMap().toString());
            } else {
                messagesTreeMap = new TreeMap<>(new NumberAwareStringComparator());
                for (int i = 1; i <= 8; i++) {
                    messagesTreeMap.put(MSG + i, "");
                }
            }
            List<String> spinnerEntries = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                spinnerEntries.add(getString(R.string.message_x, i));
            }
            Log.v(TAG, "Array-->" + spinnerEntries.size());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, spinnerEntries
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            messagesSpinner.setAdapter(adapter);
            messagesSpinner.setSelection(currentSelection, true);
        } else if (digitalBoard != null) {
            priceRoot.setVisibility(View.GONE);
            msgLayout.setVisibility(View.GONE);
            digitalclockView.setVisibility(View.VISIBLE);
            dateTime.setChecked(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpSpinner();
        if (invSwitch.isChecked()) {
            invSwitch.setText("INVERT");
        } else {
            invSwitch.setText("NOT INVERT");
        }

        sync();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sync();
            }
        }, 500);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messagesSpinner.getSelectedItemPosition() == 0) {
                    String key = MSG + 1;
                    String message = messagesTreeMap.containsKey(key) ? messagesTreeMap.get(key) : "";
                    messageEditText.setText(message);
                    //Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);

        if (messageEditText.getText().toString().equals("") || messageEditText.getText().toString().isEmpty()) {
            String key = MSG + 1;
            String message = messagesTreeMap.containsKey(key) ? messagesTreeMap.get(key) : "";
            messageEditText.setText(message);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_smart_display, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_sync:
                sync();
                if (messagesSpinner.getSelectedItemPosition() == 0) {
                    String key = MSG + 1;
                    String message = messagesTreeMap.containsKey(key) ? messagesTreeMap.get(key) : "";
                    messageEditText.setText(message);
                    //Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
                DetailFragmentHelper.displayLoadingIndicatiors(new View[]{loadingIndicatorTextView,
                        loadingIndicatorProgressBar}, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private TreeMap<String, String> createPriceTreeMap() {
        TreeMap<String, String> priceValuesTreeMap = new TreeMap<>();
        String pmsData = pmsThreeEditText.getText().toString() + ":" + pmsTwoEditText.getText().toString() + " ";
        String dpkData = dpkThreeEditText.getText().toString() + ":" + dpkTwoEditText.getText().toString() + " ";
        String agoData = agoThreeEditText.getText().toString() + ":" + agoTwoEditText.getText().toString() + " ";

        priceValuesTreeMap.put(AGO, agoData);
        priceValuesTreeMap.put(DPK, dpkData);
        priceValuesTreeMap.put(PMS, pmsData);

        return priceValuesTreeMap;
    }

    private TreeMap<String, String> syncPriceTreeMap(String pmsData, String dpkData, String agoData) {
        TreeMap<String, String> priceValuesTreeMap = new TreeMap<>();
        priceValuesTreeMap.put(AGO, agoData);
        priceValuesTreeMap.put(DPK, dpkData);
        priceValuesTreeMap.put(PMS, pmsData);

        return priceValuesTreeMap;
    }

    private void decodePriceTreeMap(TreeMap<String, String> priceValuesMap) {
        pmsThreeEditText.setText(priceValuesMap.get(PMS).split(":")[0].trim());
        pmsTwoEditText.setText(priceValuesMap.get(PMS).split(":")[1].trim());
        dpkThreeEditText.setText(priceValuesMap.get(DPK).split(":")[0].trim());
        dpkTwoEditText.setText(priceValuesMap.get(DPK).split(":")[1].trim());
        agoThreeEditText.setText(priceValuesMap.get(AGO).split(":")[0].trim());
        agoTwoEditText.setText(priceValuesMap.get(AGO).split(":")[1].trim());

        if (messagesSpinner.getSelectedItemPosition() == 0) {
            String key1 = MSG + 1;
            String message1 = messagesTreeMap.containsKey(key1) ? messagesTreeMap.get(key1) : "";
            messageEditText.setText(message1);
        }
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            URL urlObj = new URL(url);

            // make GET request to the given URL
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.connect();

            // receive response as inputStream
            inputStream = urlConnection.getInputStream();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            String error=e.getLocalizedMessage();
            if(error==null){
               error="error";
            }
            Log.d("InputStream",error);
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.


        @Override
        protected void onPostExecute(String result) {
            int ptr, ptr1, i;
            String dp = "";
            String ag = "";
            String ps = "";
            try {
                result = result.trim();
                result = result.replaceAll("\\s+", " ");
                // Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                try {
                    result = URLDecoder.decode(result);
                    // Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
                if (result.contains("%20")) {
                    result = result.replaceAll("%20", " ");
                    result = result.trim();
                    result = result.replaceAll("\\s+", " ");
                }
                if (result.contains("20")) {
                    result = result.replaceAll("20", "");
                    result = result.trim();
                    result = result.replaceAll("\\s+", " ");
                }

                // Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }

            //etResponse.setText(result);
            if (priceBoard != null) {
                int priceMSgNo = Integer.valueOf(priceBoard.getNoOfMsg());
                if (priceMSgNo > 0) {
                    try {
                        for (int j = 1; j <= priceMSgNo; j++) {
                            if (j < priceMSgNo) {
                                ptr = result.indexOf("//M" + j);
                                ptr1 = result.indexOf("//M" + (j + 1));
                                if ((ptr != -1) && (ptr1 != -1)) {
                                    saveSyncMessage(j, result.substring(ptr + 4, ptr1).trim());
                                }
                            } else if (j == priceMSgNo) {
                                ptr = result.indexOf("//M" + j);
                                ptr1 = result.indexOf("//PS");
                                if ((ptr != -1) && (ptr1 != -1)) {
                                    saveSyncMessage(j, result.substring(ptr + 4, ptr1).trim());
                                }
                                ptr = result.indexOf("//PS");
                                if ((ptr != -1) && (ptr1 != -1)) {
                                    ps = result.substring(ptr + 4, ptr + 7) + result.substring(ptr + 7, ptr + 10) + " ";
                                    // Toast.makeText(getActivity().getApplicationContext(), ps, Toast.LENGTH_SHORT).show();
                                }

                                ptr = result.indexOf("//AG");
                                if ((ptr != -1) && (ptr1 != -1)) {
                                    ag = result.substring(ptr + 4, ptr + 7) + result.substring(ptr + 7, ptr + 10) + " ";
                                }

                                ptr = result.indexOf("//DP");
                                if ((ptr != -1) && (ptr1 != -1)) {
                                    dp = result.substring(ptr + 4, ptr + 7) + result.substring(ptr + 7, ptr + 10) + " ";
                                }
                                priceBoard.setPriceValuesMap(syncPriceTreeMap(ps, ag, dp));
                                decodePriceTreeMap(syncPriceTreeMap(ps, ag, dp));
                                ptr = result.indexOf("//ST");
                                if ((ptr != -1) && (ptr1 != -1)) {
                                    String checkT = result.substring(ptr + 4, ptr + 6);
                                    if (checkT.substring(0, 1).equals(("T"))) {
                                        brtEditText.setText(result.substring(ptr + 5, ptr + 7));
                                        spdEditText.setText(result.substring(ptr + 7, ptr + 9));
                                        invertValue = result.substring(ptr + 9, ptr + 10);
                                    } else if (result.substring(ptr + 4, ptr + 6).equals(" ")) {
                                        brtEditText.setText("00");
                                        spdEditText.setText("00");
                                        invertValue = result.substring(ptr + 8, ptr + 9);
                                    } else {
                                        brtEditText.setText(result.substring(ptr + 4, ptr + 6));
                                        spdEditText.setText(result.substring(ptr + 6, ptr + 8));
                                        invertValue = result.substring(ptr + 8, ptr + 9);
                                    }
                                    if (invertValue.equals("1")) {
                                        invSwitch.setText("INVERT");
                                        invSwitch.setChecked(true);
                                    } else {
                                        invSwitch.setText("NOT INVERT");
                                        invSwitch.setChecked(false);
                                    }

                                }
                                priceBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                            }

                        }

                    } catch (Exception e) {

                    }
                } else {
                    try {
                        ptr1 = result.indexOf("//PS");
                        ptr = result.indexOf("//PS");
                        if ((ptr != -1) && (ptr1 != -1)) {
                            ps = result.substring(ptr + 4, ptr + 7) + result.substring(ptr + 7, ptr + 10) + " ";
                        }

                        ptr = result.indexOf("//AG");
                        if ((ptr != -1) && (ptr1 != -1)) {
                            ag = result.substring(ptr + 4, ptr + 7) + result.substring(ptr + 7, ptr + 10) + " ";
                        }

                        ptr = result.indexOf("//DP");
                        if ((ptr != -1) && (ptr1 != -1)) {
                            dp = result.substring(ptr + 4, ptr + 7) + result.substring(ptr + 7, ptr + 10) + " ";
                        }
                        priceBoard.setPriceValuesMap(syncPriceTreeMap(ps, ag, dp));
                        decodePriceTreeMap(syncPriceTreeMap(ps, ag, dp));
                        ptr = result.indexOf("//ST");
                        if ((ptr != -1) && (ptr1 != -1)) {
                            String checkT = result.substring(ptr + 4, ptr + 6);
                            if (checkT.substring(0, 1).equals(("T"))) {
                                brtEditText.setText(result.substring(ptr + 5, ptr + 7));
                                spdEditText.setText(result.substring(ptr + 7, ptr + 9));
                                invertValue = result.substring(ptr + 9, ptr + 10);
                            } else if (result.substring(ptr + 4, ptr + 6).equals(" ")) {
                                brtEditText.setText("00");
                                spdEditText.setText("00");
                                invertValue = result.substring(ptr + 8, ptr + 9);
                            } else {
                                brtEditText.setText(result.substring(ptr + 4, ptr + 6));
                                spdEditText.setText(result.substring(ptr + 6, ptr + 8));
                                invertValue = result.substring(ptr + 8, ptr + 9);
                            }
                            if (invertValue.equals("1")) {
                                invSwitch.setText("INVERT");
                                invSwitch.setChecked(true);
                            } else {
                                invSwitch.setText("NOT INVERT");
                                invSwitch.setChecked(false);
                            }

                        }
                        priceBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                    } catch (Exception e) {

                    }
                }
            } else if (msgBoard != null) {
                int MSgNo = Integer.valueOf(msgBoard.getNoOfMsg());
                try {
                    for (int j = 1; j <= MSgNo; j++) {
                        if (j < MSgNo) {
                            ptr = result.indexOf("//M" + j);
                            ptr1 = result.indexOf("//M" + (j + 1));
                            if ((ptr != -1) && (ptr1 != -1)) {
                                saveSyncMessage(j, result.substring(ptr + 4, ptr1).trim());
                            }
                        } else if (j == MSgNo) {
                            ptr = result.indexOf("//M" + j);
                            ptr1 = result.indexOf("//PS");
                            if ((ptr != -1) && (ptr1 != -1)) {
                                saveSyncMessage(j, result.substring(ptr + 4, ptr1).trim());
                            }
                            ptr = result.indexOf("//ST");
                            if ((ptr != -1) && (ptr1 != -1)) {
                                String checkT = result.substring(ptr + 4, ptr + 6);
                                if (checkT.substring(0, 1).equals(("T"))) {
                                    brtEditText.setText(result.substring(ptr + 5, ptr + 7));
                                    spdEditText.setText(result.substring(ptr + 7, ptr + 9));
                                    invertValue = result.substring(ptr + 9, ptr + 10);
                                } else if (result.substring(ptr + 4, ptr + 6).equals(" ")) {
                                    brtEditText.setText("00");
                                    spdEditText.setText("00");
                                    invertValue = result.substring(ptr + 8, ptr + 9);
                                } else {
                                    brtEditText.setText(result.substring(ptr + 4, ptr + 6));
                                    spdEditText.setText(result.substring(ptr + 6, ptr + 8));
                                    invertValue = result.substring(ptr + 8, ptr + 9);
                                }
                                if (invertValue.equals("1")) {
                                    invSwitch.setText("INVERT");
                                    invSwitch.setChecked(true);
                                } else {
                                    invSwitch.setText("NOT INVERT");
                                    invSwitch.setChecked(false);
                                }

                            }
                            msgBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                        }

                    }

                } catch (Exception e) {

                }

            } else if (customBoard != null) {
                try {
                    for (int j = 1; j <= 8; j++) {
                        if (j < 8) {
                            ptr = result.indexOf("//M" + j);
                            ptr1 = result.indexOf("//M" + (j + 1));
                            if ((ptr != -1) && (ptr1 != -1)) {
                                saveSyncMessage(j, result.substring(ptr + 4, ptr1).trim());
                            }
                        } else if (j == 8) {
                            ptr = result.indexOf("//M" + j);
                            ptr1 = result.indexOf("//PS");
                            if ((ptr != -1) && (ptr1 != -1)) {
                                saveSyncMessage(j, result.substring(ptr + 4, ptr1).trim());
                            }
                            ptr = result.indexOf("//ST");
                            if ((ptr != -1) && (ptr1 != -1)) {
                                String checkT = result.substring(ptr + 4, ptr + 6);
                                if (checkT.substring(0, 1).equals(("T"))) {
                                    brtEditText.setText(result.substring(ptr + 5, ptr + 7));
                                    spdEditText.setText(result.substring(ptr + 7, ptr + 9));
                                    invertValue = result.substring(ptr + 9, ptr + 10);
                                } else if (result.substring(ptr + 4, ptr + 6).equals(" ")) {
                                    brtEditText.setText("00");
                                    spdEditText.setText("00");
                                    invertValue = result.substring(ptr + 8, ptr + 9);
                                } else {
                                    brtEditText.setText(result.substring(ptr + 4, ptr + 6));
                                    spdEditText.setText(result.substring(ptr + 6, ptr + 8));
                                    invertValue = result.substring(ptr + 8, ptr + 9);
                                }
                                if (invertValue.equals("1")) {
                                    invSwitch.setText("INVERT");
                                    invSwitch.setChecked(true);
                                } else {
                                    invSwitch.setText("NOT INVERT");
                                    invSwitch.setChecked(false);
                                }

                            }
                            customBoard.setBSI(brtEditText.getText().toString() + spdEditText.getText().toString() + invertValue);
                        }

                    }
                } catch (Exception e) {

                }

            }

        }
    }

    public void sync() {
        String dataSync = null;
        try {
            if (priceBoard != null) {
                dataSync = buildSyncingUrl(priceBoard);
                // Toast.makeText(getContext(), buildSyncingUrl(priceBoard), Toast.LENGTH_SHORT).show();
            }
            if (msgBoard != null) {
                dataSync = buildSyncingUrl(msgBoard);
                // Toast.makeText(getContext(), buildSyncingUrl(msgBoard), Toast.LENGTH_SHORT).show();
            }
            if (customBoard != null) {
                dataSync = buildSyncingUrl(customBoard);
                // Toast.makeText(getContext(), buildSyncingUrl(customBoard), Toast.LENGTH_SHORT).show();
            }

            if (digitalBoard != null) {
                dataSync = buildSyncingUrl(digitalBoard);
                // Toast.makeText(getContext(), buildSyncingUrl(customBoard), Toast.LENGTH_SHORT).show();
            }
            new HttpAsyncTask().execute(dataSync);
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
        }
    }

}
