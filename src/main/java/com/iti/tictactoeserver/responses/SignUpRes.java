package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRes extends Response {
    public SignUpRes() {
        this.type = RESPONSE_SIGN_UP;
    }

    public SignUpRes(@JsonProperty("message") String message,
                     @JsonProperty("status") String status,
                     @JsonProperty("type") String type) {
        super(message, status, type);
    }
}
