package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;

public class AskToPauseRes extends Response {

    public AskToPauseRes() {
        this.type = RESPONSE_ASK_TO_PAUSE;
    }

    public AskToPauseRes(@JsonProperty("status") String status, @JsonProperty("type") String type, @JsonProperty("message") String message) {
        this.message = message;
        this.type = type;
        this.status = status;
    }
}
