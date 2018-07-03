package com.softdev.smarttechx.eritsmartdisplay;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;

import com.softdev.smarttechx.eritsmartdisplay.data.SmartDisplayDB;
import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.DigitalClockBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;
import com.softdev.smarttechx.eritsmartdisplay.utils.GsonUtil;
import com.softdev.smarttechx.eritsmartdisplay.views.CustomSelectDisplayDialog;
import com.softdev.smarttechx.eritsmartdisplay.views.DigitalClockSelectDisplayDialog;
import com.softdev.smarttechx.eritsmartdisplay.views.EmptyRecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.softdev.smarttechx.eritsmartdisplay.adapters.HomeAdapter;
import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;
import com.softdev.smarttechx.eritsmartdisplay.utils.ItemDivider;
import com.softdev.smarttechx.eritsmartdisplay.views.MessageSelectDisplayDialog;
import com.softdev.smarttechx.eritsmartdisplay.views.PriceSelectDisplayDialog;

import java.util.ArrayList;

import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.CUSTOM;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.DIGITAL;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.MESSAGE;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.PRICE;


public class HomeFragment extends Fragment implements HomeAdapter.HomeAdapterListener {


    public static final String TAG = HomeFragment.class.getSimpleName();
    private EmptyRecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private GridLayoutManager gridLayoutManager;
    private View emptyView;
    private PriceBoard priceBoard;
    private MessageBoard msgBoard;
    private CustomBoard customBoard;
    private DigitalClockBoard digitalClockBoard;
    private HomeFragmentListener homeFragmentListener;
    private SmartDisplayDB displayDB;
    ArrayList<SmartDisplay> smartBoardList;

    private ViewGroup homeViewGroup;

    public HomeFragment() {
        // Required empty public constructor
    }

    public interface HomeFragmentListener {
    }

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeFragmentListener = (HomeFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeFragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.home_recycler_view);
        emptyView = view.findViewById(R.id.empty_root);
        smartBoardList = new ArrayList<SmartDisplay>();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        } else {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }

        displayDB = new SmartDisplayDB(getActivity().getApplicationContext());
        smartBoardList = displayDB.loadDisplay();
        homeAdapter = new HomeAdapter(getActivity().getApplicationContext(), smartBoardList);
        homeAdapter.setHomeAdapterListener(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setEmptyView(emptyView);
        homeViewGroup = (ViewGroup) view.findViewById(R.id.home_root);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.erit_smart_display, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_filling_station_display) {
            PriceBoard priceBoard = new PriceBoard();
            priceBoard.setPriceBoardType(PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_ONE);
            PriceSelectDisplayDialog priceDialog = PriceSelectDisplayDialog.getInstance(priceBoard, false);
            priceDialog.setCancelable(false);
            priceDialog.show(getChildFragmentManager(), "edit");
            return true;
        } else if (id == R.id.action_add_resturant_display) {
            MessageBoard msgBoard = new MessageBoard();
            msgBoard.setMessageBoardType(MessageBoard.MessageBoardType.MESSAGE_BOARD_TYPE_NONE);
            MessageSelectDisplayDialog msgDialog = MessageSelectDisplayDialog.getInstance(msgBoard, false);
            msgDialog.setCancelable(false);
            msgDialog.show(getChildFragmentManager(), "edit");
            return true;
        } else if (id == R.id.action_add_custom_display) {
            CustomBoard customBoard = new CustomBoard();
            customBoard.setCustomBoardType(CustomBoard.CustomBoardType.CUSTOM_BOARD_TYPE_NONE);
            CustomSelectDisplayDialog editcustom = CustomSelectDisplayDialog.getInstance(customBoard, false);
            editcustom.setCancelable(false);
            editcustom.show(getChildFragmentManager(), "edit");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*Methods for the adapter listener*/
    @Override
    public void onDisplayClicked(SmartDisplay displayBoard) {
        Intent boardIntent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
        Bundle bundle = new Bundle();
        String putCustom = GsonUtil.getGsonparser().toJson(displayBoard);
        bundle.putString("BOARD", putCustom);
        boardIntent.putExtra("Board", bundle);
        boardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().getApplicationContext().startActivity(boardIntent);

    }

    /*Methods for the adapter listener*/
    @Override
    public void onDisplayOverflowClicked(final SmartDisplay displayBoard, final int device, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_edit:
                        if (displayBoard.getBoardType().equals(PRICE)) {
                            priceBoard = new PriceBoard();
                            priceBoard = displayBoard.getPriceBoard();
                            PriceSelectDisplayDialog editPriceBoard = PriceSelectDisplayDialog.getInstance(priceBoard, true);
                            editPriceBoard.setCancelable(false);
                            editPriceBoard.show(getChildFragmentManager(), "");
                        } else if (displayBoard.getBoardType().equals(MESSAGE)) {
                            msgBoard = new MessageBoard();
                            msgBoard = displayBoard.getMsgBoard();
                            MessageSelectDisplayDialog editMsgBoard = MessageSelectDisplayDialog.getInstance(msgBoard, true);
                            editMsgBoard.setCancelable(false);
                            editMsgBoard.show(getChildFragmentManager(), "");
                        } else if (displayBoard.getBoardType().equals(CUSTOM)) {
                            customBoard = new CustomBoard();
                            customBoard = displayBoard.getCustomBoard();
                            CustomSelectDisplayDialog editCustomBoard = CustomSelectDisplayDialog.getInstance(customBoard, true);
                            editCustomBoard.setCancelable(false);
                            editCustomBoard.show(getChildFragmentManager(), "");
                        }
                        else if (displayBoard.getBoardType().equals(DIGITAL)) {
                            digitalClockBoard = new DigitalClockBoard();
                            digitalClockBoard = displayBoard.getDigitalBoardBoard();
                            DigitalClockSelectDisplayDialog editdClockBoard = DigitalClockSelectDisplayDialog.getInstance(digitalClockBoard, true);
                            editdClockBoard.setCancelable(false);
                            editdClockBoard.show(getChildFragmentManager(), "");
                        }
                        return true;

                    case R.id.action_delete:
                        smartBoardList.remove(device);
                        displayDB.saveDisplays(smartBoardList);
                        homeAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;

                }
            }
        });

        popupMenu.inflate(R.menu.menu_home_item);
        popupMenu.show();
    }

}
