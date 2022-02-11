package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Position;

public class UpdateBoardNotification extends Notification {
    private Position position;

    public UpdateBoardNotification() {
        this.type = NOTIFICATION_UPDATE_BOARD;
    }

    public UpdateBoardNotification(Position position) {
        this.position = position;
        this.type = NOTIFICATION_UPDATE_BOARD;
    }

    public UpdateBoardNotification(@JsonProperty("type") String type, @JsonProperty("position") Position position) {
        this.position = position;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
