package com.exchange;

import java.io.Serializable;
import java.util.Date;

public class Email implements Comparable<Email> , Serializable {
    private static int count = 0;
    private int id;
    private String sender;
    private String receivers;
    private String object;
    private String text;
    private Date date;

    public Email(String sender, String receivers, String object, String text, Date date) {
        this.sender = sender;
        this.receivers = receivers;
        this.object = object;
        this.text = text;
        this.date = date;
    }

    public Email() {
        this.sender = "";
        this.receivers = "";
        this.object = "";
        this.text = "";
        this.date = null;
    }

    public Email(int id,String sender, String receivers, String object, String text, Date date) {
        this.id = id;
        if (count < id) count = id;
        this.sender = sender;
        this.receivers = receivers;
        this.object = object;
        this.text = text;
        this.date = date;
    }

    public void setNewId(){
        this.id = ++count;
        System.out.println("nuovo id creato: " + count);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receivers=" + receivers +
                ", object='" + object + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(Email e) {
        return this.getDate().compareTo(e.getDate());
    }
}
