package com.iti.tictactoeserver.helpers.server;
import com.iti.tictactoeserver.helpers.db.DbConnection;
import com.iti.tictactoeserver.models.PlayerFullInfo;
import com.google.gson.Gson;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread {

    private static List<ClientHandler> clients = new ArrayList<>();
    private static final DbConnection dbConnection = new DbConnection();
    private Map<String, IAction> actions = new HashMap<>();
    private static List<PlayerFullInfo> playersFullInfo;
    private PrintStream printStream;
    private DataInputStream dataInputStream;
    private static ObjectMapper mapper = new ObjectMapper();
    private static Gson gson = new Gson();


    public ClientHandler(Socket socket) {
        initActions();
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
            clients.add(this);
            System.out.println("No. Clients: " + clients.size());
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String jRequest = dataInputStream.readLine();
                JSONObject json = new JSONObject(jRequest);
                actions.get(json.get("action")).handleAction(jRequest);
            } catch (Exception e) {
                System.out.println("Stopped");
                clients.remove(this);
                System.out.println("No. of Clients: " + clients.size());
                break;
            }
        }
    }

    private void initActions() {
        actions.put("login", this::login);
    }

    public static void initPlayerList() {
        playersFullInfo = new ArrayList<>();
        playersFullInfo = dbConnection.getAllPlayers();
        System.out.println(playersFullInfo.size());
    }

    private void login(String json) {


    }

    interface IAction {
        void handleAction(String json);
    }
}
