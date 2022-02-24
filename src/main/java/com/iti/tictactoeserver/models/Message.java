package com.iti.tictactoeserver.models;

public class Message {
    private String message;
    private String from;

    public Message(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
