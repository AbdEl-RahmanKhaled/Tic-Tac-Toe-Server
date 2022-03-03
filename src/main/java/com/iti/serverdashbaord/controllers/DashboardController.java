package com.iti.serverdashbaord.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.serverdashbaord.ServerDashboard;
import com.iti.serverdashbaord.helpers.ServerListener;
import com.iti.serverdashbaord.models.PlayerFullInfo;
import com.iti.serverdashbaord.requests.Request;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private static boolean isStarted = false;
    private static Map<Integer, PlayerFullInfo> playersFullInfo;

    @FXML
    private TableView<PlayerFullInfo> tPlayers;
    @FXML
    private TableColumn<PlayerFullInfo, String> cPlayerName;
    @FXML
    private TableColumn<PlayerFullInfo, String> cStatus;
    @FXML
    private TableColumn<PlayerFullInfo, Boolean> cIsInGame;
    @FXML
    private TableColumn<PlayerFullInfo, Integer> cScore;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnStart, btnStop;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cPlayerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        cIsInGame.setCellValueFactory(new PropertyValueFactory<>("inGame"));
        cScore.setCellValueFactory(new PropertyValueFactory<>("points"));
        cStatus.setComparator(cStatus.getComparator().reversed());
        lblStatus.setText("Stopped");
    }

    @FXML
    protected void onActionStart() {
        if (!isStarted) {
            try {
                ServerListener.sendRequest(ServerDashboard.mapper.writeValueAsString(new Request(Request.ACTION_START_SERVER)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onActionStop() {
        if (isStarted) {
            try {
                ServerListener.sendRequest(ServerDashboard.mapper.writeValueAsString(new Request(Request.ACTION_STOP_SERVER)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void fillPlayersTable(Map<Integer, PlayerFullInfo> _playersFullInfo) {
        playersFullInfo = _playersFullInfo;
        isStarted = true;
        lblStatus.setText("Running");
        updateTable();
    }

    public void stopped() {
        playersFullInfo.clear();
        updateTable();
        isStarted = false;
        lblStatus.setText("Stopped");
    }


    public void update(PlayerFullInfo playerFullInfo) {
        playersFullInfo.put(playerFullInfo.getDb_id(), playerFullInfo);
        updateTable();
    }

    public void offline(boolean isOffline) {
        tPlayers.setDisable(isOffline);
        btnStart.setDisable(isOffline);
        btnStop.setDisable(isOffline);
    }

    private void updateTable() {
        if (isStarted) {
            tPlayers.getItems().clear();
            tPlayers.getItems().setAll(playersFullInfo.values());
            tPlayers.getSortOrder().add(cStatus);
        }
    }
}