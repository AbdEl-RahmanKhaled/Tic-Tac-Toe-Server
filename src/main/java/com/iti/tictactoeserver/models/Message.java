package com.iti.tictactoeserver.models;

public class Message {
    private String message;
    private Player from;

    public Message(String message, Player from) {
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

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }
}
