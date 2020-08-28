package com.softdev.smarttechx.eritsmartdisplay.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.softdev.smarttechx.eritsmartdisplay.R;
import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;

/**
 * Created by SMARTTECHX on 10/20/2017.
 */

public class CustomSelectDisplayDialog extends DialogFragment implements TextWatcher, AdapterView.OnItemSelectedListener {
    public static final String IS_IN_EDIT_MODE_KEY = "positive_button";
    public static final String CustomBOARD_KEY = "Customboard_key";
    private SelectCustomDisplayDialogListener listener;

    private AlertDialog dialog;
    private TextInputEditText nameTextInputEditText;
    private TextInputEditText ipAddressTextInputEditText;
    private CustomBoard customBoard;
    private AppCompatSpinner appCompatSpinner;
    private String name, ipAddress, noOfMsg;
    private boolean hasUserSelected;
    private CheckBox memoFormat;
    int formatValue = 0;

    private boolean isEditing;

    public static CustomSelectDisplayDialog getInstance(CustomBoard customBoard, boolean isEditing) {
        CustomSelectDisplayDialog editCustomBoard = new CustomSelectDisplayDialog();
        String putCustom = GsonUtil.getGsonparser().toJson(customBoard);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        bundle.putString(CustomBOARD_KEY, putCustom);
        //bundle.putSerializable(CustomBOARD_KEY, customBoard);
        editCustomBoard.setArguments(bundle);
        return editCustomBoard;
    }

    public interface SelectCustomDisplayDialogListener {

        void onCustomPositiveButtonClicked(DialogFragment dialog, CustomBoard customBoard, boolean isEditing);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            try {
                customBoard.setCustomBoardType(CustomBoard.getCustomBoardTypeFromInt(position));
                hasUserSelected = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((nameTextInputEditText.getText().toString().length() > 0) && (ipAddressTextInputEditText.getText().toString().length() > 0)) {
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
            }
        } else {
            showToast();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        hasUserSelected = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SelectCustomDisplayDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString() + "must implement CustomDialogListener");
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
        String getCustom = bundle.getString(CustomBOARD_KEY);
        isEditing = bundle.getBoolean(IS_IN_EDIT_MODE_KEY);
        customBoard = GsonUtil.getGsonparser().fromJson(getCustom, CustomBoard.class);
        name = customBoard.getName();
        ipAddress = customBoard.getIpAddress();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_custom_spinner, null);
        nameTextInputEditText = (TextInputEditText) view.findViewById(R.id.add_boardName_et);
        ipAddressTextInputEditText = (TextInputEditText) view.findViewById(R.id.add_ip_et);
        appCompatSpinner = (AppCompatSpinner) view.findViewById(R.id.dialog_custom_board_spinner);
        memoFormat = (CheckBox) view.findViewById(R.id.checkMemoFormat);
        ipAddressTextInputEditText.addTextChangedListener(this);
        appCompatSpinner.setOnItemSelectedListener(this);
        customBoard = new CustomBoard();
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
        memoFormat.setChecked(false);
        memoFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    formatValue = 1;
                } else {
                    formatValue = 0;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle(isEditing ? getString(R.string.edit_boad) : getString(R.string.add_new_baord));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (hasUserSelected == false) {
                        Toast.makeText(getActivity().getApplicationContext(), "Board not saved, pls select a Board type", Toast.LENGTH_SHORT).show();
                    } else {
                        customBoard.setName(nameTextInputEditText.getText().toString());
                        customBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());
                        customBoard.setNoOfMsg("8");
                        customBoard.setFormat(formatValue);
                        listener.onCustomPositiveButtonClicked(CustomSelectDisplayDialog.this, customBoard, isEditing);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Board not saved, pls select a Board type", Toast.LENGTH_SHORT).show();

                }
            }
        });
        if (isEditing) {
            builder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (hasUserSelected == false) {
                            Toast.makeText(getActivity().getApplicationContext(), "Board not saved, pls select a Board type", Toast.LENGTH_SHORT).show();
                        } else {
                            customBoard.setName(nameTextInputEditText.getText().toString());
                            customBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());
                            customBoard.setNoOfMsg("8");
                            customBoard.setFormat(formatValue);
                            listener.onCustomPositiveButtonClicked(CustomSelectDisplayDialog.this, customBoard, isEditing);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Board not saved, pls select a Board type", Toast.LENGTH_SHORT).show();

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


        //initially disable the button
//        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        return dialog;
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        setUpSpinner();
        if (isEditing) {
            nameTextInputEditText.setText(name);
            ipAddressTextInputEditText.setText(ipAddress);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        /*if (TextUtils.isEmpty(s)) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }*/
    }

    private void setUpSpinner() {
        int arrayResource = R.array.custom_board_types;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayResource, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        appCompatSpinner.setAdapter(adapter);
    }

}
