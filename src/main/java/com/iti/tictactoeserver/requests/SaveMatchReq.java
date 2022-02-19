package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.Position;

import java.util.List;

public class SaveMatchReq extends Request {
    private Match match;
    private List<Position> positions;

    public SaveMatchReq() {
        this.action = ACTION_SAVE_MATCH;
    }

    public SaveMatchReq(Match match, List<Position> positions) {
        this.action = ACTION_SAVE_MATCH;
        this.match = match;
        this.positions = positions;
    }

    public SaveMatchReq(@JsonProperty("action") String action,
                        @JsonProperty("match") Match match,
                        @JsonProperty("positions") List<Position> positions) {
        super(action);
        this.match = match;
        this.positions = positions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
