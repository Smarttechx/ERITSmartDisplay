package com.kryptkode.cyberman.eritsmartdisplay.models;
import java.io.Serializable;
import java.util.TreeMap;
/**
 * Created by Cyberman on 8/9/2017.
 */

public class PriceBoard implements Serializable {
    public static final String TAG = PriceBoard.class.getSimpleName();
    public static final String PMS = "//P";
    public static final String DPK = "//D";
    public static final String AGO = "//A";
    public static final String MSG = "//M";
    private String messageString;
    private String name;
    private String bsi;
    private String ipAddress;
    private int id;
    private String NoOfMsg;
    private String priceString;
    private PriceBoardType priceBoardType;
    private TreeMap<String, String> priceValuesMap;
    private TreeMap<String, String> msgsMap;
    private int format;

    public PriceBoard(String name,String ipAddress,String NoOfMsg, PriceBoardType priceBoardType) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.NoOfMsg=NoOfMsg;
        this.priceBoardType = priceBoardType;
    }
    public PriceBoard(String name,String ipAddress,String NoOfMsg, PriceBoardType priceBoardType,TreeMap<String, String> priceValuesMap) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.NoOfMsg=NoOfMsg;
        this.priceBoardType = priceBoardType;
        this.priceValuesMap=priceValuesMap;
    }
    public PriceBoard(String name,String ipAddress,String NoOfMsg, PriceBoardType priceBoardType,TreeMap<String, String> priceValuesMap,TreeMap<String, String> msgsMap) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.NoOfMsg=NoOfMsg;
        this.priceBoardType = priceBoardType;
        this.priceValuesMap=priceValuesMap;
        this.msgsMap=msgsMap;
    }

    public PriceBoard(String name,String ipAddress, PriceBoardType priceBoardType) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.priceBoardType = priceBoardType;
    }

    public PriceBoard(PriceBoardType priceBoardType) {
        this.priceBoardType = priceBoardType;
    }


    public PriceBoard() {

    }


    public String getNoOfMsg() {
        return NoOfMsg;
    }

    public void setNoOfMsg(String NoOfMsg) {
        this.NoOfMsg = NoOfMsg;
    }

    public String getBSI() {
        return bsi;
    }

    public void setBSI(String bsi) {
        this.bsi = bsi;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    public void setFormat(int format) {
        this.format = format;
    }
    public int getFormat() {
        return format;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public TreeMap<String, String> getPriceValuesMap() {
        return priceValuesMap;
    }


    public void setPriceValuesMap(TreeMap<String, String> priceValuesMap) {
        this.priceValuesMap = priceValuesMap;
    }
    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }


    public String createMessageSendFormat(){
        return DisplayBoardHelpers.createMessageSendFormat(this.msgsMap);
    }

    public TreeMap<String, String> getMsgsMap() {
        return msgsMap;
    }


    public void setMsgsMap(TreeMap<String, String> msgsMap) {
        this.msgsMap = msgsMap;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static PriceBoardType getPriceBoardTypeFromInt(int code) throws Exception {
        switch (code) {
            case (1):
                return PriceBoardType.PRICE_BOARD_TYPE_ONE;
            case (2):
                return PriceBoardType.PRICE_BOARD_TYPE_TWO;
            case (3):
                return PriceBoardType.PRICE_BOARD_TYPE_THREE;
            case (4):
                return PriceBoardType.PRICE_BOARD_TYPE_FOUR;
            case (5):
                return PriceBoardType.PRICE_BOARD_TYPE_FIVE;
            case (6):
                return PriceBoardType.PRICE_BOARD_TYPE_SIX;
            case (7):
                return PriceBoardType.PRICE_BOARD_TYPE_SEVEN;
            case (8):
                return PriceBoardType.PRICE_BOARD_TYPE_EIGHT;
            case (-1):
                return PriceBoardType.PRICE_BOARD_TYPE_NONE;

            default:
                throw new Exception("Integer " + code + " is not associated with any Price board type");
        }
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public PriceBoardType getPriceBoardType() {
        return priceBoardType;
    }

    public void setPriceBoardType(PriceBoardType priceBoardType) {
        this.priceBoardType = priceBoardType;
    }

    public String createPriceSendFormat(){
        return DisplayBoardHelpers.createMessageSendFormat(this.priceValuesMap);
    }

    public String getPriceBoardCode(){
        return DisplayBoardHelpers.generatePriceBoardCode(this.priceBoardType);
    }

    public String createBoardType(){
        return String.valueOf(this.getPriceBoardType().getNumberOfCascades());
    }


    public enum PriceBoardType {
        PRICE_BOARD_TYPE_ONE(1),
        PRICE_BOARD_TYPE_TWO(2),
        PRICE_BOARD_TYPE_THREE(3),
        PRICE_BOARD_TYPE_FOUR(4),
        PRICE_BOARD_TYPE_FIVE(5),
        PRICE_BOARD_TYPE_SIX(6),
        PRICE_BOARD_TYPE_SEVEN(7),
        PRICE_BOARD_TYPE_EIGHT(8),
        PRICE_BOARD_TYPE_NONE(-1);

        private int numberOfCascades;

        PriceBoardType(int numberOfCascades) {
            this.numberOfCascades = numberOfCascades;
        }

        public int getNumberOfCascades() {
            return numberOfCascades;
        }
    }

}
