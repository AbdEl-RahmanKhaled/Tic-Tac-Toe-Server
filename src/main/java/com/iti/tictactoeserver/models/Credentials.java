package com.iti.tictactoeserver.models;

public class Credentials {
    protected String username;
    protected String password;

    public void setUserName(String userName) {
        username = userName;
    }

    public void setPassword(String Password) {
        password = Password;
    }
    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
