package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.Position;

import java.util.List;

public class ResumeGameNotification extends Notification {
    private Match match;
    private List<Position> positions;

    public ResumeGameNotification() {
        this.type = NOTIFICATION_RESUME_GAME;
    }

    public ResumeGameNotification(Match match, List<Position> positions) {
        this.match = match;
        this.positions = positions;
        this.type = NOTIFICATION_RESUME_GAME;

    }

    public ResumeGameNotification(@JsonProperty("type") String type,
                                  @JsonProperty("match") Match match,
                                  @JsonProperty("positions") List<Position> positions) {
        super(type);
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
