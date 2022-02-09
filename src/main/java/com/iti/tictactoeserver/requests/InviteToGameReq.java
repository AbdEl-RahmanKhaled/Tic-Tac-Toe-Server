package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class InviteToGameReq extends Requests {
    private Player player;

    public InviteToGameReq() {
        action = ACTION_INVITE_TO_GAME;
    }

    public InviteToGameReq(Player player) {
        this.player = player;
        action = ACTION_INVITE_TO_GAME;
    }

    public InviteToGameReq(@JsonProperty("player") Player player, @JsonProperty("action") String action) {
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
