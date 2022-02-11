package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class AcceptInvitationReq extends Request {
    private Player player;

    public AcceptInvitationReq() {
        super(ACTION_ACCEPT_INVITATION);
    }

    public AcceptInvitationReq(Player player) {
        super(ACTION_ACCEPT_INVITATION);
        this.player = player;
    }

    public AcceptInvitationReq(@JsonProperty("action") String action,
                               @JsonProperty("player") Player player) {
        super(action);
        this.player = player;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
