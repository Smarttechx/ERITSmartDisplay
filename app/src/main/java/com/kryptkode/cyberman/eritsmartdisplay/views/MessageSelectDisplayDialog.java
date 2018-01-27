package com.kryptkode.cyberman.eritsmartdisplay.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
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

import com.kryptkode.cyberman.eritsmartdisplay.R;
import com.kryptkode.cyberman.eritsmartdisplay.models.CustomBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;
import com.kryptkode.cyberman.eritsmartdisplay.utils.GsonUtil;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class MessageSelectDisplayDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    public static final String IS_IN_EDIT_MODE_KEY = "positive_button";
    public static final String PRICE_TYPE = "board_type";

    private SelectDisplayDialogListener listener;

    private AlertDialog dialog;
    private AppCompatSpinner appCompatSpinner;
    private LinearLayout msgShow;
    private TextInputEditText nameTextInputEditText;
    private TextInputEditText ipAddressTextInputEditText;
    private TextInputEditText textInputEditText;


    private boolean isEditing;
    private MessageBoard msgBoard;
    private boolean status;
    private String name, ipAddress, noOfMsg;
    private boolean hasUserSelected;
    private CheckBox memoFormat;
    int formatValue = 0;


    public interface SelectDisplayDialogListener {
        void onMessageDialogPositiveButtonClicked(DialogFragment dialog, MessageBoard msgBoard, boolean isEditing);
    }

    public static MessageSelectDisplayDialog getInstance(MessageBoard messageBoard, boolean isEditing) {
        MessageSelectDisplayDialog messageSelectDisplayDialog = new MessageSelectDisplayDialog();
        String putMsg = GsonUtil.getGsonparser().toJson(messageBoard);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        bundle.putString(PRICE_TYPE, putMsg);
        messageSelectDisplayDialog.setArguments(bundle);
        return messageSelectDisplayDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SelectDisplayDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString() + "must implement SelectDisplayDialogListener");
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
        String getMsg = bundle.getString(PRICE_TYPE);
        isEditing = bundle.getBoolean(IS_IN_EDIT_MODE_KEY);
        msgBoard = GsonUtil.getGsonparser().fromJson(getMsg, MessageBoard.class);
        name = msgBoard.getName();
        ipAddress = msgBoard.getIpAddress();
        noOfMsg = msgBoard.getNoOfMsg();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View spinnerView = layoutInflater.inflate(R.layout.dialog_message_spinner, null);
        appCompatSpinner = (AppCompatSpinner) spinnerView.findViewById(R.id.message_board_spinner);
        textInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_num_of_msgs);
        nameTextInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_boardName_et);
        ipAddressTextInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_ip_et);
        msgShow = (LinearLayout) spinnerView.findViewById(R.id.msgShow);
        textInputEditText.addTextChangedListener(this);
        appCompatSpinner.setOnItemSelectedListener(this);
        ipAddressTextInputEditText.addTextChangedListener(this);
        msgBoard = new MessageBoard();
        memoFormat = (CheckBox) spinnerView.findViewById(R.id.checkMemoFormat);
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
        builder.setView(spinnerView);
        builder.setTitle(isEditing ? getString(R.string.edit_boad) : getString(R.string.add_new_baord));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (hasUserSelected == false) {
                        Toast.makeText(getActivity().getApplicationContext(), "Board not saved, pls select a Board type", Toast.LENGTH_SHORT).show();
                    } else {
                        msgBoard.setName(nameTextInputEditText.getText().toString());
                        msgBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());
                        msgBoard.setNoOfMsg(textInputEditText.getText().toString());
                        msgBoard.setFormat(formatValue);
                        listener.onMessageDialogPositiveButtonClicked(MessageSelectDisplayDialog.this, msgBoard, isEditing);
                        Toast.makeText(getActivity().getApplicationContext(), "Board saved successfully", Toast.LENGTH_LONG).show();

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
                            msgBoard.setName(nameTextInputEditText.getText().toString());
                            msgBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());
                            msgBoard.setNoOfMsg(textInputEditText.getText().toString());
                            msgBoard.setFormat(formatValue);
                            listener.onMessageDialogPositiveButtonClicked(MessageSelectDisplayDialog.this, msgBoard, isEditing);
                            Toast.makeText(getActivity().getApplicationContext(), "Board saved successfully", Toast.LENGTH_LONG).show();

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
        return dialog;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            try {
                msgBoard.setMessageBoardType(MessageBoard.getMessageBoardTypeFromInt(position));
                setImageResource(msgBoard.getMessageBoardType().getNumberOfCascades());
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
        hasUserSelected = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }


    private void showToast() {
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type_show_msg, Toast.LENGTH_LONG).show();
    }

    //used to change the image resource or video for the proto_simutation of the display
    private void setImageResource(int boardType) {
        // TODO: Implememt the method to change the image resouce
    }

    private void setUpSpinner() {
        int arrayResource = R.array.message_board_types;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayResource, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        appCompatSpinner.setAdapter(adapter);
    }
}