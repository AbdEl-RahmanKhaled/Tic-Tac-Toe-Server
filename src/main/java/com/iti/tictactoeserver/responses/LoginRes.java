package com.iti.tictactoeserver.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iti.tictactoeserver.models.Player;
import com.iti.tictactoeserver.models.PlayerFullInfo;

import java.util.Map;

public class LoginRes extends Response{
    private PlayerFullInfo playerFullInfo;
    private Map<Integer,PlayerFullInfo> playerFullInfoMap;

    public LoginRes(){
        this.type = RESPONSE_LOGIN;
    }

    public LoginRes(String status) {
        this.status = status;
    }



    public LoginRes(String status, PlayerFullInfo playerFullInfo, Map<Integer,PlayerFullInfo> playerFullInfoMap) {
        this.playerFullInfo = playerFullInfo;
        this.status = status;
        this.type = RESPONSE_LOGIN;
        this.playerFullInfoMap=playerFullInfoMap;
    }


    public LoginRes(String status, String message, PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
        this.status = status;
        this.message = message;
        this.type = RESPONSE_LOGIN;

    }

    public LoginRes(@JsonProperty("status") String status,
                           @JsonProperty("type") String type,
                           @JsonProperty("message") String message,
                           @JsonProperty("playerFullInfo") PlayerFullInfo playerFullInfo,
                           @JsonProperty("playerFullInfoMap") Map<Integer,PlayerFullInfo> playerFullInfoMap) {
        this.status = status;
        this.type = type;
        this.message = message;
        this.playerFullInfo = playerFullInfo;
        this.playerFullInfoMap = playerFullInfoMap;

    }

    public PlayerFullInfo getPlayerFullInfo() {
        return playerFullInfo;
    }

    public void setPlayerFullInfo(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
    }

    public Map<Integer, PlayerFullInfo> getPlayerFullInfoMap() {
        return playerFullInfoMap;
    }

    public void setPlayerFullInfoMap(Map<Integer, PlayerFullInfo> playerFullInfoMap) {
        this.playerFullInfoMap = playerFullInfoMap;
    }
}
