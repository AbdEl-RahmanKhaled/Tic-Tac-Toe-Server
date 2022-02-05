module com.iti.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.iti.tictactoeserver to javafx.fxml;
    exports com.iti.tictactoeserver;
}