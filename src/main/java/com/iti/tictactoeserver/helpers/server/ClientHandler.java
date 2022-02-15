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

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class ClientHandler extends Thread {

    private static final DbConnection dbConnection = new DbConnection();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<Long, ClientHandler> clients = new HashMap<>();
    private static Map<Integer, PlayerFullInfo> playersFullInfo;
    private Map<String, IAction> actions;
    private PrintStream printStream;
    private BufferedReader dataInputStream;
    private ClientHandler competitor;
    private PlayerFullInfo myFullInfoPlayer;


    public ClientHandler(Socket socket) {
        initActions();
        try {
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            clients.put(this.getId(), this);
            System.out.println("No. Clients: " + clients.size());
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initActions() {
        actions = new HashMap<>();
        actions.put(Request.ACTION_INVITE_TO_GAME, this::inviteToGame);
        actions.put(Request.ACTION_ACCEPT_INVITATION, this::acceptInvitation);
        actions.put(Request.ACTION_REJECT_INVITATION, this::rejectInvitation);
        actions.put(Request.ACTION_UPDATE_BOARD, this::updateBoard);
        actions.put(Request.ACTION_UPDATE_IN_GAME_STATUS, this::updateInGameStatus);
        actions.put(Request.ACTION_LOGIN, this::Login);
        actions.put(Request.ACTION_SIGN_UP, this::signUp);
        actions.put(Request.ACTION_ACCEPT_TO_PAUSE, this::acceptToPause);
        actions.put(Request.ACTION_REJECT_TO_PAUSE, this::rejectToPause);
        actions.put(Request.ACTION_SEND_MESSAGE, this::sendMessage);
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
                dropConnection();
                System.out.println("No. of Clients: " + clients.size());
                break;
            }
        }
    }

    public void Login(String json) {
        try {
            LoginReq loginReq = mapper.readValue(json, LoginReq.class);
            LoginRes loginRes = new LoginRes();
            int u_id = dbConnection.authenticate(loginReq.getCredentials());
            if (u_id != -1) {
                playersFullInfo.get(u_id).setStatus(PlayerFullInfo.ONLINE);
                playersFullInfo.get(u_id).setS_id(this.getId());
                clients.get(this.getId()).myFullInfoPlayer = playersFullInfo.get(u_id);
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                loginRes.setStatus(LoginRes.STATUS_OK);
                loginRes.setPlayerFullInfo(playersFullInfo.get(u_id));
                loginRes.setPlayerFullInfoMap(playersFullInfo);
            } else {
                loginRes.setStatus(LoginRes.STATUS_ERROR);
                loginRes.setMessage("Incorrect Password or Username.");
            }
            String jResponse = mapper.writeValueAsString(loginRes);
            printStream.println(jResponse);
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public static void initPlayerList() {
        playersFullInfo = new HashMap<>();
        playersFullInfo = dbConnection.getAllPlayers(true);
        System.out.println(playersFullInfo.size());
    }

    private void signUp(String json) {
        try {
            SignUpReq signUpReq = mapper.readValue(json, SignUpReq.class);
            PlayerFullInfo playerFullInfo = dbConnection.signUp(signUpReq.getUser());
            Response response = new Response();
            if (playerFullInfo != null) {
                response.setStatus(Response.STATUS_OK);
                response.setMessage("You have successfully registered");
                playersFullInfo.put(playerFullInfo.getDb_id(), playerFullInfo);
            } else {
                response.setStatus(Response.STATUS_ERROR);
                response.setMessage("Username You entered already exists!");
            }
            String jResponse = mapper.writeValueAsString(response);
            printStream.println(jResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String json) {
        try {
            // get the object from the json
            SendMessageReq sendMessageReq = mapper.readValue(json, SendMessageReq.class);
            // create notification for message
            MessageNotification messageNotification = new MessageNotification(sendMessageReq.getMessage());
            // convert notification to json
            String jNotification = mapper.writeValueAsString(messageNotification);
            // get the competitor socket then send him the message
            clients.get(this.getId()).competitor.printStream.println(jNotification);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void inviteToGame(String json) {
        try {
            // convert json to java object
            InviteToGameReq inviteToGameReq = mapper.readValue(json, InviteToGameReq.class);
            // get the competitor and send the notification
//            ClientHandler competitor = getCompetitor(inviteToGameReq.getPlayer());
            ClientHandler _competitor = clients.get(inviteToGameReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create the notification
                GameInvitationNotification gameInvitationNotification = new GameInvitationNotification(clients.get(this.getId()).myFullInfoPlayer);
                // create json from the notification
                String jNotification = mapper.writeValueAsString(gameInvitationNotification);
                // send the game invitation to the competitor
                _competitor.printStream.println(jNotification);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(inviteToGameReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void acceptToPause(String json) {
        try {
            AcceptToPauseReq acceptToPauseReq = mapper.readValue(json, AcceptToPauseReq.class);
            dbConnection.saveMatch(acceptToPauseReq.getMatch(), acceptToPauseReq.getPositions());
            Response response = new Response(Response.STATUS_OK, Response.RESPONSE_ASK_TO_PAUSE);
            String jResponse = mapper.writeValueAsString(response);
            clients.get(this.getId()).competitor.printStream.println(jResponse);
            clients.get(clients.get(this.getId()).competitor.getId()).competitor = null;
            clients.get(this.getId()).competitor = null;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void rejectToPause(String json) {
        try {
            Response response = new Response(Response.STATUS_ERROR,Response.RESPONSE_ASK_TO_PAUSE);
            String jResponse = mapper.writeValueAsString(response);
            clients.get(this.getId()).competitor.printStream.println(jResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void acceptInvitation(String json) {
        try {
            // get the object from the json
            AcceptInvitationReq acceptInvitationReq = mapper.readValue(json, AcceptInvitationReq.class);
            // get the competitor
//            ClientHandler competitor = getCompetitor(acceptInvitationReq.getPlayer());
            ClientHandler _competitor = clients.get(acceptInvitationReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // link the two player with each other
                clients.get(this.getId()).competitor = _competitor;
                clients.get(acceptInvitationReq.getPlayer().getS_id()).competitor = this;
//                competitor.competitor = this;
                // create new match
                Match match = createMatch(acceptInvitationReq.getPlayer(), myFullInfoPlayer);
                // create start match notification
                StartGameNotification startGameNotification = new StartGameNotification(match);
                // create json
                String jNotification = mapper.writeValueAsString(startGameNotification);
                // send the notification to the two players to start the game
                _competitor.printStream.println(jNotification);
                printStream.println(jNotification);
                // update in game status
                clients.get(this.getId()).myFullInfoPlayer.setInGame(true);
                clients.get(_competitor.getId()).myFullInfoPlayer.setInGame(true);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(acceptInvitationReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendOfflineOrInGame(Player player, ClientHandler _competitor) throws JsonProcessingException {
        // create error response
        InviteToGameRes inviteToGameRes = new InviteToGameRes(InviteToGameRes.STATUS_ERROR, player);
        if (_competitor == null) {
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
//            ClientHandler competitor = getCompetitor(rejectInvitationReq.getPlayer());
            ClientHandler _competitor = clients.get(rejectInvitationReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create error response
                InviteToGameRes inviteToGameRes = new InviteToGameRes(InviteToGameRes.STATUS_ERROR, myFullInfoPlayer);
                inviteToGameRes.setMessage("It seems your competitor can not play with you at this moment.");
                // create json from response
                String jResponse = mapper.writeValueAsString(inviteToGameRes);
                // send the response to the client
                _competitor.printStream.println(inviteToGameRes);
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

    private void updateBoard(String json) {
        try {
            // get the object from the json
            UpdateBoardReq updateBoardReq = mapper.readValue(json, UpdateBoardReq.class);
            //create update board notification
            UpdateBoardNotification updateBoardNotification = new UpdateBoardNotification(updateBoardReq.getPosition());
            // create json
            String jNotification = mapper.writeValueAsString(updateBoardNotification);
            // send the notification to the players
            clients.get(this.getId()).competitor.printStream.println(jNotification);
            printStream.println(jNotification);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void dropConnection() {
        // check if were playing with a competitor
        if (clients.get(this.getId()).competitor != null) {
            try {
                // notify the competitor
                Notification notification = new Notification(Notification.NOTIFICATION_COMPETITOR_CONNECTION_ISSUE);
                String jNotification = mapper.writeValueAsString(notification);
                clients.get(this.getId()).competitor.printStream.println(jNotification);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        clients.get(this.getId()).myFullInfoPlayer.setStatus(PlayerFullInfo.OFFLINE);
        clients.get(this.getId()).myFullInfoPlayer.setS_id(-1);
        updateStatus(clients.get(this.getId()).myFullInfoPlayer);
        clients.remove(this.getId());
    }

    private void updateInGameStatus(String json) {
        try {
            UpdateInGameStatusReq updateInGameStatusReq = mapper.readValue(json, UpdateInGameStatusReq.class);
            clients.get(this.getId()).myFullInfoPlayer.setInGame(updateInGameStatusReq.getInGame());
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(PlayerFullInfo playerFullInfo) {
        // update player status in the list
//        playersFullInfo.set(playerFullInfo.getIndex(), playerFullInfo);
        playersFullInfo.put(playerFullInfo.getDb_id(), playerFullInfo);
        // create notification to send
        UpdateStatusNotification updateStatusNotification = new UpdateStatusNotification(playerFullInfo);
        try {
            // create json from the notification
            String jNotification = mapper.writeValueAsString(updateStatusNotification);
            // send to all client notification with now status for the player
            new Thread(() -> {
                for (ClientHandler client : clients.values()) {
                    if (client.getId() != playerFullInfo.getS_id()) {
                        client.printStream.println(jNotification);
                    }
                }
            }).start();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    private PlayerFullInfo getPlayer(long s_id) {
//        return playersFullInfo.stream().filter(playerFullInfo -> playerFullInfo.getS_id() == s_id)
//                .collect(Collectors.toList()).get(0);
//    }


//    private ClientHandler getCompetitor(Player player) {
//        List<ClientHandler> competitor = clients.stream()
//                .filter(clientHandler -> clientHandler.getId() == player.getS_id()).collect(Collectors.toList());
//        if (competitor.isEmpty())
//            return null;
//        else
//            return competitor.get(0);
//    }

    interface IAction {
        void handleAction(String json);
    }
}
