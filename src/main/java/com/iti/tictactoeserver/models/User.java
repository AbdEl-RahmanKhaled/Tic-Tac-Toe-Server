package com.iti.tictactoeserver.models;


public class User extends Credentials {
    private int u_id;
    private String name;
    private int points;

    public void setId(int Id) {
        u_id = Id;
    }

    public void setName(String Name) {
        name = Name;
    }

    public void setPoints(int Points) {
        points = Points;
    }

    public int getId() {
        return u_id;
    }

    public String getName() {
        return name;
    }


    public int getPoints() {
        return points;
    }
}