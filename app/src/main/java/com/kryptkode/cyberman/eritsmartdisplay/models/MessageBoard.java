package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.content.Context;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class MessageBoard  implements Serializable {
    public static final String TAG = MessageBoard.class.getSimpleName();
    public static final String MSG = "//M";
    private String messageString;
    private int numberOfMessages;
    private MessageBoardType messageBoardType;
    private TreeMap<String, String> messagesMap;
    private String name;
    private String ipAddress;
    private int id;
    private String NoOfMsg;
    private String bsi;
    private int format;

    public MessageBoard(String messageString, int numberOfMessages, MessageBoardType messageBoardType) {
        this.messageString = messageString;
        this.messageBoardType = messageBoardType;
        this.numberOfMessages = numberOfMessages;
    }

    public MessageBoard(String messageString, int numberOfMessages) {
        this.messageString = messageString;
        this.numberOfMessages = numberOfMessages;
    }

    public MessageBoard(MessageBoardType messageBoardType) {

        this.messageBoardType = messageBoardType;
    }

    public MessageBoard() {

    }

    public String getBSI() {
        return bsi;
    }

    public void setBSI(String bsi) {
        this.bsi = bsi;
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

    public TreeMap<String, String> getMessagesMap() {
        return messagesMap;
    }

    public void setMessagesMap(TreeMap<String, String> messagesMap) {
        this.messagesMap = messagesMap;
    }

    public void setFormat(int format) {
        this.format = format;
    }
    public int getFormat() {
        return format;
    }
    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }


    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public String createMessageSendFormat(){
        return DisplayBoardHelpers.createMessageSendFormat(this.messagesMap);
    }

   public String getMessageBoardCode(){
        return DisplayBoardHelpers.generateMesssageBoardCode(this.messageBoardType);
    }


    public static MessageBoardType getMessageBoardTypeFromInt(int code) throws Exception {
        switch (code) {
            case (1):
                return MessageBoardType.MESSAGE_BOARD_TYPE_ONE;
            case (2):
                return MessageBoardType.MESSAGE_BOARD_TYPE_TWO;
            case (3):
                return MessageBoardType.MESSAGE_BOARD_TYPE_THREE;
            case (4):
                return MessageBoardType.MESSAGE_BOARD_TYPE_FOUR;
            case (5):
                return MessageBoardType.MESSAGE_BOARD_TYPE_FIVE;
            case (6):
                return MessageBoardType.MESSAGE_BOARD_TYPE_SIX;
            case (7):
                return MessageBoardType.MESSAGE_BOARD_TYPE_SEVEN;
            case (8):
                return MessageBoardType.MESSAGE_BOARD_TYPE_EIGHT;
            case (9):
                return MessageBoardType.MESSAGE_BOARD_TYPE_NINE;
            case (10):
                return MessageBoardType.MESSAGE_BOARD_TYPE_TEN;
            case (11):
                return MessageBoardType.MESSAGE_BOARD_TYPE_ELEVEN;
            case (12):
                return MessageBoardType.MESSAGE_BOARD_TYPE_TWELVE;
            case (13):
                return MessageBoardType.MESSAGE_BOARD_TYPE_THIRTEEN;
            case (14):
                return MessageBoardType.MESSAGE_BOARD_TYPE_FOURTEEN;
            case (15):
                return MessageBoardType.MESSAGE_BOARD_TYPE_FIFTEEN;
            case (16):
                return MessageBoardType.MESSAGE_BOARD_TYPE_SIXTEEN;
            case (17):
                return MessageBoardType.MESSAGE_BOARD_TYPE_SEVENTEEN;
            case (18):
                return MessageBoardType.MESSAGE_BOARD_TYPE_EIGHTEEN;
            case (19):
                return MessageBoardType.MESSAGE_BOARD_TYPE_NINETEEN;
            case (20):
                return MessageBoardType.MESSAGE_BOARD_TYPE_TWENTY;
            case (-200):
                return MessageBoardType.MESSAGE_BOARD_TYPE_NONE;

            default:
                throw new Exception("Integer " + code + " is not associated with any Message board type");
        }
    }


    public MessageBoardType getMessageBoardType() {
        return messageBoardType;
    }

    public void setMessageBoardType(MessageBoardType messageBoardType) {
        this.messageBoardType = messageBoardType;
    }

    public enum MessageBoardType {
        MESSAGE_BOARD_TYPE_ONE(1),
        MESSAGE_BOARD_TYPE_TWO(2),
        MESSAGE_BOARD_TYPE_THREE(3),
        MESSAGE_BOARD_TYPE_FOUR(4),
        MESSAGE_BOARD_TYPE_FIVE(5),
        MESSAGE_BOARD_TYPE_SIX(6),
        MESSAGE_BOARD_TYPE_SEVEN(7),
        MESSAGE_BOARD_TYPE_EIGHT(8),
        MESSAGE_BOARD_TYPE_NINE(9),
        MESSAGE_BOARD_TYPE_TEN(10),
        MESSAGE_BOARD_TYPE_ELEVEN(11),
        MESSAGE_BOARD_TYPE_TWELVE(12),
        MESSAGE_BOARD_TYPE_THIRTEEN(13),
        MESSAGE_BOARD_TYPE_FOURTEEN(14),
        MESSAGE_BOARD_TYPE_FIFTEEN(15),
        MESSAGE_BOARD_TYPE_SIXTEEN(16),
        MESSAGE_BOARD_TYPE_SEVENTEEN(17),
        MESSAGE_BOARD_TYPE_EIGHTEEN(18),
        MESSAGE_BOARD_TYPE_NINETEEN(19),
        MESSAGE_BOARD_TYPE_TWENTY(20),
        MESSAGE_BOARD_TYPE_NONE(-200);


        private int numberOfCascades;

        MessageBoardType(int numberOfCascades) {
            this.numberOfCascades = numberOfCascades;
        }

        public int getNumberOfCascades() {
            return numberOfCascades;
        }
    }
}
