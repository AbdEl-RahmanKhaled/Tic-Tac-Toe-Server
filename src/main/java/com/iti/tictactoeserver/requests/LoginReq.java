package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Credentials;

public class LoginReq extends Request {
    private Credentials credentials;

    public LoginReq() {
        super(ACTION_LOGIN);
    }

    public LoginReq(Credentials credentials) {
        super(ACTION_LOGIN);
        this.credentials = credentials;
    }

    public LoginReq(@JsonProperty("action") String action,
                    @JsonProperty("credentials") Credentials credentials) {
        super(ACTION_LOGIN);
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
