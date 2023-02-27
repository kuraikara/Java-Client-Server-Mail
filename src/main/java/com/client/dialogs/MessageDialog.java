package com.client.dialogs;

import com.client.Client;
import com.client.ClientAppController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MessageDialog extends Application{
    private String message;

    public MessageDialog(String message) {
        this.message = message;
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("message.fxml"));
        fxmlLoader.setController(new MessageDialogController(message));
        stage.setTitle("Message");
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }
}
