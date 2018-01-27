package com.kryptkode.cyberman.eritsmartdisplay.models;


import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by SMARTTECHX on 10/20/2017.
 */

public class CustomBoard implements Serializable {

    private CustomBoardType customBoardType;
    private TreeMap<String, String> customMap;
    private String name;
    private String ipAddress;
    private int id;
    private String NoOfMsg;
    private String bsi;
    private int format;

    public CustomBoard() {

    }

    public CustomBoard( CustomBoardType customBoardType) {

        this.customBoardType = customBoardType;

    }

    public String getNoOfMsg() {
        return NoOfMsg;
    }

    public void setNoOfMsg(String NoOfMsg) {
        this.NoOfMsg = NoOfMsg;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public void setFormat(int format) {
        this.format = format;
    }
    public int getFormat() {
        return format;
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

    public TreeMap<String, String> getCustomMap() {
        return customMap;
    }

    public void setCustomMap(TreeMap<String, String> customMap) {
        this.customMap = customMap;
    }
    public String getBSI() {
        return bsi;
    }

    public void setBSI(String bsi) {
        this.bsi = bsi;
    }


    public String createMessageSendFormat(){
        return DisplayBoardHelpers.createMessageSendFormat(this.customMap);
    }

    public static CustomBoardType getCustomBoardTypeFromInt(int code) throws Exception {
        switch (code) {
            case (1):
                return CustomBoardType.CUSTOM_BOARD_TYPE_ONE;
            case (2):
                return CustomBoardType.CUSTOM_BOARD_TYPE_TWO;
            case (3):
                return CustomBoardType.CUSTOM_BOARD_TYPE_THREE;
            case (4):
                return CustomBoardType.CUSTOM_BOARD_TYPE_FOUR;
            case (5):
                return CustomBoardType.CUSTOM_BOARD_TYPE_FIVE;
            case (6):
                return CustomBoardType.CUSTOM_BOARD_TYPE_SIX;
            case (7):
                return CustomBoardType.CUSTOM_BOARD_TYPE_SEVEN;
            case (8):
                return CustomBoardType.CUSTOM_BOARD_TYPE_EIGHT;
            case (9):
                return CustomBoardType.CUSTOM_BOARD_TYPE_NINE;
            case (10):
                return CustomBoardType.CUSTOM_BOARD_TYPE_TEN;
            case (11):
                return CustomBoardType.CUSTOM_BOARD_TYPE_ELEVEN;
            case (12):
                return CustomBoardType.CUSTOM_BOARD_TYPE_TWELVE;
            case (13):
                return CustomBoardType.CUSTOM_BOARD_TYPE_THIRTEEN;
            case (14):
                return CustomBoardType.CUSTOM_BOARD_TYPE_FOURTEEN;
            case (15):
                return CustomBoardType.CUSTOM_BOARD_TYPE_FIFTEEN;
            case (16):
                return CustomBoardType.CUSTOM_BOARD_TYPE_SIXTEEN;
            case (17):
                return CustomBoardType.CUSTOM_BOARD_TYPE_SEVENTEEN;
            case (18):
                return CustomBoardType.CUSTOM_BOARD_TYPE_EIGHTEEN;
            case (19):
                return CustomBoardType.CUSTOM_BOARD_TYPE_NINETEEN;
            case (20):
                return CustomBoardType.CUSTOM_BOARD_TYPE_TWENTY;
            case (-200):
                return CustomBoardType.CUSTOM_BOARD_TYPE_NONE;

            default:
                throw new Exception("Integer " + code + " is not associated with any custom board type");
        }
    }


    public CustomBoardType getCustomBoardType() {
        return customBoardType;
    }

    public void setCustomBoardType(CustomBoardType customBoardType) {
        this.customBoardType = customBoardType;
    }

    public enum CustomBoardType {
       CUSTOM_BOARD_TYPE_ONE(1),
       CUSTOM_BOARD_TYPE_TWO(2),
       CUSTOM_BOARD_TYPE_THREE(3),
       CUSTOM_BOARD_TYPE_FOUR(4),
       CUSTOM_BOARD_TYPE_FIVE(5),
       CUSTOM_BOARD_TYPE_SIX(6),
       CUSTOM_BOARD_TYPE_SEVEN(7),
       CUSTOM_BOARD_TYPE_EIGHT(8),
       CUSTOM_BOARD_TYPE_NINE(9),
       CUSTOM_BOARD_TYPE_TEN(10),
       CUSTOM_BOARD_TYPE_ELEVEN(11),
       CUSTOM_BOARD_TYPE_TWELVE(12),
       CUSTOM_BOARD_TYPE_THIRTEEN(13),
       CUSTOM_BOARD_TYPE_FOURTEEN(14),
       CUSTOM_BOARD_TYPE_FIFTEEN(15),
       CUSTOM_BOARD_TYPE_SIXTEEN(16),
       CUSTOM_BOARD_TYPE_SEVENTEEN(17),
       CUSTOM_BOARD_TYPE_EIGHTEEN(18),
       CUSTOM_BOARD_TYPE_NINETEEN(19),
       CUSTOM_BOARD_TYPE_TWENTY(20),
       CUSTOM_BOARD_TYPE_NONE(-200);
        private int numberOfCascades;
        CustomBoardType(int numberOfCascades) {
            this.numberOfCascades = numberOfCascades;
        }

        public int getNumberOfCascades() {
            return numberOfCascades;
        }
    }

    public String createBoardType(){
        return this.getCustomBoardType().getNumberOfCascades() + "|" ;
    }

    public String getCustomBoardCode(){
        return DisplayBoardHelpers.generateCustomizeBoardCode(this.customBoardType);
    }
}