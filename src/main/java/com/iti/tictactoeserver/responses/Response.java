package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_OK = "ok";
    public static final String RESPONSE_INVITE_TO_GAME = "inviteToGameRes";
    public static final String RESPONSE_LOGIN = "loginRes";
    public static final String RESPONSE_SIGN_UP = "signupRes";
    public static final String RESPONSE_ASK_TO_PAUSE = "askToPauseRes";
    public static final String RESPONSE_ASK_TO_RESUME = "askToResumeRes";
    public static final String RESPONSE_GET_MATCH_HISTORY = "getMatchHistoryRes";
    public static final String RESPONSE_GET_PAUSED_MATCH = "getPausedMatch";

    protected String message, status, type;

    public Response() {
    }

    public Response(String type) {
        this.type = type;
    }

    public Response(String status, String type) {
        this.status = status;
        this.type = type;
    }

    public Response(@JsonProperty("message") String message,
                    @JsonProperty("status") String status,
                    @JsonProperty("type") String type) {
        this.message = message;
        this.status = status;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
