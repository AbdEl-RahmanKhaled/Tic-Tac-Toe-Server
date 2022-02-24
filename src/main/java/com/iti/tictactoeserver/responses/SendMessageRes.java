package com.iti.tictactoeserver.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Message;
import com.iti.tictactoeserver.requests.SendMessageReq;

public class SendMessageRes extends Response {
    Message message;
    public SendMessageRes(){this.type=RESPONSE_SEND_MESSAGE;}

    public SendMessageRes(@JsonProperty("type") String type , @JsonProperty("message") Message message){
        this.type = type;
        this.message = message;
    }
    public Message getMsg() {
        return message;
    }

    public void setMsg(Message message) {
        this.message = message;
    }
}
