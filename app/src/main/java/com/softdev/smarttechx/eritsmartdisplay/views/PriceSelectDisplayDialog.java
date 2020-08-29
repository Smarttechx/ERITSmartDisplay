package com.softdev.smarttechx.eritsmartdisplay.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.softdev.smarttechx.eritsmartdisplay.R;
import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class PriceSelectDisplayDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    private PriceSelectDisplayDialogListener listener;

    private AppCompatSpinner appCompatSpinner;

    public static final String IS_IN_EDIT_MODE_KEY = "positive_button";
    public static final String BOARD_KEY = "price_board_key";

    private AlertDialog dialog;
    private TextInputEditText nameTextInputEditText;
    private TextInputEditText ipAddressTextInputEditText;
    private TextInputEditText textInputEditText;
    private boolean isEditing;
    private boolean hasUserSelected;
    private LinearLayout msgShow;
    private PriceBoard priceBoard;
    private String name, ipAddress, noOfMsg;
    private CheckBox memoFormat;
    int formatValue=0;

    public interface PriceSelectDisplayDialogListener {
        void onPriceDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard, boolean isEditing);
    }

    public static PriceSelectDisplayDialog getInstance(PriceBoard priceBoard,  boolean isEditing) {
        PriceSelectDisplayDialog priceSelectDisplayDialog = new PriceSelectDisplayDialog();
        String putPrice= GsonUtil.getGsonparser().toJson(priceBoard);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        bundle.putString(BOARD_KEY ,putPrice);
        priceSelectDisplayDialog.setArguments(bundle);
        return priceSelectDisplayDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PriceSelectDisplayDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString() + "must implement  PriceDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String getPrice= bundle.getString(BOARD_KEY);
        isEditing = bundle.getBoolean(IS_IN_EDIT_MODE_KEY);
        priceBoard = GsonUtil.getGsonparser().fromJson(getPrice,PriceBoard.class);
        name=priceBoard.getName();
        ipAddress=priceBoard.getIpAddress();
        noOfMsg=priceBoard.getNoOfMsg();
    }
    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        setUpSpinner();
        if (isEditing) {
            nameTextInputEditText.setText(name);
            ipAddressTextInputEditText.setText(ipAddress);
            textInputEditText.setText(noOfMsg);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View spinnerView = layoutInflater.inflate(R.layout.dialog_price_spinner, null);
        appCompatSpinner = (AppCompatSpinner) spinnerView.findViewById(R.id.dialog_price_board_spinner);
        appCompatSpinner.setOnItemSelectedListener(this);
        nameTextInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_boardName_et);
        textInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_num_of_msgs);
        ipAddressTextInputEditText = (TextInputEditText)  spinnerView.findViewById(R.id.add_ip_et);
        ipAddressTextInputEditText.addTextChangedListener(this);
        textInputEditText.addTextChangedListener(this);
        msgShow=(LinearLayout)spinnerView.findViewById(R.id.msgShow);
        memoFormat= (CheckBox) spinnerView.findViewById(R.id.checkMemoFormat);
        priceBoard=new PriceBoard();

        memoFormat.setChecked(false);
        memoFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    formatValue=1;
                }
                else{
                    formatValue=0;
                }
            }
        });

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (String split : splits) {
                            if (Integer.valueOf(split) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        ipAddressTextInputEditText.setFilters(filters);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(spinnerView);
        builder.setTitle(isEditing ? getString(R.string.edit_boad) : getString(R.string.add_new_baord));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    if(hasUserSelected==false){
                        Toast.makeText(getActivity().getApplicationContext(), "Board not saved,pls select a Board type", Toast.LENGTH_LONG).show();
                    }
                    else{
                        priceBoard.setName(nameTextInputEditText.getText().toString());
                        priceBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());
                        priceBoard.setFormat(formatValue);
                        if(msgShow.getVisibility()==View.VISIBLE){
                            priceBoard.setNoOfMsg(textInputEditText.getText().toString());
                        }else{
                            priceBoard.setNoOfMsg("0");
                        }
                        listener.onPriceDialogPositiveButtonClicked(PriceSelectDisplayDialog.this, priceBoard, isEditing);
                    }

                }catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "Board not saved,pls select a Board type", Toast.LENGTH_LONG).show();

                }

            }
        });
        if (isEditing) {
           //
            builder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Add save
                    try{
                        if(hasUserSelected==false){
                            Toast.makeText(getActivity().getApplicationContext(), "Board not saved,pls select a Board type", Toast.LENGTH_LONG).show();
                        }
                        else {
                            priceBoard.setName(nameTextInputEditText.getText().toString());
                            priceBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());
                            priceBoard.setFormat(formatValue);
                            if(msgShow.getVisibility()==View.VISIBLE){
                                priceBoard.setNoOfMsg(textInputEditText.getText().toString());
                            }else{
                                priceBoard.setNoOfMsg("0");

                            }
                            listener.onPriceDialogPositiveButtonClicked(PriceSelectDisplayDialog.this, priceBoard, isEditing);
                            Toast.makeText(getActivity().getApplicationContext(), "Board saved  successfully", Toast.LENGTH_LONG).show();

                        }
                    }catch(Exception e){
                        Toast.makeText(getActivity().getApplicationContext(), "Board not saved,pls select a Board type", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        return dialog;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if ((TextUtils.isEmpty(s))) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >0 ) {
            try {
                if(position<3){
                    msgShow.setVisibility(View.GONE);
                }
                else {
                    msgShow.setVisibility(View.VISIBLE);

                }
                priceBoard.setPriceBoardType(PriceBoard.getPriceBoardTypeFromInt( position));
                setImageResource(priceBoard.getPriceBoardType().getNumberOfCascades());
                //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(priceBoard.getPriceBoardType().getNumberOfCascades()), Toast.LENGTH_SHORT).show();
                hasUserSelected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
           showToast();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void showToast(){
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type, Toast.LENGTH_LONG).show();
    }

    //used to change the image resource or video for the proto_simutation of the display
    private void setImageResource(int priceSpinnerPosition) {
        // TODO: Implememt the method to change the image resouce
    }


    private void setUpSpinner(){
        int arrayResource = R.array.price_board_types;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayResource, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        appCompatSpinner.setAdapter(adapter);
    }
}
