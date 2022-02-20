package com.iti.tictactoeserver.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompetitorConnectionIssueNotification extends Notification {
    public CompetitorConnectionIssueNotification() {
        this.type = NOTIFICATION_COMPETITOR_CONNECTION_ISSUE;
    }

    public CompetitorConnectionIssueNotification(@JsonProperty("type") String type) {
        super(type);
    }
}
