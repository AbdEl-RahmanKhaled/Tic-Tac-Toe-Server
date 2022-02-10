package com.iti.tictactoeserver.helpers.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.tictactoeserver.helpers.db.DbConnection;
import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.PlayerFullInfo;
import com.iti.tictactoeserver.notification.*;
import com.iti.tictactoeserver.requests.*;
import com.iti.tictactoeserver.responses.*;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;
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
        actions.put(Requests.ACTION_LOGIN, this::login);
        actions.put(Requests.ACTION_INVITE_TO_GAME, this::inviteToGame);
        actions.put(Requests.ACTION_ACCEPT_INVITATION, this::acceptInvitation);
        actions.put(Requests.ACTION_REJECT_INVITATION, this::rejectInvitation);
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
            // check if the competitor is still online and not in a game
            if (competitor != null && !competitor.myFullInfoPlayer.isInGame()) {
                // create the notification
                GameInvitationNotification gameInvitationNotification = new GameInvitationNotification(myFullInfoPlayer);
                // create json from the notification
                String jNotification = mapper.writeValueAsString(gameInvitationNotification);
                // send the game invitation to the competitor
                competitor.printStream.println(jNotification);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(inviteToGameReq.getPlayer(), competitor);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void acceptInvitation(String json) {
        try {
            // get the object from the json
            AcceptInvitationReq acceptInvitationReq = mapper.readValue(json, AcceptInvitationReq.class);
            // get the competitor
            ClientHandler competitor = getCompetitor(acceptInvitationReq.getPlayer());
            // check if the competitor is still online and not in a game
            if (competitor != null && !competitor.myFullInfoPlayer.isInGame()) {
                // link the two player with each other
                this.competitor = competitor;
                competitor.competitor = this;
                // create new match
                Match match = createMatch(acceptInvitationReq.getPlayer(), myFullInfoPlayer);
                // create start match notification
                StartGameNotification startGameNotification = new StartGameNotification(match);
                // create json
                String jNotification = mapper.writeValueAsString(startGameNotification);
                // send the notification to the two players to start the game
                competitor.printStream.println(jNotification);
                printStream.println(jNotification);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(acceptInvitationReq.getPlayer(), competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendOfflineOrInGame(Player player, ClientHandler competitor) throws JsonProcessingException {
        // create error response
        InviteToGameRes inviteToGameRes = new InviteToGameRes(InviteToGameRes.STATUS_ERROR, player);
        if (competitor == null) {
            inviteToGameRes.setMessage("Your competitor became offline now.");
        } else {
            inviteToGameRes.setMessage("It seems your competitor has entered another game.");
        }
        // create json from response
        String jResponse = mapper.writeValueAsString(inviteToGameRes);
        // send the response to the client
        printStream.println(jResponse);
    }

    private void rejectInvitation(String json) {
        try {
            // get the object from the json
            RejectInvitationReq rejectInvitationReq = mapper.readValue(json, RejectInvitationReq.class);
            // get the competitor
            ClientHandler competitor = getCompetitor(rejectInvitationReq.getPlayer());
            // check if the competitor is still online and not in a game
            if (competitor != null && !competitor.myFullInfoPlayer.isInGame()) {
                // create error response
                InviteToGameRes inviteToGameRes = new InviteToGameRes(InviteToGameRes.STATUS_ERROR, myFullInfoPlayer);
                inviteToGameRes.setMessage("It seems your competitor can not play with you at this moment.");
                // create json from response
                String jResponse = mapper.writeValueAsString(inviteToGameRes);
                // send the response to the client
                competitor.printStream.println(inviteToGameRes);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Match createMatch(Player p1, Player p2) {
        Match match = new Match();
        match.setPlayer1_id(p1.getDb_id());
        match.setPlayer2_id(p2.getDb_id());
        match.setM_date(new Timestamp(System.currentTimeMillis()));
        char[] choices = new char[]{Match.CHOICE_X, Match.CHOICE_O};
        match.setP1_choice(String.valueOf(choices[new Random().nextInt(3)]));
        match.setP2_choice(String.valueOf(choices[match.getP1_choice().equals(String.valueOf(Match.CHOICE_X)) ? 1 : 0]));
        return match;
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
                for (ClientHandler client : clients) {
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
