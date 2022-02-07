package com.iti.tictactoeserver.helpers.db;

import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.User;

import java.sql.*;

public class DbConnection {
    private static final String dbName = "xo_db";
    private static final String dbHost = "ec2-18-130-5-5.eu-west-2.compute.amazonaws.com";
    private static final String dbPort = "80";
    private static final String dbUser = "postgres";
    private static final String dbPass = "admin";
    private Connection connection;
    ResultSet rsuser;
    ResultSet rsmatch;

    public DbConnection() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName,
                    dbUser, dbPass);
            System.out.println("connected To DB...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean signUp() {
        if (ValidateUserName()) {
            return false;
        } else {
            User user = new User();
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement("insert into users (name , username , password , points) values (? , ? , ? , ?)");
                pst.setString(2, user.getName());
                pst.setString(3, user.getUserName());
                pst.setString(4, user.getPassword());
                pst.setInt(5, user.getPoints());
                rsuser = pst.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    public boolean ValidateUserName() {
        return rsuser.next();
    }

    public void getPositions() {
        if(ValidateMatch()) {
            Match match = new Match();
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement("select position from positions where m_id = ?");
                pst.setInt(1, match.getM_id());
                rsmatch = pst.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Match does not exist!");
        }
    }
    public boolean ValidateMatch(){
        return rsmatch.next();
    }
}