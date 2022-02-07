package com.iti.tictactoeserver.helpers.db;

import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.PlayerFullInfo;
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

    public void saveMatch(Match match, List<Position> positions) {
        // insert match into the database
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO matches (m_date, player1_id, " +
                    "player2_id, p1_choice, p2_choice, status, winner, level) VALUES( ?, ?, ?, ?, ?, ?, ?, ?);");
            stm.setTimestamp(1, match.getM_date());
            stm.setInt(2, match.getPlayer1_id());
            stm.setInt(3, match.getPlayer2_id());
            stm.setString(4, match.getP1_choice());
            stm.setString(5, match.getP2_choice());
            stm.setString(6, match.getStatus());
            stm.setInt(7, match.getWinner());
            stm.setString(8, match.getLevel());
            boolean done = stm.execute();

            if (done) {
                // get match id if match inserted successfully
                int m_id = selectMatchId(match.getM_date(), match.getPlayer1_id(), match.getPlayer2_id());
                if (m_id != -1) {
                    // insert positions
                    insertPositions(positions, m_id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int selectMatchId(Timestamp m_date, int p1_id, int p2_id) {
        int m_id = -1;
        try {
            PreparedStatement stm = connection.prepareStatement("select m_id from matches " +
                    "where m_date=? and player1_id=? and player2_id=?");
            stm.setTimestamp(1, m_date);
            stm.setInt(2, p1_id);
            stm.setInt(3, p2_id);

            m_id = stm.executeQuery().getInt("m_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m_id;
    }

    private void insertPositions(List<Position> positions, int m_id) {
        for (Position position : positions) {
            try {
                PreparedStatement stm = connection.prepareStatement("INSERT INTO positions (m_id, player_id, " +
                        "position) VALUES( ?, ?, ?);");
                stm.setInt(1, m_id);
                stm.setInt(2, position.getPlayer_id());
                stm.setString(3, position.getPosition());
                stm.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<PlayerFullInfo> getAllPlayers() {
        List<PlayerFullInfo> players = new ArrayList<>();
        try {
            PreparedStatement stm = connection.prepareStatement("select u_id, name, points from users ");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                players.add(new PlayerFullInfo(
                        resultSet.getInt("u_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("points")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }


    public boolean authenticate(User usr) throws SQLException {
        Statement stmt = connection.createStatement();
        String queryString = new String("select * from users where username='" + usr.getUser_name() + "' && password='" + usr.getPassword() + "';");
        ResultSet rs = stmt.executeQuery(queryString);
        if (rs.first())
            return true;
        return false;
    }

    public List<Match> getMatchHistory() throws SQLException {
        List<Match> matches = new ArrayList<Match>();
        Statement stmt = connection.createStatement();
        String queryString = new String("select * from matches;");
        ResultSet rs = stmt.executeQuery(queryString);
        if (!rs.next()) {
            return null;
        }
        do {
            Match match = new Match();
            matches.add(match);
        } while (rs.next());
        return matches;
    }
}
