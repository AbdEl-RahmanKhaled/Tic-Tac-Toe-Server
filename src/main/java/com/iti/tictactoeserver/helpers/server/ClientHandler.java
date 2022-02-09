package com.iti.tictactoeserver.helpers.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.tictactoeserver.helpers.db.DbConnection;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.PlayerFullInfo;
import com.iti.tictactoeserver.notification.*;
import com.iti.tictactoeserver.requests.*;
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
import java.util.stream.Collectors;

public class ClientHandler extends Thread {

    private static List<ClientHandler> clients = new ArrayList<>();
    private static final DbConnection dbConnection = new DbConnection();
    private Map<String, IAction> actions = new HashMap<>();
    private static List<PlayerFullInfo> playersFullInfo;
    private PrintStream printStream;
    private DataInputStream dataInputStream;
    private static ObjectMapper mapper = new ObjectMapper();
    private ClientHandler competitor;
    private PlayerFullInfo myFullInfoPlayer;
//    private static Gson gson = new Gson();

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

    private void initActions() {
        actions.put("login", this::login);
        actions.put("inviteToGame", this::inviteToGame);
    }


    @Override
    public void run() {
        while (true) {
            try {
                String jRequest = dataInputStream.readLine();
                JSONObject json = new JSONObject(jRequest);
                String clientAction = (String) json.get("action");
                actions.get(clientAction).handleAction(jRequest);
            } catch (Exception e) {
                System.out.println("Stopped");
                myFullInfoPlayer.setStatus(PlayerFullInfo.OFFLINE);
                myFullInfoPlayer.setS_id(-1);
                updateStatus(myFullInfoPlayer);
                clients.remove(this);
                System.out.println("No. of Clients: " + clients.size());
                break;
            }
        }
    }


    public static void initPlayerList() {
        playersFullInfo = new ArrayList<>();
        playersFullInfo = dbConnection.getAllPlayers();
        System.out.println(playersFullInfo.size());
    }

    private void login(String json) {


    }

    private void inviteToGame(String json) {

        try {
            // convert json to java object
            InviteToGameReq inviteToGameReq = mapper.readValue(json, InviteToGameReq.class);
            // get the competitor and send the notification
            ClientHandler competitor = getCompetitor(inviteToGameReq.getPlayer());
            // check if the competitor is still online and not in game
            if (competitor != null && !competitor.myFullInfoPlayer.isInGame()) {
                // create the notification
                GameInvitationNotification gameInvitationNotification = new GameInvitationNotification(myFullInfoPlayer);
                // create json from the notification
                String jNotification = mapper.writeValueAsString(gameInvitationNotification);
                // send the game invitation to the competitor
                competitor.printStream.println(jNotification);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private ClientHandler getCompetitor(Player player) {
        List<ClientHandler> competitor = clients.stream()
                .filter(clientHandler -> clientHandler.getId() == player.getS_id()).collect(Collectors.toList());
        if (competitor.isEmpty())
            return null;
        else
            return competitor.get(0);
    }

    private void updateStatus(PlayerFullInfo playerFullInfo) {
        // update player status in the list
        playersFullInfo.set(playerFullInfo.getIndex(), playerFullInfo);
        // create notification to send
        UpdateStatusNotification updateStatusNotification = new UpdateStatusNotification(playerFullInfo);
        try {
            // create json from the notification
            String jNotification = mapper.writeValueAsString(updateStatusNotification);
            // send to all client notification with now status for the player
            new Thread(() -> {
                for(ClientHandler client: clients){
                    client.printStream.println(jNotification);
                }
            }).start();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private PlayerFullInfo getPlayer(long s_id) {
        return playersFullInfo.stream().filter(playerFullInfo -> playerFullInfo.getS_id() == s_id)
                .collect(Collectors.toList()).get(0);
    }

    interface IAction {
        void handleAction(String json);
    }
}
