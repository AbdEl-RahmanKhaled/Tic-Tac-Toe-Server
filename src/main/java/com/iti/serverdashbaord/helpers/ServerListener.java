package com.iti.serverdashbaord.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.serverdashbaord.ServerDashboard;
import com.iti.serverdashbaord.controllers.DashboardController;
import com.iti.serverdashbaord.notification.Notification;
import com.iti.serverdashbaord.notification.ServerStartedNotification;
import com.iti.serverdashbaord.notification.UpdateStatusNotification;
import javafx.application.Platform;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerListener extends Thread {
    private static final int PORT = 5001;
    private static final String HOST = "127.0.0.1";
    private static PrintStream printStream;
    private Socket socket;
    private BufferedReader bufferedReader;
    private Map<String, IType> types;
    private boolean running, first;

    public ServerListener() {
        running = true;
        first = true;
        initTypes();
    }

    private void initConnection() {
        try {
            socket = new Socket(HOST, PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            System.out.println("connected");
            backFromOffline();
            first = false;
        } catch (Exception ex) {
            System.out.println("Failed to connect");
            if (first) {
                first = false;
                goOffline();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
            initConnection();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                String sMessage = bufferedReader.readLine();
                System.out.println(sMessage);
                JSONObject json = new JSONObject(sMessage);
                String serverType = (String) json.get("type");
                if (types.get(serverType) != null)
                    types.get(serverType).handleAction(sMessage);
            } catch (Exception e) {
                if (running) {
                    goOffline();
                    initConnection();
                }
            }
        }
    }

    private void initTypes() {
        types = new HashMap<>();
        types.put(Notification.NOTIFICATION_SERVER_STARTED, this::serverStartedNotification);
        types.put(Notification.NOTIFICATION_SERVER_STOPPED, this::serverStoppedNotification);
        types.put(Notification.NOTIFICATION_UPDATE_STATUS, this::updateStatusNotification);
    }

    private void updateStatusNotification(String json) {
        try {
            UpdateStatusNotification updateStatusNotification = ServerDashboard.mapper.readValue(json, UpdateStatusNotification.class);
            Platform.runLater(() -> ServerDashboard.dashboardController.update(updateStatusNotification.getPlayerFullInfo()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void serverStoppedNotification(String json) {
        Platform.runLater(() -> ServerDashboard.dashboardController.stopped());
    }

    private void serverStartedNotification(String json) {
        try {
            ServerStartedNotification serverStartedNotification = ServerDashboard.mapper.readValue(json, ServerStartedNotification.class);
            Platform.runLater(() -> ServerDashboard.dashboardController.fillPlayersTable(serverStartedNotification.getPlayerFullInfoMap()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void goOffline() {
        if (!first) {
            Platform.runLater(() -> {
                ServerDashboard.dashboardController.offline(true);
            });
        }
    }


    public static void sendRequest(String json) {
        try {
            System.out.println(json);
            printStream.println(json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void backFromOffline() {
        Platform.runLater(() -> {
            ServerDashboard.dashboardController.offline(false);
        });
    }


    @Override
    public void interrupt() {
        super.interrupt();
        try {
            running = false;
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    interface IType {
        void handleAction(String json);
    }
}