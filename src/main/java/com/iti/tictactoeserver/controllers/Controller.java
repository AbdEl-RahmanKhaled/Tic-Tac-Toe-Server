package com.iti.tictactoeserver.controllers;

import com.iti.tictactoeserver.models.PlayerFullInfo;
import javafx.fxml.FXML;
import com.iti.tictactoeserver.helpers.server.ClientListener;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static ClientListener clientListener;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cPlayerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        cIsInGame.setCellValueFactory(new PropertyValueFactory<>("inGame"));
        cScore.setCellValueFactory(new PropertyValueFactory<>("points"));
        cStatus.setComparator(cStatus.getComparator().reversed());
    }

    @FXML
    protected void onActionStart() {
        clientListener = new ClientListener();
        clientListener.setDaemon(true);
        clientListener.start();
    }

    @FXML
    protected void onActionStop() {
        clientListener.interrupt();
        clientListener = null;
    }

    public void fillPlayersTable(Collection<PlayerFullInfo> playersFullInfo) {
        tPlayers.getItems().clear();
        tPlayers.getItems().setAll(playersFullInfo);
        tPlayers.getSortOrder().add(cStatus);
    }


}