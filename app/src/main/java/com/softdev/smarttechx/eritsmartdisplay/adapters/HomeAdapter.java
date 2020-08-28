package com.softdev.smarttechx.eritsmartdisplay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdev.smarttechx.eritsmartdisplay.R;
import com.softdev.smarttechx.eritsmartdisplay.models.CustomBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.DigitalClockBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.MessageBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.PriceBoard;
import com.softdev.smarttechx.eritsmartdisplay.models.SmartDisplay;

import java.util.ArrayList;

import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.CUSTOM;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.DIGITAL;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.MESSAGE;
import static com.softdev.smarttechx.eritsmartdisplay.EritSmartDisplayActivity.PRICE;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    public static final String TAG = HomeAdapter.class.getSimpleName();
    public static final int IMG_TAG = 1;
    private HomeAdapterListener homeAdapterListener;
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<SmartDisplay> smartBoardList;
    private SmartDisplay smartDisplay;
    private PriceBoard priceBoard;
    private CustomBoard customBoard;
    private MessageBoard msgBoard;
    private DigitalClockBoard digitalclockBoard;
    public int devicePosition;
    public HomeAdapter(Context context,ArrayList<SmartDisplay>  smartBoardList) {
        this.context = context;
        this.smartBoardList =  smartBoardList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list_item, parent, false);
        return new HomeViewHolder(itemView);

    }


    public interface HomeAdapterListener {
        void onDisplayClicked(SmartDisplay displayBoard);
        void onDisplayOverflowClicked(SmartDisplay displayBoard, int positon, View view);
    }

    public void setHomeAdapterListener(HomeAdapterListener homeAdapterListener) {
        this.homeAdapterListener = homeAdapterListener;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView displayAvatar;
        TextView displayName;
        ImageButton displayOverflowButton;
        ImageView boardTypeIcon;

        public HomeViewHolder(View itemView) {
            super(itemView);
            displayAvatar = (ImageView) itemView.findViewById(R.id.display_imageView);
            displayName = (TextView) itemView.findViewById(R.id.display_name_textView);
            displayOverflowButton = (ImageButton) itemView.findViewById(R.id.display_overflow);
            boardTypeIcon = (ImageView) itemView.findViewById(R.id.board_type_icon);
            itemView.setOnClickListener(this);
            displayOverflowButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == displayOverflowButton) {
                devicePosition=getLayoutPosition();
                homeAdapterListener.onDisplayOverflowClicked((SmartDisplay)v.getTag(),devicePosition,v);
            }
            else {

                homeAdapterListener.onDisplayClicked((SmartDisplay)v.getTag());
            }

        }
    }

    @Override
    public void onBindViewHolder( final HomeViewHolder holder, final int position) {
        smartDisplay =smartBoardList.get(position);
        if(smartDisplay.getBoardType().equals(PRICE)){
            priceBoard=new PriceBoard();
            priceBoard=smartDisplay.getPriceBoard();
            priceBoard.setId(holder.getLayoutPosition());
            holder.displayName.setText(priceBoard.getName());
            holder.boardTypeIcon.setImageResource(R.drawable.ic_local_gas_station_black_24dp);
            holder.itemView.setTag( smartDisplay );
            holder.displayOverflowButton.setTag( smartDisplay );
        }
        else if(smartDisplay.getBoardType().equals(MESSAGE)){
            msgBoard=new MessageBoard();
            msgBoard=smartDisplay.getMsgBoard();
            msgBoard.setId(holder.getLayoutPosition());
            holder.displayName.setText(msgBoard.getName());
            holder.boardTypeIcon.setImageResource(R.drawable.ic_restaurant);
            holder.itemView.setTag( smartDisplay );
            holder.displayOverflowButton.setTag( smartDisplay );

        }
       else if(smartDisplay.getBoardType().equals(CUSTOM)){
            customBoard=new CustomBoard();
            customBoard=smartDisplay.getCustomBoard();
            customBoard.setId(holder.getLayoutPosition());
            holder.displayName.setText(customBoard.getName());
            holder.boardTypeIcon.setImageResource(R.drawable.ic_custom_24dp);
            holder.itemView.setTag( smartDisplay );
            holder.displayOverflowButton.setTag( smartDisplay );
        }

        else if(smartDisplay.getBoardType().equals(DIGITAL)){
            digitalclockBoard=new DigitalClockBoard();
            digitalclockBoard=smartDisplay.getDigitalBoardBoard();
            digitalclockBoard.setId(holder.getLayoutPosition());
            holder.displayName.setText(digitalclockBoard.getName());
            holder.boardTypeIcon.setImageResource(R.drawable.ic_access_time_black_24dp);
            holder.itemView.setTag( smartDisplay );
            holder.displayOverflowButton.setTag( smartDisplay );
        }
    }


    @Override
    public int getItemCount() {
        return  smartBoardList.size();
    }

}

