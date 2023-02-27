package com.server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerAppController implements Initializable {
    private Server model;

    public ServerAppController(Server model) {
        this.model = model;
    }

    @FXML
    Button startButton;

    @FXML
    Button closeButton;

    @FXML
    Label status;

    @FXML
    TableView table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        status.textProperty().bind(model.statusProperty());

        table.itemsProperty().bind(model.logsProperty());

        startButton.setOnAction(actionEvent -> model.start());

        closeButton.setOnAction(actionEvent -> model.close());

    }
}
