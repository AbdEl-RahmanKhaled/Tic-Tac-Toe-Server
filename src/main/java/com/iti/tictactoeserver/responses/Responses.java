package com.iti.tictactoeserver.responses;

public class Responses {
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_OK = "ok";
//    public static final String STATUS_ACCEPTED = "accepted";
//    public static final String STATUS_REJECTED = "rejected";

    public static final String RESPONSE_INVITE_TO_GAME = "inviteToGameRes";

    protected String message, status, type;

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
