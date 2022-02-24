package com.iti.tictactoeserver.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Message;

public class SendMessageReq extends Request {
    private Message message;

    public SendMessageReq() {
        super(ACTION_SEND_MESSAGE);
    }

    public SendMessageReq(Message message) {
        super(ACTION_SEND_MESSAGE);
        this.message = message;
    }

    public SendMessageReq(@JsonProperty("action") String action, @JsonProperty("message") Message message) {
        super(action);
        this.message = message;
    }

    public Message getMsg() {
        return message;
    }

    public void setMsg(Message message) {
        this.message = message;
    }
}
