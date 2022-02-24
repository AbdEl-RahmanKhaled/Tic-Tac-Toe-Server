package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishGameNotification extends Notification {
    private int winner;

    public FinishGameNotification() {
        this.type = NOTIFICATION_FINISH_GAME;
    }

    public FinishGameNotification(int winner) {
        this.winner = winner;
    }

    public FinishGameNotification(@JsonProperty("type") String type, @JsonProperty("winner") int winner) {
        super(type);
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
