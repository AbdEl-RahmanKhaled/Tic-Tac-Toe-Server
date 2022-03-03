package com.iti.serverdashbaord.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
    protected String action;

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
