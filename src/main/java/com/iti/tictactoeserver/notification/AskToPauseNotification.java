package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.PlayerFullInfo;

public class AskToPauseNotification extends Notification{
    private PlayerFullInfo playerFullInfo;

    public AskToPauseNotification() {
        super(NOTIFICATION_ASK_TO_PAUSE);
    }

    public AskToPauseNotification(PlayerFullInfo playerFullInfo) {
        super(NOTIFICATION_ASK_TO_PAUSE);
        this.playerFullInfo = playerFullInfo;
    }

    public AskToPauseNotification(@JsonProperty("type") String type,
                                  @JsonProperty("playerFullInfo") PlayerFullInfo playerFullInfo) {
        super(NOTIFICATION_ASK_TO_PAUSE);
        this.playerFullInfo = playerFullInfo;
    }

    public PlayerFullInfo getPlayerFullInfo() {
        return playerFullInfo;
    }

    public void setPlayerFullInfo(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
    }
}
