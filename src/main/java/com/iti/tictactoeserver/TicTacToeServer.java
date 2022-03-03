package com.iti.tictactoeserver;

import com.iti.tictactoeserver.controllers.Controller;
import com.iti.tictactoeserver.helpers.serverdashboard.DashBoardListener;

public class TicTacToeServer{
    public static Controller controller;

    public static void main(String[] args) {
        new DashBoardListener().run();
    }
}