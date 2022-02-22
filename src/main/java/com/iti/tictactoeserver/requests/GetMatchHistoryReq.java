package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetMatchHistoryReq extends Request{
    public GetMatchHistoryReq() {
        super(ACTION_GET_MATCH_HISTORY);
    }

    public GetMatchHistoryReq(@JsonProperty String action) {
        super(ACTION_GET_MATCH_HISTORY);
    }
}
