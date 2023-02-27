package com.client;


import com.client.dialogs.EmailDialog.*;
import com.client.dialogs.MessageDialog;
import com.exchange.Email;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ClientAppController implements Initializable {
    private Client model;
    private Email selectedEmail;

    public ClientAppController(Client model) {
        this.model = model;
    }

    @FXML
    ListView inviate;

    @FXML
    ListView ricevute;

    @FXML
    ListView drafts;

    @FXML
    Label username;

    @FXML
    Label status;

    @FXML
    Accordion accordion;

    @FXML
    Label oggetto;

    @FXML
    Label from;

    @FXML
    Label to;

    @FXML
    Label date;

    @FXML
    TextFlow text;

    @FXML
    Button newbutt;

    @FXML
    Button reloadbutt;

    @FXML
    Button replybutt;

    @FXML
    Button replyallbutt;

    @FXML
    Button forwardbutt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setEmailCell(inviate);
        setEmailCell(ricevute);
        setEmailCell(drafts);

        username.textProperty().bind(model.emailAddressProperty());
        status.textProperty().bind(model.statusProperty());
        model.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                new MessageDialog(t1);
            }
        });

        inviate.itemsProperty().bind(model.sendedProperty());
        ricevute.itemsProperty().bind(model.receivedProperty());
        drafts.itemsProperty().bind(model.draftsProperty());

        startSelectionCleaner();

        addSelectionListener(inviate);
        addSelectionListener(ricevute);
        addDraftSelectionListener();

        newbutt.setOnAction(actionEvent ->  new NewEmailDialog(model));
        reloadbutt.setOnAction(actionEvent -> model.syncEmails());
        replybutt.setOnAction(actionEvent -> {if(selectedEmail != null)new ReplyDialog(model,selectedEmail);});
        replyallbutt.setOnAction(actionEvent -> {if(selectedEmail != null)new ReplyAllDialog(model,selectedEmail);});
        forwardbutt.setOnAction(actionEvent -> {if(selectedEmail != null)new ForwardDialog(model,selectedEmail);});
    }

    private void setEmailCell(ListView list){
        list.setCellFactory(new Callback<ListView<Email>, ListCell<Email>>() {
            @Override
            public ListCell<Email> call(ListView<Email> listView) {
                return new EmailCell();
            }
        });
    }

    static class EmailCell extends ListCell<Email>{
        Label label = new Label();

        public void updateItem(Email item, boolean empty){
            super.updateItem(item,empty);
            if (item != null) {
                label.setText(item.getObject());
                setGraphic(label);
            }
        }
    }

    private void startSelectionCleaner() {
        accordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> observableValue, TitledPane titledPane, TitledPane t1) {
                if (accordion.getExpandedPane() != null) {
                    ScrollPane pane = (ScrollPane) accordion.getExpandedPane().getContent();
                    ListView list = (ListView) pane.getContent();
                    list.getSelectionModel().clearSelection();
                }
            }
        });
    }

    private void addDraftSelectionListener() {
        drafts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                Email email = (Email)drafts.getSelectionModel().getSelectedItem();
                if (email != null) new DraftDialog(model,email);
            }
        });
    }

    private void addSelectionListener(ListView list) {
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                selectedEmail = (Email)list.getSelectionModel().getSelectedItem();
                if (selectedEmail != null) {
                    System.out.println("Visualizzo email " + selectedEmail);
                    oggetto.setText(selectedEmail.getObject());
                    from.setText(selectedEmail.getSender());
                    to.setText(selectedEmail.getReceivers());
                    date.setText(selectedEmail.getDate().toString());
                    text.getChildren().clear();
                    text.getChildren().add(new Text(selectedEmail.getText()));
                }
            }
        });
    }



}


