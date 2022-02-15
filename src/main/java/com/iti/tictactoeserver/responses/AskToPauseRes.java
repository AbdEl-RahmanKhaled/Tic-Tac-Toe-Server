package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class AskToPauseRes extends Response {
    Player player1;

    public AskToPauseRes() {

    }

    public AskToPauseRes(@JsonProperty("type") String type, @JsonProperty("message") String message) {
        this.message = message;
        this.type = type;
    }

    public Player getPlayer() {
        return player1;
    }

    public void setPlayer(Player player1) {
        this.player1 = player1;
    }
}
