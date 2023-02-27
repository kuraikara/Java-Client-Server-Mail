package com.client.dialogs;

import com.client.Client;
import com.exchange.Email;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmailDialog extends Application{

    private String title;
    private Client model;
    private Email email;
    private boolean toEditable;
    private boolean objectEditable;
    private boolean textEditable;

    public EmailDialog(String title,Client model, Email email, boolean toEditable, boolean objectEditable, boolean textEditable) {
        this.title = title;
        this.model = model;
        this.email = email;
        this.toEditable = toEditable;
        this.objectEditable = objectEditable;
        this.textEditable = textEditable;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("provanuovaemail.fxml"));
        fxmlLoader.setController(new EmailDialogController(email,toEditable,objectEditable,textEditable,model));
        stage.setTitle(title);
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static class NewEmailDialog extends EmailDialog{
        public NewEmailDialog(Client model) {
            super("New Email", model,new Email(),true,true,true);
            try {
                super.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ReplyDialog extends EmailDialog{
        public ReplyDialog(Client model,Email email) {
            super("Reply",model,new Email(null, email.getSender(), "","",null),false,true,true);
            try {
                super.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ReplyAllDialog extends EmailDialog{
        public ReplyAllDialog(Client model, Email email) {
            super("Reply All",model,new Email(null, email.getSender() + ";" + email.getReceivers(), "","",null),false,true,true);
            try {
                super.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class ForwardDialog extends EmailDialog{
        public ForwardDialog(Client model, Email email) {
            super("Forward",model,new Email(null, "", email.getObject(), email.getText(), null),true,false,false);
            try {
                super.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class DraftDialog extends EmailDialog{
        public DraftDialog(Client model, Email email) {
            super("Draft",model,new Email(null, email.getReceivers(), email.getObject(), email.getText(), null),true,true,true);
            try {
                super.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
