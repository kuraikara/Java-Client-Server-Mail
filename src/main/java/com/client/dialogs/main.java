package com.client.dialogs;

import com.client.Client;
import com.exchange.Email;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.stage.Stage;

public class main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Client c = new Client("cr");
        Button button = new Button("suca");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("suuuuca");
                Email email = new Email("sender", "receiver" , "object", "text", null);
                new EmailDialog.NewEmailDialog(c);
                new EmailDialog.DraftDialog(c,email);
                new EmailDialog.ReplyDialog(c,email);
                new EmailDialog.ReplyAllDialog(c,email);
                new EmailDialog.ForwardDialog(c,email);
            }
        });

        stage.setScene(new Scene(button));
        stage.show();
    }
}
