package com.iti.tictactoeserver.controllers;

import com.iti.tictactoeserver.helpers.server.ClientHandler;
import javafx.fxml.FXML;
import com.iti.tictactoeserver.helpers.server.ClientListener;

public class Controller {
    private static ClientListener clientListener;

    @FXML
    protected void onActionStart() {
        clientListener = new ClientListener();
        clientListener.setDaemon(true);
        clientListener.start();
    }

    @FXML
    protected void onActionStop() {
        clientListener.interrupt();
        clientListener = null;
    }

}