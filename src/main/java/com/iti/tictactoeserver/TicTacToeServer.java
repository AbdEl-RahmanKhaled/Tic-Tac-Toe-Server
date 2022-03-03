package com.iti.tictactoeserver;

import com.iti.tictactoeserver.helpers.server.ClientListener;

public class TicTacToeServer {


    public static void main(String[] args) {
        new ClientListener().run();
    }
}