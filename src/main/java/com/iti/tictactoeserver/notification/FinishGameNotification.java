package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishGameNotification extends Notification {
    public FinishGameNotification() {
        this.type = NOTIFICATION_FINISH_GAME;
    }

    public FinishGameNotification(@JsonProperty("type") String type) {
        super(type);
    }
}
