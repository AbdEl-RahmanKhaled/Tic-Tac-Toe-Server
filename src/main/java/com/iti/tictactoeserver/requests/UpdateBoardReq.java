package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Position;

public class UpdateBoardReq extends Request {
    private Position position;

    public UpdateBoardReq() {
        super(ACTION_UPDATE_BOARD);
    }

    public UpdateBoardReq(Position position) {
        super(ACTION_UPDATE_BOARD);
        this.position = position;
    }

    public UpdateBoardReq(@JsonProperty("action") String action, @JsonProperty("position") Position position) {
        super(action);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
