package com.iti.serverdashbaord.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    protected String type;

    public static final String NOTIFICATION_UPDATE_STATUS = "updateStatusNotification";
    public static final String NOTIFICATION_SERVER_STARTED = "serverStartedNotification";
    public static final String NOTIFICATION_SERVER_STOPPED = "serverStoppedNotification";


    public Notification() {
    }

    public Notification(@JsonProperty("type") String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
