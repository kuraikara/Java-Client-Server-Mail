package com.server;

import com.exchange.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ClientHandler implements Runnable{
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private List<User> users;
    private User user;
    private ObservableList<Log> logs;
    private List<Email> newEmails;

    public ClientHandler(Socket socket, List<User> users, ObservableList<Log> logs ) {
        this.socket = socket;
        this.users = users;
        this.logs = logs;
        this.newEmails = new ArrayList<>();
    }

    private void createStreams() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("[CH]Nuovo Client handler operativo");
        createStreams();
        handleMessage();
    }


    //COMUNICAZIONE CON IL CLIENT
    private void handleMessage() {
            Message newMessage = null;
            if ((newMessage = receive()) != null){
                System.out.println("[CH]Ricevuto messaggio dal client: " + newMessage);
                switch (newMessage.getType()){
                    case SyncEmails:
                        provideEmails(newMessage.getUser(),(int)newMessage.getExtra());
                        break;
                    case SendEmail:
                        sendEmail(((Email)newMessage.getExtra()));
                        break;
                    case RemoveEmail:
                        break;
                    default:
                        System.out.println("Ho ricevuto un messaggio anomalo");
                }
            }
    }

    private Message receive() {
        try {
            System.err.println("[CH]Sono in ascolto");
            Object msg = inputStream.readObject();
            return (Message)msg;
        } catch (IOException e) {
            System.err.println("[CH]Errore io ricezione");
            //TODO handlare cla chiusura del client
            close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("Errore chiusura socket");
        }
    }

    public void send(Message msg){
        System.out.println("[CH]Invio un messaggio: " + msg);
        try {
            outputStream.writeObject(msg);
            outputStream.flush();
        }catch(IOException ioe){
            System.err.println("[CH]Errore nell'invio del messaggio!");
        }
    }


    //TIPI DI RISPOSTA AL CLIENT
    private List<User> parseReceivers(String receivers){
        List<User> parsedReceivers = new ArrayList<>();
        String[] splittedReceivers = receivers.split(",");
        for (String r: splittedReceivers) {
            parsedReceivers.add(new User(r));
        }
        return parsedReceivers;
    }

    private void sendEmail(Email email) {
        List<User> toSend = parseReceivers(email.getReceivers());
        List<Integer> indexes = new ArrayList<>();
        email.setNewId();
        if (email.getDate() != null){
            for (User receiver: toSend){
                Integer i;
                if ((i = users.indexOf(receiver)) == -1){
                    System.out.println("AGGIUNTA EMAIL non esiste un receiver");
                    email.setDate(null);
                    user.addEmail(email);
                    send(new Message(Message.MessageType.Message, "Impossibile inviare l'email: " + receiver.getName() + " non esiste!"));
                    return;
                }
                indexes.add(i);
            }
            logs.add(0,new Log(user.getName(), "Sended email [id: " + email.getId() + "] to " + email.getReceivers(), new Date()));
            for (Integer index: indexes) {
                users.get(index).addEmail(email);
            }
        }else{
            System.out.println("INVIATA DRAFT");
        }
        user.removeEmailById(email.getId());
        user.addEmail(email);
        // TODO send(new Message(Message.MessageType.EmailSended));
    }


    private void provideEmails(String user, int lastEmailId) {
        System.out.println("Invio email al client dopo id: " + lastEmailId);
        File userFile = new File("./data/usersdata/"+user);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            String line;
            Boolean found = false;
            List<Email> toSend = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (found){
                    //toSend.add()
                }
                if (!found && (int)line.charAt(0) == lastEmailId){
                    found = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Email parseEmail(String string){
        String[] emailInfo = string.split(";");
        return new Email(emailInfo[0],emailInfo[1],emailInfo[2],emailInfo[3],new Date(Long.parseLong(emailInfo[4])));
    }
}
