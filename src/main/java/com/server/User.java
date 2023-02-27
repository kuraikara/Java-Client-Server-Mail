package com.server;

import com.exchange.Email;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class User implements Comparator<User> {
    private String name;
    public File file;
    private List<Email> emails;


    public User(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public synchronized void addEmail(Email email){
        this.emails.add(email);
        try {
            FileWriter writer = new FileWriter(file, true);
            String string = email.getSender() + ";" + email.getReceivers() + ";" + email.getObject() + ";" + email.getText() + ";" + email.getDate().getTime();
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<Email> getEmails() {
        return emails;
    }


    @Override
    public String toString() {
        String out = "User{" +
                "name='" + name + '\'' +
                ", emails=" + '\n';

        for (Email e:emails
        ) {
            out += e.toString() + '\n';
        }

        return out;
    }

    @Override
    public int compare(User o1, User o2) {
        return o1.getName().compareTo(o2.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()){
            System.out.println("COMPARO DUE UTENTI");
            return ((User) obj).getName().equals(this.getName());
        }else if (obj.getClass() == String.class){
            System.out.println("COMPARO UTENTE E STRINGA");
            return ((String) obj).equals(this.getName());
        }
        return false;
    }

    public boolean equals(User u , String s){
        return s.equals(u.getName());
    }


    //todo remove from file
    public synchronized void removeEmailById(int id) {
        for (Email e: emails) {
            if (e.getId()==id)emails.remove(e);
        }
    }

    public synchronized void removeEmail(Email email) {
        emails.remove(email);
    }
}
