package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class RejectToResumeReq extends Request {
    private Player player;

    public RejectToResumeReq() {
        super(ACTION_REJECT_TO_RESUME);
    }

    public RejectToResumeReq(Player player) {
        super(ACTION_REJECT_TO_RESUME);
        this.player = player;
    }

    public RejectToResumeReq(@JsonProperty("action") String action, @JsonProperty("player") Player player) {
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
