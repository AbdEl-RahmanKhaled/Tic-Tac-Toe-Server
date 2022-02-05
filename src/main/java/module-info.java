module com.iti.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.iti.tictactoeserver to javafx.fxml;
    exports com.iti.tictactoeserver;
}