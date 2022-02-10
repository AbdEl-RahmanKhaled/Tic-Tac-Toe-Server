package com.iti.tictactoeserver.requests;

public class Requests {
    protected String action;

    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_INVITE_TO_GAME = "inviteToGame";
    public static final String ACTION_ACCEPT_INVITATION = "acceptInvitation";
    public static final String ACTION_REJECT_INVITATION = "rejectInvitation";


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
