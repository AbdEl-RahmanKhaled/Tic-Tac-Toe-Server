package com.iti.tictactoeserver.requests;

public class Requests {
    protected String action;

    public static final String ACTION_INVITE_TO_GAME = "inviteToGame";

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
