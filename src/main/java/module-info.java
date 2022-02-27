/**
 *
 */
module com.iti.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires org.json;
    requires com.fasterxml.jackson.databind;


    opens com.iti.tictactoeserver ;
    exports com.iti.tictactoeserver;
    exports com.iti.tictactoeserver.controllers;
    opens com.iti.tictactoeserver.controllers to javafx.fxml;
    opens com.iti.tictactoeserver.models;
    exports com.iti.tictactoeserver.notification;
    exports com.iti.tictactoeserver.requests;
    exports com.iti.tictactoeserver.responses;
    exports com.iti.tictactoeserver.models;

}