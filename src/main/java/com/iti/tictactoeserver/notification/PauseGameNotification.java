package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PauseGameNotification extends Notification {
    public PauseGameNotification() {
        this.type = NOTIFICATION_PAUSE_GAME;
    }

    public PauseGameNotification(@JsonProperty("type") String type) {
        super(type);
    }
}
