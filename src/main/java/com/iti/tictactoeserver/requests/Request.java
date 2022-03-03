package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
    protected String action;

    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_ASK_TO_PAUSE = "askToPause";
    public static final String ACTION_INVITE_TO_GAME = "inviteToGame";
    public static final String ACTION_ACCEPT_INVITATION = "acceptInvitation";
    public static final String ACTION_REJECT_INVITATION = "rejectInvitation";
    public static final String ACTION_UPDATE_BOARD = "updateBoard";
    public static final String ACTION_UPDATE_IN_GAME_STATUS = "updateInGameStatus";
    public static final String ACTION_SIGN_UP = "signup";
    public static final String ACTION_SAVE_MATCH = "saveMatch";
    public static final String ACTION_REJECT_TO_PAUSE = "rejectToPause";
    public static final String ACTION_ACCEPT_TO_PAUSE = "acceptToPause";
    public static final String ACTION_SEND_MESSAGE = "sendMessage";
    public static final String ACTION_ASK_TO_RESUME = "askToResume";
    public static final String ACTION_REJECT_TO_RESUME = "rejectToResume";
    public static final String ACTION_ACCEPT_TO_RESUME = "acceptToResume";
    public static final String ACTION_GET_MATCH_HISTORY = "getMatchHistory";
    public static final String ACTION_BACK_FROM_OFFLINE = "backFromOffline";
    public static final String ACTION_GET_PAUSED_MATCH = "getPausedMatch";

    // Server Dashboard requests
    public static final String ACTION_START_SERVER = "startServer";
    public static final String ACTION_STOP_SERVER = "stopServer";



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
