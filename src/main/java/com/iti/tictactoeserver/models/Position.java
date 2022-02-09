package com.iti.tictactoeserver.models;

public class Position {
    private int m_id, player_id;
    private String position;
    public Position() {
    }

    public Position(int m_id, int player_id, String position) {
        this.m_id = m_id;
        this.player_id = player_id;
        this.position = position;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}