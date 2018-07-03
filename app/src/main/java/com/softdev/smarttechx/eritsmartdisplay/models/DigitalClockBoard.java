package com.softdev.smarttechx.eritsmartdisplay.models;


import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by SMARTTECHX on 10/20/2017.
 */

public class DigitalClockBoard implements Serializable {

    private DigitalClockType digitalClockType;
    private TreeMap<String, String> digitalclockMap;
    private String name;
    private String ipAddress;
    private int id;
    private int format;
    private String bsi;
    public DigitalClockBoard() {

    }

    public DigitalClockBoard(DigitalClockType digitalClockType) {

        this.digitalClockType = digitalClockType;

    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public TreeMap<String, String> getDigitalClockMap() {
        return digitalclockMap;
    }

    public void setdigitalclockMap(TreeMap<String, String> digitalclockMap) {
        this.digitalclockMap = digitalclockMap;
    }

    public String createMessageSendFormat(){
        return DisplayBoardHelpers.createMessageSendFormat(this.digitalclockMap);
    }

    public void setFormat(int format) {
        this.format = format;
    }
    public int getFormat() {
        return format;
    }

    public String getBSI() {
        return bsi;
    }

    public void setBSI(String bsi) {
        this.bsi = bsi;
    }

    public static DigitalClockType getDigitalClockTypeFromInt(int code) throws Exception {
        switch (code) {
            case (1):
                return DigitalClockType.digitalclock_BOARD_TYPE_NONE;

            case (2):
                return DigitalClockType.digitalclock_BOARD_TYPE_TWO;
            case (3):
                return DigitalClockType.digitalclock_BOARD_TYPE_THREE;
            case (-200):
                return DigitalClockType.digitalclock_BOARD_TYPE_NONE;

            default:
                throw new Exception("Integer " + code + " is not associated with any digitalclock board type");
        }
    }


    public DigitalClockType getDigitalClockType() {
        return digitalClockType;
    }

    public void setDigitalClockType(DigitalClockType digitalClockType) {
        this.digitalClockType = digitalClockType;
    }

    public enum DigitalClockType {
       digitalclock_BOARD_TYPE_ONE(1),
       digitalclock_BOARD_TYPE_TWO(2),
       digitalclock_BOARD_TYPE_THREE(3),
       digitalclock_BOARD_TYPE_NONE(-200);
        private int numberOfCascades;
        DigitalClockType(int numberOfCascades) {
            this.numberOfCascades = numberOfCascades;
        }

        public int getNumberOfCascades() {
            return numberOfCascades;
        }
    }

    public String createBoardType(){
        return this.getDigitalClockType().getNumberOfCascades() + "|" ;
    }

    public String getDigitalClockCode(){
        return DisplayBoardHelpers.generateDigitalClockBoardCode(this.digitalClockType);
    }
}