package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AskToPauseReq extends Request{
    public AskToPauseReq() {
        super(ACTION_ASK_TO_PAUSE);
    }

    public AskToPauseReq(@JsonProperty String action) {
        super(action);
    }
}
