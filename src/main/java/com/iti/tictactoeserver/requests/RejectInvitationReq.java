package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class RejectInvitationReq extends Request {
    Player player;

    public RejectInvitationReq() {
        super(ACTION_REJECT_INVITATION);
    }

    public RejectInvitationReq(Player player) {
        super(ACTION_REJECT_INVITATION);
        this.player = player;
    }

    public RejectInvitationReq(@JsonProperty("action") String action, @JsonProperty("player") Player player) {
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
