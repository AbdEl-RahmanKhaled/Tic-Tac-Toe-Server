package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.User;

public class SignUpRes extends Response {
    private User user;

    public SignUpRes(String status, User user) {
        //super(status,RESPONSE_SIGN_UP);
        this.user = user;
        this.status = status;
        this.type = RESPONSE_SIGN_UP;

    }

    public SignUpRes(String status, String message, User user) {
        //super(status,RESPONSE_SIGN_UP);
        this.user = user;
        this.status = status;
        this.message = message;
        this.type = RESPONSE_SIGN_UP;
    }

    public SignUpRes(@JsonProperty("status") String status,
                           @JsonProperty("type") String type,
                           @JsonProperty("message") String message,
                           @JsonProperty("user") User user) {
        //super(message,status,RESPONSE_SIGN_UP);
        this.status = status;
        this.type = type;
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
