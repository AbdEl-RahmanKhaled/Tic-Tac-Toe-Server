package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class InviteToGameReq extends Request {
    private Player player;

    public InviteToGameReq() {
        super(ACTION_INVITE_TO_GAME);
    }

    public InviteToGameReq(Player player) {
        super(ACTION_INVITE_TO_GAME);
        this.player = player;
    }

    public InviteToGameReq(@JsonProperty("player") Player player, @JsonProperty("action") String action) {
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
