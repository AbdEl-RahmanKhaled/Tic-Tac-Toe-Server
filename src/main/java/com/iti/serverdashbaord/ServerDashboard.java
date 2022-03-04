package com.iti.serverdashbaord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iti.serverdashbaord.controllers.DashboardController;
import com.iti.serverdashbaord.helpers.ServerListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerDashboard extends Application {
    public static final ObjectMapper mapper = new ObjectMapper();
    public static DashboardController dashboardController;

    @Override
    public void init() throws Exception {
        super.init();
        ServerListener serverListener = new ServerListener();
        serverListener.setDaemon(true);
        serverListener.start();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerDashboard.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  533, 443);
        dashboardController = fxmlLoader.getController();
        stage.setTitle("Server Dashboard");
        stage.setScene(scene);
        stage.show();
    }

}