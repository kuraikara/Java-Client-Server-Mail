package com.server;

import com.exchange.Email;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class                    Server {
    private static final int MAX_TO_HANDLE = 5;

    private ServerSocket socket;
    private ExecutorService executorService;
    private List<User> users;
    private StringProperty status;
    private ListProperty<Log> logs;
    private ObservableList<Log> logsContent;

    public Server() {
        users = new ArrayList<>();
        status = new SimpleStringProperty("Offline");
        logs = new SimpleListProperty<>();
        logsContent = FXCollections.observableList(new ArrayList<>());
        logs.set(logsContent);
        //loadLogs();
        loadUsers();
    }

    public ListProperty logsProperty(){return logs;}

    public StringProperty statusProperty() {return status;}

    private void startClientListener() {
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    System.out.println("[S]Server in ascolto...");
                    try {
                        Socket newClientSocket = socket.accept();
                        logsContent.add(0,new Log( "Nuovo client connesso", new Date()));
                        executorService.execute(new ClientHandler(newClientSocket, users, logsContent));
                    }catch (SocketException se){
                        System.err.println("Socket chiuso");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void start(){
        try {
            loadUsers();
            socket = new ServerSocket(4445);
            executorService = Executors.newFixedThreadPool(MAX_TO_HANDLE);
            status.set("Online");
            startClientListener( );
            logsContent.add(0,new Log( "Server aperto e in ascolto", new Date()));
        } catch (IOException e) {
            System.err.println("[S]Errore nell'apertura del server socket");
        }
    }

    public void close(){
        System.out.println("Chiudo il server");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Errore chiusura socket");
        }
        logsContent.add(0,new Log( "Server chiuso", new Date()));
        writeLogs();
    }

    private void loadLogs() {
        File file = new File("src/main/java/com/server/data/logs");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] parsed = line.split(";");
                logsContent.add(new Log(parsed[0], parsed[1], parsed[2]));
            }

            System.out.println("Logs caricati");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLogs(){
        System.out.println("scrivo logs");
        File file = new File("src/main/java/com/server/data/logs");
        try {
            BufferedWriter writer = new BufferedWriter(new PrintWriter(file));
            for (Log log: logsContent) {
                writer.write(log.getAbout()+";"+log.getDescription()+";"+log.getDate()+"\n");
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        File usersDirectory = new File("./data/usersdata");

            String[] usersFiles = usersDirectory.list();
            for (int i = 0; i< usersFiles.length; i++){
                File userFile = new File(usersDirectory.getAbsolutePath(), usersFiles[i]);
                addUser(userFile);
            }
    }

    private void addUser(File userFile){
        User user = new User(userFile.getName(),userFile);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] emailInfo = line.split(";");
                user.addEmail(new Email(emailInfo[0],emailInfo[1],emailInfo[2],emailInfo[3],new Date(Long.parseLong(emailInfo[4]))));
            }
            System.out.println(user);
            users.add(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File file = new File("src/main/java/com/server/data/nuovofile.txt");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
