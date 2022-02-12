package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.User;

import static com.iti.tictactoeserver.requests.Request.ACTION_SIGN_UP;

public class SignUpReq extends Request {
    private User user;

    public SignUpReq() {
        super(ACTION_SIGN_UP);
    }

    public SignUpReq(User user) {
        super(ACTION_SIGN_UP);
        this.user = user;
    }

    public SignUpReq(@JsonProperty("user") User user, @JsonProperty("action") String action) {
        super(ACTION_SIGN_UP);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
