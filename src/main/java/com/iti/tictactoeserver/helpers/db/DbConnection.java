package com.iti.tictactoeserver.helpers.db;

import com.iti.tictactoeserver.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public PlayerFullInfo signUp(User user) {
        if (ValidateUserName(user.getUserName())) {
            return null;
        } else {
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement("insert into users (name , username , password ) values (? , ? , ?)");
                pst.setString(1, user.getName());
                pst.setString(2, user.getUserName());
                pst.setString(3, user.getPassword());
                //pst.setInt(4, user.getPoints());
                pst.execute();
                return getPlayerInfo(user.getUserName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
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

    private PlayerFullInfo getPlayerInfo(String username) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("select u_id, name, points from users where username = ?");
        stm.setString(1, username);
        ResultSet resultSet = stm.executeQuery();
        resultSet.next();
        return new PlayerFullInfo(resultSet.getInt("u_id"),
                resultSet.getString("name"),
                resultSet.getInt("points"));
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
            int index = 0;
            while (resultSet.next()) {
                players.add(new PlayerFullInfo(
                        index,
                        resultSet.getInt("u_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("points")
                ));
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }


    public Map<Integer, PlayerFullInfo> getAllPlayers(boolean b) {
        Map<Integer, PlayerFullInfo> players = new HashMap<>();
        try {
            PreparedStatement stm = connection.prepareStatement("select u_id, name, points from users ");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                int db_id = resultSet.getInt("u_id");
                players.put(db_id,
                        new PlayerFullInfo(db_id,
                                resultSet.getString("name"),
                                resultSet.getInt("points")
                        ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public int authenticate(Credentials credentials) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String queryString = new String("select u_id from users where username='" + credentials.getUserName() + "' and password='" + credentials.getPassword() + "';");
        ResultSet rs = stmt.executeQuery(queryString);
        if (rs.first())
            return rs.getInt("u_id");
        return -1;
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

    public List<Match> getMatchHistory(int u_id) throws SQLException {
        List<Match> matches = new ArrayList<Match>();
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String queryString = new String("select * from matches where player1_id='"+u_id+"' or player2_id='"+u_id+"';");
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
