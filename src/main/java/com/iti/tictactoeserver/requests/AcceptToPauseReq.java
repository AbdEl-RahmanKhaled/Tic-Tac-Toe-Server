package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AcceptToPauseReq extends Request{
    public AcceptToPauseReq() {
        super(ACTION_ACCEPT_TO_PAUSE);
    }

    public AcceptToPauseReq(@JsonProperty String action) {
        super(action);
    }
}
