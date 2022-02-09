package com.iti.tictactoeserver.helpers.db;

import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.Position;
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

    public boolean signUp(User user) {
        if (ValidateUserName(user.getUserName())) {
            return false;
        } else {
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement("insert into users (name , username , password ) values (? , ? , ?)");
                pst.setString(1, user.getName());
                pst.setString(2, user.getUserName());
                pst.setString(3, user.getPassword());
                //pst.setInt(4, user.getPoints());
                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public boolean ValidateUserName(String user_name) {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("select * from user where userName = ?");
            p.setString(1, user_name);
            ResultSet result = p.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Position> getPositions(Match match) {
        List<Position> positions = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select position from positions where m_id = ?");
            pst.setInt(1, match.getM_id());
            ResultSet rsmatch = pst.executeQuery();
            while (rsmatch.next()) {
                positions.add(new Position(rsmatch.getInt("m_id"),
                        rsmatch.getInt("player_id"),
                        rsmatch.getString("position")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }
}
