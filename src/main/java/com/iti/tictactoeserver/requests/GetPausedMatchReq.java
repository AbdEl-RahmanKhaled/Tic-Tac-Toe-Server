package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPausedMatchReq extends Request{
    private int m_id;

    public GetPausedMatchReq(){super(ACTION_GET_PAUSED_MATCH);}

    public GetPausedMatchReq(int m_id) {
        super(ACTION_GET_PAUSED_MATCH);
        this.m_id = m_id;

    }

    public GetPausedMatchReq(@JsonProperty("action") String action,
                             @JsonProperty("m_id") int m_id){
        super(ACTION_GET_PAUSED_MATCH);
        this.m_id = m_id;

    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }


}
