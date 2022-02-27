package com.iti.tictactoeserver;

import com.iti.tictactoeserver.controllers.Controller;
import com.iti.tictactoeserver.helpers.server.ClientListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TicTacToeServer{
    public static Controller controller;

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(TicTacToeServer.class.getResource("main-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 533, 443);
//        controller = fxmlLoader.getController();
//        stage.setTitle("Server");
//        stage.setScene(scene);
//        stage.show();
//    }

    public static void main(String[] args) {
//        launch();
        new ClientListener().run();
    }
}