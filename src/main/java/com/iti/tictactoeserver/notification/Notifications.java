package com.iti.tictactoeserver.notification;

public class Notifications {
    protected String type;

    public static final String NOTIFICATION_GAME_INVITATION = "gameInvitationNotification";
    public static final String NOTIFICATION_UPDATE_STATUS = "updateStatusNotification";


    public Notifications() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
