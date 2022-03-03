module com.iti.serverdashbaord {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires org.json;
    requires com.fasterxml.jackson.databind;


    opens com.iti.serverdashbaord ;
    exports com.iti.serverdashbaord;
    exports com.iti.serverdashbaord.controllers;
    opens com.iti.serverdashbaord.models;
    exports com.iti.serverdashbaord.notification;
    exports com.iti.serverdashbaord.requests;
    exports com.iti.serverdashbaord.models;
    opens com.iti.serverdashbaord.controllers;
}