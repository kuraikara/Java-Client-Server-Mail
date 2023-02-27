package com.client.dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageDialogController implements Initializable {
    private String message;

    public MessageDialogController(String message) {
        this.message = message;
    }

    @FXML
    Button okbutt;

    @FXML
    Label msg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        msg.setText(message);
        okbutt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                okbutt.getScene().getWindow().hide();
            }
        });
    }
}
