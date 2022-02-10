package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class RejectInvitationReq extends Requests {
    Player player;

    public RejectInvitationReq() {
        this.action = ACTION_REJECT_INVITATION;
    }

    public RejectInvitationReq(Player player) {
        this.player = player;
        this.action = ACTION_REJECT_INVITATION;
    }

    public RejectInvitationReq(@JsonProperty("action") String action, @JsonProperty("player") Player player) {
        this.player = player;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
