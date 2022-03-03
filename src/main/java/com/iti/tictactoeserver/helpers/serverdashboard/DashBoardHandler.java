package com.iti.tictactoeserver.helpers.serverdashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.tictactoeserver.helpers.server.ClientHandler;
import com.iti.tictactoeserver.helpers.server.ClientListener;
import com.iti.tictactoeserver.notification.Notification;
import com.iti.tictactoeserver.notification.ServerStartedNotification;
import com.iti.tictactoeserver.requests.Request;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardHandler extends Thread {
    private static final List<DashBoardHandler> dashboards = new ArrayList<>();
    private static  ClientListener clientListener;
    private static boolean isStarted = false;
    private Map<String, IAction> actions;
    private PrintStream printStream;
    private BufferedReader dataInputStream;
    private Socket mySocket;

    public DashBoardHandler(Socket socket) {
        initActions();
        try {
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            dashboards.add(this);
            System.out.println("No. Dashboards: " + dashboards.size());
            mySocket = socket;
            start();
            if (isStarted) sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String jRequest = dataInputStream.readLine();
                System.out.println(jRequest);
                JSONObject json = new JSONObject(jRequest);
                String clientAction = (String) json.get("action");
                actions.get(clientAction).handleAction();
            } catch (Exception e) {
                System.out.println("Stopped");
                dashboards.remove(this);
                System.out.println("No. of Dashboards: " + dashboards.size());
//                e.printStackTrace();
                break;
            }
        }
    }

    private void initActions() {
        actions = new HashMap<>();
        actions.put(Request.ACTION_START_SERVER, this::startServer);
        actions.put(Request.ACTION_STOP_SERVER, this::stopServer);
    }

    private void stopServer() {
        if (isStarted) {
            isStarted = false;
            clientListener.interrupt();
            try {
                sendUpdate(ClientHandler.mapper.writeValueAsString(new Notification(Notification.NOTIFICATION_SERVER_STOPPED)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void startServer() {
        if (!isStarted) {
            isStarted = true;
            clientListener = new ClientListener();
            clientListener.setDaemon(true);
            clientListener.start();
        }
    }

    public static void sendUpdate(String json) {
        new Thread(() -> {
            for (DashBoardHandler dashboard : dashboards) {
                dashboard.printStream.println(json);
            }
        }).start();
    }

    public static void sendData() {
        ServerStartedNotification serverStartedNotification = new ServerStartedNotification(ClientHandler.getPlayersFullInfo());
        try {
            sendUpdate(ClientHandler.mapper.writeValueAsString(serverStartedNotification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public static void stopAll() {
        for (DashBoardHandler dashBoard : dashboards) {
            dashBoard.interrupt();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface IAction {
        void handleAction();
    }
}
