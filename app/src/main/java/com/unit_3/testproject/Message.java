package com.unit_3.testproject;

public class Message {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";
    String messgae;
    String sentBy;

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public Message(String messgae, String sentBy) {
        this.messgae = messgae;
        this.sentBy = sentBy;
    }
}
