package com.iti.tictactoeserver.helpers.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.tictactoeserver.TicTacToeServer;
import com.iti.tictactoeserver.helpers.db.DbConnection;
import com.iti.tictactoeserver.models.Match;
import com.iti.tictactoeserver.models.MatchTable;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.PlayerFullInfo;
import com.iti.tictactoeserver.notification.*;
import com.iti.tictactoeserver.requests.*;
import com.iti.tictactoeserver.responses.*;
import javafx.application.Platform;
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
    private Socket mySocket;


    public ClientHandler(Socket socket) {
        initActions();
        try {
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            clients.put(this.getId(), this);
            System.out.println("No. Clients: " + clients.size());
            mySocket = socket;
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
        actions.put(Request.ACTION_SIGN_UP, this::signUp);
        actions.put(Request.ACTION_LOGIN, this::login);
        actions.put(Request.ACTION_ASK_TO_PAUSE, this::askToPause);
        actions.put(Request.ACTION_SAVE_MATCH, this::saveMatch);
        actions.put(Request.ACTION_REJECT_TO_PAUSE, this::rejectToPause);
        actions.put(Request.ACTION_SEND_MESSAGE, this::sendMessage);
        actions.put(Request.ACTION_GET_MATCH_HISTORY, this::getMatchHistory);
        actions.put(Request.ACTION_ASK_TO_RESUME, this::askToResume);
        actions.put(Request.ACTION_REJECT_TO_RESUME, this::rejectToResume);
        actions.put(Request.ACTION_ACCEPT_TO_RESUME, this::acceptToResume);
        actions.put(Request.ACTION_BACK_FROM_OFFLINE, this::backFromOffline);
    }


    @Override
    public void run() {
        while (true) {
            try {
                String jRequest = dataInputStream.readLine();
                System.out.println(jRequest);
                JSONObject json = new JSONObject(jRequest);
                String clientAction = (String) json.get("action");
                actions.get(clientAction).handleAction(jRequest);
            } catch (Exception e) {
                System.out.println("Stopped");
                dropConnection();
                System.out.println("No. of Clients: " + clients.size());
                e.printStackTrace();
                break;
            }
        }
    }

    public void getMatchHistory(String json) {
        try {
            //get the requested client u_id
            int u_id = clients.get(this.getId()).myFullInfoPlayer.getDb_id();
            //create response
            GetMatchHistoryRes getMatchHistoryRes = new GetMatchHistoryRes();
            //get matches from database
            List<MatchTable> userMatches = dbConnection.getMatchHistory(u_id);
            //if there are matches, send them back to the client
            if (userMatches != null) {
                getMatchHistoryRes.setStatus(GetMatchHistoryRes.STATUS_OK);
                getMatchHistoryRes.setMatches(userMatches);
                //convert the response to json String
                String jResponse = mapper.writeValueAsString(getMatchHistoryRes);
                printStream.println(jResponse);
            } else {
                getMatchHistoryRes.setStatus(GetMatchHistoryRes.STATUS_ERROR);
                getMatchHistoryRes.setMessage("No Matches So Far!");
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void askToPause(String json) {
        try {
            //create notification
            AskToPauseNotification askToPauseNotification = new AskToPauseNotification();
            //convert notification object to json string
            String jNotification = mapper.writeValueAsString(askToPauseNotification);
            //send the notification to the competitor
            clients.get(this.getId()).competitor.printStream.println(jNotification);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


    public void login(String json) {
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
            System.out.println(jResponse);
            printStream.println(jResponse);

        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public static void initPlayerList() {
        playersFullInfo = new HashMap<>();
        playersFullInfo = dbConnection.getAllPlayers(true);
        System.out.println(playersFullInfo.size());
        Platform.runLater(() -> TicTacToeServer.controller.fillPlayersTable(playersFullInfo.values()));
    }

    private void signUp(String json) {
        try {
            SignUpReq signUpReq = mapper.readValue(json, SignUpReq.class);
            PlayerFullInfo playerFullInfo = dbConnection.signUp(signUpReq.getUser());
            SignUpRes signUpRes = new SignUpRes();
            if (playerFullInfo != null) {
                signUpRes.setStatus(Response.STATUS_OK);
                signUpRes.setMessage("You have successfully registered");
                playersFullInfo.put(playerFullInfo.getDb_id(), playerFullInfo);
                updateStatus(playerFullInfo);
            } else {
                signUpRes.setStatus(Response.STATUS_ERROR);
                signUpRes.setMessage("Username You entered already exists!");
            }
            String jResponse = mapper.writeValueAsString(signUpRes);
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

    private void backFromOffline(String json) {
        try {
            BackFromOfflineReq backFromOfflineReq = mapper.readValue(json, BackFromOfflineReq.class);
            playersFullInfo.get(backFromOfflineReq.getPlayer().getDb_id()).setS_id(this.getId());
            playersFullInfo.get(backFromOfflineReq.getPlayer().getDb_id()).setStatus(Player.ONLINE);
            clients.get(this.getId()).myFullInfoPlayer = playersFullInfo.get(backFromOfflineReq.getPlayer().getDb_id());
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
            LoginRes loginRes = new LoginRes(
                    LoginRes.STATUS_OK,
                    clients.get(this.getId()).myFullInfoPlayer,
                    playersFullInfo
            );
            String jResponse = mapper.writeValueAsString(loginRes);
            printStream.println(jResponse);
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
                GameInvitationNotification gameInvitationNotification = new GameInvitationNotification(new Player(clients.get(this.getId()).myFullInfoPlayer));
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

    private void saveMatch(String json) {
        try {
            SaveMatchReq saveMatchReq = mapper.readValue(json, SaveMatchReq.class);
            dbConnection.saveMatch(saveMatchReq.getMatch(), saveMatchReq.getPositions());

            // if the game was with a player
            if (clients.get(this.getId()).competitor != null) {
                String jResponse;
                // if status of the match is paused
                if (saveMatchReq.getMatch().getStatus().equals(Match.STATUS_PAUSED)) {
                    PauseGameNotification pauseGameNotification = new PauseGameNotification();
                    jResponse = mapper.writeValueAsString(pauseGameNotification);
                } else {
                    int u_id = saveMatchReq.getMatch().getWinner();
                    //update points
                    if (dbConnection.updatePoints(u_id)) {
                        int points = playersFullInfo.get(u_id).getPoints();
                        playersFullInfo.get(u_id).setPoints(points + 1);
                        updateStatus(playersFullInfo.get(u_id));
                    }
                    // if status of the match is finished
                    FinishGameNotification finishGameNotification = new FinishGameNotification();
                    finishGameNotification.setWinner(u_id);
                    jResponse = mapper.writeValueAsString(finishGameNotification);
                }
                // if the competitor still connected
                if (clients.get(clients.get(this.getId()).competitor.getId()) != null) {
                    // notify the competitor the game status
                    clients.get(this.getId()).competitor.printStream.println(jResponse);
                    // update competitor in game status
                    clients.get(clients.get(this.getId()).competitor.getId()).myFullInfoPlayer.setInGame(false);
                    updateStatus(clients.get(clients.get(this.getId()).competitor.getId()).myFullInfoPlayer);
                    // unlink from the competitor
                    clients.get(clients.get(this.getId()).competitor.getId()).competitor = null;
                }
                // unlink from the competitor
                clients.get(this.getId()).competitor = null;
            }
            // update in game status for the player
            clients.get(this.getId()).myFullInfoPlayer.setInGame(false);
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void rejectToPause(String json) {
        try {
            Response response = new Response(Response.STATUS_ERROR, Response.RESPONSE_ASK_TO_PAUSE);
            String jResponse = mapper.writeValueAsString(response);
            clients.get(this.getId()).competitor.printStream.println(jResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void askToResume(String json) {
        try {
            // get object from json
            AskToResumeReq askToResumeReq = mapper.readValue(json, AskToResumeReq.class);
            // get competitor
            ClientHandler _competitor = clients.get(askToResumeReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create the notification
                AskToResumeNotification askToResumeNotification = new AskToResumeNotification(
                        new Player(clients.get(this.getId()).myFullInfoPlayer), askToResumeReq.getMatch());
                // create json from the notification
                String jNotification = mapper.writeValueAsString(askToResumeNotification);
                // send the game invitation to the competitor
                _competitor.printStream.println(jNotification);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(askToResumeReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void rejectToResume(String json) {
        try {
            // get object from json
            RejectToResumeReq rejectToResumeReq = mapper.readValue(json, RejectToResumeReq.class);
            // get the competitor
            ClientHandler _competitor = clients.get(rejectToResumeReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create error response
                AskToResumeRes askToResumeRes = new AskToResumeRes(AskToResumeRes.STATUS_ERROR, new Player(clients.get(this.getId()).myFullInfoPlayer));
                askToResumeRes.setMessage("It seems your competitor can not resume the game at this moment.");
                // create json from response
                String jResponse = mapper.writeValueAsString(askToResumeRes);
                // send the response to the client
                _competitor.printStream.println(jResponse);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void acceptToResume(String json) {
        try {
            AcceptToResumeReq acceptToResumeReq = mapper.readValue(json, AcceptToResumeReq.class);
            // get the competitor
            ClientHandler _competitor = clients.get(acceptToResumeReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // link the two player with each other
                clients.get(this.getId()).competitor = _competitor;
                clients.get(acceptToResumeReq.getPlayer().getS_id()).competitor = this;
                Match match = dbConnection.getMatch(acceptToResumeReq.getMatch().getM_id());
                // create resume match notification
                ResumeGameNotification resumeGameNotification = new ResumeGameNotification(match,
                        dbConnection.getPositions(acceptToResumeReq.getMatch()));
                // create json
                String jNotification = mapper.writeValueAsString(resumeGameNotification);
                // send the notification to the two players to start the game
                _competitor.printStream.println(jNotification);
                printStream.println(jNotification);
                // update in game status
                clients.get(this.getId()).myFullInfoPlayer.setInGame(true);
                clients.get(_competitor.getId()).myFullInfoPlayer.setInGame(true);
                // cast the status to all
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                updateStatus(clients.get(_competitor.getId()).myFullInfoPlayer);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(acceptToResumeReq.getPlayer(), _competitor);
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
            ClientHandler _competitor = clients.get(acceptInvitationReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // link the two player with each other
                clients.get(this.getId()).competitor = _competitor;
                clients.get(acceptInvitationReq.getPlayer().getS_id()).competitor = this;

                // create new match
                Match match = createMatch(acceptInvitationReq.getPlayer(), clients.get(this.getId()).myFullInfoPlayer);
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
                // cast the status to all
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                updateStatus(clients.get(_competitor.getId()).myFullInfoPlayer);
            } else {
                System.out.println("offline");
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
            ClientHandler _competitor = clients.get(rejectInvitationReq.getPlayer().getS_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create error response
                InviteToGameRes inviteToGameRes = new InviteToGameRes(InviteToGameRes.STATUS_ERROR, new Player(clients.get(this.getId()).myFullInfoPlayer));
                inviteToGameRes.setMessage("It seems your competitor " + clients.get(this.getId()).myFullInfoPlayer.getName() + " can not play with you at this moment.");
                // create json from response
                String jResponse = mapper.writeValueAsString(inviteToGameRes);
                // send the response to the client
                _competitor.printStream.println(jResponse);
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
        match.setP1_choice(String.valueOf(choices[new Random().nextInt(2)]));
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
                CompetitorConnectionIssueNotification competitorConnectionIssueNotification = new CompetitorConnectionIssueNotification();
                String jNotification = mapper.writeValueAsString(competitorConnectionIssueNotification);

                clients.get(this.getId()).competitor.printStream.println(jNotification);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        // if not logged in
        if (clients.get(this.getId()).myFullInfoPlayer != null) {
            clients.get(this.getId()).myFullInfoPlayer.setStatus(PlayerFullInfo.OFFLINE);
            clients.get(this.getId()).myFullInfoPlayer.setInGame(false);
            clients.get(this.getId()).myFullInfoPlayer.setS_id(-1);
        }
        updateStatus(clients.get(this.getId()).myFullInfoPlayer);
        clients.remove(this.getId());
    }

    private void updateInGameStatus(String json) {
        try {
            UpdateInGameStatusReq updateInGameStatusReq = mapper.readValue(json, UpdateInGameStatusReq.class);
            clients.get(this.getId()).myFullInfoPlayer.setInGame(updateInGameStatusReq.getInGame());
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
            // if were in game then competitor disconnected
            if (!updateInGameStatusReq.getInGame() && clients.get(this.getId()).competitor != null) {
                clients.get(this.getId()).competitor = null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(PlayerFullInfo playerFullInfo) {
        // update player status in the list
        playersFullInfo.put(playerFullInfo.getDb_id(), playerFullInfo);
        // create notification to send
        UpdateStatusNotification updateStatusNotification = new UpdateStatusNotification(playerFullInfo);
        try {
            // create json from the notification
            String jNotification = mapper.writeValueAsString(updateStatusNotification);
            // send to all client notification with now status for the player
            new Thread(() -> {
                for (ClientHandler client : clients.values()) {
                    if (client.myFullInfoPlayer != null) {
                        client.printStream.println(jNotification);
                    }
                }
            }).start();
            Platform.runLater(() -> TicTacToeServer.controller.fillPlayersTable(playersFullInfo.values()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void stopAll() {
        for (ClientHandler client : clients.values()) {
            client.interrupt();
        }
    }

    public static PlayerFullInfo getPlayerFullInfo(int db_id) {
        return playersFullInfo.get(db_id);
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
        void handleAction(String json);
    }
}
