package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    protected String type;

    public static final String NOTIFICATION_GAME_INVITATION = "gameInvitationNotification";
    public static final String NOTIFICATION_UPDATE_STATUS = "updateStatusNotification";
    public static final String NOTIFICATION_START_GAME = "startGameNotification";
    public static final String NOTIFICATION_UPDATE_BOARD = "updateBoardNotification";
    public static final String NOTIFICATION_COMPETITOR_CONNECTION_ISSUE = "competitorConnectionIssueNotification";
    public static final String NOTIFICATION_ASK_TO_PAUSE = "askToPauseNotification";
    public static final String NOTIFICATION_MESSAGE = "messageNotification";
    public static final String NOTIFICATION_FINISH_GAME = "finishGameNotification";
    public static final String NOTIFICATION_ASK_TO_RESUME = "askToResumeNotification";
    public static final String NOTIFICATION_RESUME_GAME = "resumeGameNotification";
    public static final String NOTIFICATION_PAUSE_GAME = "pauseGameNotification";

    // Dashboard notification
    public static final String NOTIFICATION_SERVER_STARTED = "serverStartedNotification";
    public static final String NOTIFICATION_SERVER_STOPPED = "serverStoppedNotification";





    public Notification() {
    }

    public Notification(@JsonProperty("type") String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
