package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RejectToPauseReq extends Request{
    public RejectToPauseReq() {
        super(ACTION_REJECT_TO_PAUSE);
    }

    public RejectToPauseReq(@JsonProperty String action) {
        super(action);
    }
}
