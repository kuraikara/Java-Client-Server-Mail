package com.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private String username;

    public ClientApp(String username) {
        this.username = username;
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("ClientUI.fxml"));
        fxmlLoader.setController(new ClientAppController(new Client(username)));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        stage.setTitle("BadMails client");
        stage.setScene(scene);
        stage.show();
    }
}
