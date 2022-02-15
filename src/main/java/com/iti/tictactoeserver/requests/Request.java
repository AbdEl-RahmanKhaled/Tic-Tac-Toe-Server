package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
    protected String action;

    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_INVITE_TO_GAME = "inviteToGame";
    public static final String ACTION_ACCEPT_INVITATION = "acceptInvitation";
    public static final String ACTION_REJECT_INVITATION = "rejectInvitation";
    public static final String ACTION_UPDATE_BOARD = "updateBoard";
    public static final String ACTION_UPDATE_IN_GAME_STATUS = "updateInGameStatus";
    public static final String ACTION_SIGN_UP = "signup";
    public static final String ACTION_ACCEPT_TO_PAUSE = "acceptToPause";
    public static final String ACTION_REJECT_TO_PAUSE = "rejectToPause";
    public static final String ACTION_SEND_MESSAGE = "sendMessage";


    public Request() {

    }

    public Request(@JsonProperty("action") String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
