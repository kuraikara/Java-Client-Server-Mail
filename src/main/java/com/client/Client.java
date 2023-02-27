package com.client;

import com.exchange.*;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.exchange.Message.MessageType.*;

public class Client {

    private StringProperty emailAddres;

    //Comunicazione col server
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    //variabili sessione
    String sessionHost;
    int sessionPort;
    int lastEmailId;

    //properties

    ListProperty<Email> sended;
    ObservableList<Email> sendedContent;
    ListProperty<Email> received;
    ObservableList<Email> receivedContent;
    ListProperty<Email> drafts;
    ObservableList<Email> draftsContent;
    StringProperty status;
    StringProperty message;


    public Client(String emailAddres) {
        setProperties(emailAddres);
        //syncEmails();
    }

    private void setProperties(String username) {
        sendedContent = FXCollections.observableList(new ArrayList<>());
        receivedContent = FXCollections.observableList(new ArrayList<>());
        draftsContent = FXCollections.observableList(new ArrayList<>());
        sended = new SimpleListProperty<>();
        received = new SimpleListProperty<>();
        drafts = new SimpleListProperty<>();
        emailAddres = new SimpleStringProperty(username);
        status = new SimpleStringProperty("Offline");
        message = new SimpleStringProperty();
        sended.set(sendedContent);
        received.set(receivedContent);
        drafts.set(draftsContent);
    }

    public ListProperty<Email> sendedProperty(){return sended;}
    public ListProperty<Email> receivedProperty(){return received;}
    public ListProperty<Email> draftsProperty(){return drafts;}
    public StringProperty emailAddressProperty() {
        return emailAddres;
    }
    public StringProperty statusProperty() {
        return status;
    }
    public StringProperty messageProperty(){return message;}

    //METODI DI COMUNICAZIONE COL SERVER
    public boolean connect(String host, int port){
        int attempts = 0;
        boolean success = false;

        while(attempts < 5 && !success) {
            attempts += 1;
            System.out.println("[C]Cerco di connettermi a "+ host +" - tentativo " + attempts);
            try {
                socket = new Socket(host, port);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("[C]Connesso");
                success = true;
            }catch(UnknownHostException ue){
                System.err.println("[C]Imposibile connettersi all'host specificato!");
            }catch(IOException ioe){
                System.err.println("[C]Errore di IO nell connessione all'host!");
            }

            if(success) {
                syncEmails();
                sessionHost = host;
                sessionPort = port;
                status.set("Online");
                return true;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    private void addNewEmails(List<Email> emails){
        lastEmailId = emails.get(emails.size() - 1).getId();
        System.out.println("Ultima email ricevuta: " + lastEmailId);
        for (Email email: emails) {
            if (email.getSender().equals(emailAddressProperty().get())) {
                if (email.getDate() == null) draftsContent.add(email);
                else sendedContent.add(email);
            } else {
                receivedContent.add(email);
            }
        }
    }

    public void closeSocket(){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("[C]Sono disconnesso dal server");
        } catch (IOException ex) {
            System.err.println("[C]Errore chiusura socket");
        }
    }



    public static void main(String[] args) {
        Client c = new Client("ciccio");
        c.connect("127.0.0.1", 4445);
    }




    //-------------------------------------

    public void sendEmail(Email email){
        sendMessageToServer(new Message(SendEmail,emailAddres.get(), email));
    }
    public void syncEmails(){
        sendMessageToServer(new Message(SyncEmails, emailAddres.get(), lastEmailId));
    }
    public void removeEmail(Email email){
        sendMessageToServer(new Message(RemoveEmail, emailAddres.get(), email.getId()));
    }


    private void sendMessageToServer(Message message){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
                receiveMessage();
            }
        });
        thread.start();
    }

    public void send(Message msg){
        if (connect("127.0.0.1", 4445)){
            System.out.println("[C]Invio un messaggio: " + msg);
            try {
                outputStream.writeObject(msg);
                outputStream.flush();
            }catch(IOException ioe){
                System.err.println("[C]Errore nell'invio del messaggio!");
            }
        }else{
            //TODO finesta messaggio impossibile comunicare col server
        }
    }

    private void receiveMessage() {
        Message newMessage = receive();
        System.out.println("[C]Ricevuto nuovo messaggio: " + newMessage);
        switch (newMessage.getType()){
            case SyncEmails:
                Platform.runLater(() -> {
                    addNewEmails(((List<Email>) newMessage.getExtra()));
                });
                closeSocket();
                break;
            case Message:
                Platform.runLater(() -> {
                    message.set((String) newMessage.getExtra());
                });
                syncEmails();
                break;
            default:
                System.out.println("Ho ricevuto un messaggio anomalo");
        }

    }
    private Message receive() {
        try {
            Object msg = inputStream.readObject();
            return (Message) msg;
        } catch (IOException e) {
            System.err.println("[C]Errore io ricezione");
            status.set("Offline");
            //TODO handlare disconnessione server
            closeSocket();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
