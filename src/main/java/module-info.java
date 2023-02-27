module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.client to javafx.fxml;
    opens com.client.dialogs to javafx.fxml;
    opens com.server to javafx.fxml;
    exports com.client;
    exports com.server;
    exports com.exchange;
    exports com.client.dialogs;
}