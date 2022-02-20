package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Match;

import java.util.List;

public class GetMatchHistoryRes extends Response{
    private List<Match> matches;

    public GetMatchHistoryRes() {
        this.type = RESPONSE_GET_MATCH_HISTORY;
    }

    public GetMatchHistoryRes(String status,  List<Match> matches) {
        this.matches = matches;
        this.type = RESPONSE_GET_MATCH_HISTORY;
        this.status = status;
    }

    public GetMatchHistoryRes(@JsonProperty("status") String status,
                              @JsonProperty("type") String type,
                              @JsonProperty("message") String message,
                              @JsonProperty("matches") List<Match> matches) {
        super(message, status, type);
        this.matches = matches;
    }


    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
