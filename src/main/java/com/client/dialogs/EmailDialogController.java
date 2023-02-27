package com.client.dialogs;

import com.client.Client;
import com.exchange.Email;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class EmailDialogController implements Initializable {
    private Email email;
    private boolean toEditable;
    private boolean objectEditable;
    private boolean textEditable;
    private Client model;

    public EmailDialogController(Email email, boolean toEditable, boolean objectEditable, boolean textEditable, Client model) {
        this.email = email;
        this.toEditable = toEditable;
        this.objectEditable = objectEditable;
        this.textEditable = textEditable;
        this.model = model;
    }

    @FXML
    TextField to;

    @FXML
    TextField object;

    @FXML
    TextArea text;

    @FXML
    Button send;

    @FXML
    Button saveDraft;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        to.setText(email.getReceivers());
        object.setText(email.getObject());
        text.setText(email.getText());
        to.setEditable(toEditable);
        object.setEditable(objectEditable);
        text.setEditable(textEditable);
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.sendEmail(new Email(model.emailAddressProperty().get(),to.getText(),object.getText(),text.getText(),new Date()));
                send.getScene().getWindow().hide();
            }
        });
        saveDraft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.sendEmail(new Email(model.emailAddressProperty().get(),to.getText(),object.getText(),text.getText(),null));
                saveDraft.getScene().getWindow().hide();
            }
        });
    }
}
