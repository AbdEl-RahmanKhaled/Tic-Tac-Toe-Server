package com.iti.tictactoeserver;

import javafx.fxml.FXML;
import com.iti.tictactoeserver.helpers.server.ClientListener;

public class Controller {
    private static final ClientListener clientListener = new ClientListener();

    @FXML
    protected void onActionStart() {
        clientListener.setDaemon(true);
        clientListener.start();
    }

    @FXML
    protected void onActionStop() {
        clientListener.interrupt();
    }

}