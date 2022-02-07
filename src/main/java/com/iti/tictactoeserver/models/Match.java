package com.iti.tictactoeserver.models;
import java.sql.Timestamp;
import java.time.LocalDateTime;

    public class Match {
        private int m_id, player1_id, player2_id, winner;
        private String p1_choice, p2_choice, status, level;
        private Timestamp m_date;
        public static final char CHOICE_X = 'X';
        public static final char CHOICE_O = 'O';
        public static final String STATUS_FINISHED = "finished";
        public static final String STATUS_PAUSED = "paused";

        public Match() {
        }

        public Match(int player1_id, int player2_id, int winner, String p1_choice, String p2_choice, String status, Timestamp m_date) {
            this.player1_id = player1_id;
            this.player2_id = player2_id;
            this.winner = winner;
            this.p1_choice = p1_choice;
            this.p2_choice = p2_choice;
            this.status = status;
            this.m_date = m_date;
        }

        public Match(int player1_id, int player2_id, String p1_choice, String p2_choice, String status, Timestamp m_date) {
            this.player1_id = player1_id;
            this.player2_id = player2_id;
            this.p1_choice = p1_choice;
            this.p2_choice = p2_choice;
            this.status = status;
            this.m_date = m_date;
        }

        public int getM_id() {
            return m_id;
        }

        public void setM_id(int m_id) {
            this.m_id = m_id;
        }

        public int getPlayer1_id() {
            return player1_id;
        }

        public void setPlayer1_id(int player1_id) {
            this.player1_id = player1_id;
        }

        public int getPlayer2_id() {
            return player2_id;
        }

        public void setPlayer2_id(int player2_id) {
            this.player2_id = player2_id;
        }

        public int getWinner() {
            return winner;
        }

        public void setWinner(int winner) {
            this.winner = winner;
        }

        public String getP1_choice() {
            return p1_choice;
        }

        public void setP1_choice(String p1_choice) {
            this.p1_choice = p1_choice;
        }

        public String getP2_choice() {
            return p2_choice;
        }

        public void setP2_choice(String p2_choice) {
            this.p2_choice = p2_choice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public Timestamp getM_date() {
            return m_date;
        }

        public void setM_date(Timestamp m_date) {
            this.m_date = m_date;
        }
    }
}
