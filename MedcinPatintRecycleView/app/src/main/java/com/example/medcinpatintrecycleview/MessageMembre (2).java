package com.example.medcinpatintrecycleview;

public class MessageMembre {
   private String message,time,date,type,senderUid,receiverUid;

    public MessageMembre(){}

    public MessageMembre(String message, String time, String date, String type, String senderUid, String receiverUid) {
        this.message = message;
        this.time = time;
        this.date = date;
        this.type = type;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }
}
