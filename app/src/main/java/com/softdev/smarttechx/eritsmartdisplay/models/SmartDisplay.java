package com.softdev.smarttechx.eritsmartdisplay.models;
import java.io.Serializable;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplay implements Serializable {
    public static final String TAG = SmartDisplay.class.getSimpleName();
    private String boardType;
    private PriceBoard priceBoard;
    private MessageBoard msgBoard;
    private CustomBoard customBoard;
    private DigitalClockBoard digitalBoard;

    public SmartDisplay( String boardType,PriceBoard priceBoard ) {

        this.boardType=boardType;
        this.priceBoard=priceBoard;

    }

    public SmartDisplay(String boardType, CustomBoard customBoard) {

        this.boardType = boardType;
        this.customBoard = customBoard;
    }

    public SmartDisplay(String boardType, DigitalClockBoard digitalBoard) {

        this.boardType = boardType;
        this.digitalBoard = digitalBoard;
    }

    public SmartDisplay(String boardType, MessageBoard msgBoard) {

        this.boardType = boardType;
        this.msgBoard = msgBoard;

    }

    public SmartDisplay(){
        //empty constructor
    }

    public PriceBoard getPriceBoard() {
        return priceBoard;
    }

    public void setPriceBoard(PriceBoard priceBoard) {
        this.priceBoard= priceBoard;
    }
    public MessageBoard getMsgBoard() {
        return msgBoard;
    }

    public void setMsgBoard(MessageBoard msgBoard) {
        this.msgBoard= msgBoard;
    }

    public CustomBoard getCustomBoard() {
        return customBoard;
    }

    public void setCustomBoard(CustomBoard customBoard) {
        this.customBoard = customBoard;
    }

    public DigitalClockBoard getDigitalBoard() {
        return digitalBoard;
    }

    public void setDigitalBoard(DigitalClockBoard digitalBoard) {
        this.digitalBoard = digitalBoard;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

}
