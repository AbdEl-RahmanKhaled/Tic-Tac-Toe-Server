package com.iti.serverdashbaord.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.serverdashbaord.models.PlayerFullInfo;

public class UpdateStatusNotification extends Notification {
    PlayerFullInfo playerFullInfo;

    public UpdateStatusNotification() {
        type = NOTIFICATION_UPDATE_STATUS;
    }

    public UpdateStatusNotification(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
        type = NOTIFICATION_UPDATE_STATUS;
    }

    public UpdateStatusNotification(@JsonProperty("playerFullInfo") PlayerFullInfo playerFullInfo,
                                    @JsonProperty("type") String type) {
        this.playerFullInfo = playerFullInfo;
        this.type = type;
    }

    public PlayerFullInfo getPlayerFullInfo() {
        return playerFullInfo;
    }

    public void setPlayerFullInfo(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
    }
}
