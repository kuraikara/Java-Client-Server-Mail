package com.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ServerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("serverUI.fxml"));
        fxmlLoader.setController(new ServerAppController(new Server()));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        stage.setTitle("BadMails Server");
        stage.setScene(scene);
        stage.show();
    }
}
