package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Message;

public class MessageNotification extends Notification {
    private Message message;

    public MessageNotification() {
        super(NOTIFICATION_MESSAGE);
    }

    public MessageNotification(Message message) {
        super(NOTIFICATION_MESSAGE);
        this.message = message;
    }

    public MessageNotification(@JsonProperty("type") String type, @JsonProperty("message") Message message) {
        super(type);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
