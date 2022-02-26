package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.Position;

import java.util.List;

public class GetPausedMatchRes extends Response{
    private List<Position> positions;
    private Match match;

    public GetPausedMatchRes(){super(RESPONSE_GET_PAUSED_MATCH);
        this.match = match;
    }

    public GetPausedMatchRes(List<Position> positions, Match match) {
        super(RESPONSE_GET_PAUSED_MATCH);
        this.positions = positions;
        this.match=match;
    }

    public GetPausedMatchRes(@JsonProperty("type") String type,
                             @JsonProperty("positions") List<Position> positions,
                             @JsonProperty("match") Match match){
        super(RESPONSE_GET_PAUSED_MATCH);
        this.positions=positions;
        this.match=match;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
