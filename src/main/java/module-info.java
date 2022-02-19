module com.iti.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires org.json;
    requires com.fasterxml.jackson.databind;


    opens com.iti.tictactoeserver to javafx.fxml;
    exports com.iti.tictactoeserver;
    exports com.iti.tictactoeserver.controllers;
    opens com.iti.tictactoeserver.controllers to javafx.fxml;
    exports com.iti.tictactoeserver.requests to com.fasterxml.jackson.databind;
    exports com.iti.tictactoeserver.responses to com.fasterxml.jackson.databind;
    exports com.iti.tictactoeserver.models to com.fasterxml.jackson.databind;
    exports com.iti.tictactoeserver.notification to com.fasterxml.jackson.databind;

}