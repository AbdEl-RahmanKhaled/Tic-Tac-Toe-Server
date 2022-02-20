package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AskToPauseNotification extends Notification {
    public AskToPauseNotification() {
        this.type = NOTIFICATION_ASK_TO_PAUSE;
    }

    public AskToPauseNotification(@JsonProperty("type") String type) {
        super(type);
    }
}
