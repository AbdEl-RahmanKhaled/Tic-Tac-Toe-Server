package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class AcceptInvitationReq extends Requests {
    private Player player;

    public AcceptInvitationReq() {
        this.action = ACTION_ACCEPT_INVITATION;
    }

    public AcceptInvitationReq(Player player) {
        this.player = player;
        this.action = ACTION_ACCEPT_INVITATION;
    }

    public AcceptInvitationReq(@JsonProperty("action") String action,
                               @JsonProperty("player") Player player) {
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
