package com.iti.tictactoeserver.notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class GameInvitationNotification extends Notification {
    private Player player;

    public GameInvitationNotification() {
        type = NOTIFICATION_GAME_INVITATION;
    }

    public GameInvitationNotification(Player player) {
        this.player = player;
        type = NOTIFICATION_GAME_INVITATION;
    }

    public GameInvitationNotification(@JsonProperty("player") Player player, @JsonProperty("type") String type) {
        this.type = type;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
