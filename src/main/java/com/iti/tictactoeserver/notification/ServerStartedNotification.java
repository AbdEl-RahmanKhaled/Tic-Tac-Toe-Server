package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.PlayerFullInfo;

import java.util.Map;

public class ServerStartedNotification extends Notification {
    private Map<Integer, PlayerFullInfo> playerFullInfoMap;

    public ServerStartedNotification() {
        this.type = NOTIFICATION_SERVER_STARTED;
    }

    public ServerStartedNotification(Map<Integer, PlayerFullInfo> playerFullInfoMap) {
        this.playerFullInfoMap = playerFullInfoMap;
        this.type = NOTIFICATION_SERVER_STARTED;
    }

    public ServerStartedNotification(@JsonProperty("type") String type, @JsonProperty("playerFullInfoMap") Map<Integer, PlayerFullInfo> playerFullInfoMap) {
        super(type);
        this.playerFullInfoMap = playerFullInfoMap;
    }

    public Map<Integer, PlayerFullInfo> getPlayerFullInfoMap() {
        return playerFullInfoMap;
    }

    public void setPlayerFullInfoMap(Map<Integer, PlayerFullInfo> playerFullInfoMap) {
        this.playerFullInfoMap = playerFullInfoMap;
    }
}