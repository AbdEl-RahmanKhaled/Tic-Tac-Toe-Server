package com.iti.tictactoeserver.models;

public class User {
    private int u_id;
    private String name;
    private String username;
    private String password;
    private int points;
    public void setId(int Id){
        u_id = Id;
    }
    public void setName(String Name){
        name = Name;
    }
    public void setUserName(String userName){
        username = userName;
    }
    public void setPassword(String Password){
        password = Password;
    }
    public void setPoints(int Points){
        points = Points;
    }
    public int getId(){
        return u_id;
    }
    public String getName(){
        return name;
    }
    public String getUserName(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public int getPoints(){
        return points;
    }
}
