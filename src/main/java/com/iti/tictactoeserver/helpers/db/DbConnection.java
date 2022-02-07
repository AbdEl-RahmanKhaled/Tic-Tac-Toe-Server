package com.iti.tictactoeserver.helpers.db;

import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {
    private static final String dbName = "xo_db";
    private static final String dbHost = "ec2-18-130-5-5.eu-west-2.compute.amazonaws.com";
    private static final String dbPort = "80";
    private static final String dbUser = "postgres";
    private static final String dbPass = "admin";
    private Connection connection;

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

    public boolean auhenticate(User usr) throws SQLException {
        Statement stmt = connection.createStatement();
        String queryString = new String("select * from users where username='"+usr.getUsername()+"' && password='"+usr.getPassword()+"';");
        ResultSet rs = stmt.executeQuery(queryString);
        if(rs.first())
            return true;
        return false;
    }

    public List<Match> getMatchHistory () throws SQLException {
        List<Match> matches= new ArrayList<Match>();
        Statement stmt = connection.createStatement();
        String queryString = new String("select * from matches;");
        ResultSet rs = stmt.executeQuery(queryString);
        if(!rs.next()){
            return null;
        }
        do{
            Match match = new Match();
            matches.add(match);
        }while(rs.next());
        return matches;
    }



}
