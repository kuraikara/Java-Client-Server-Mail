package com.exchange;
import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private String user;
    private Object extra;

    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, String user, Object extra) {
        this.type = type;
        this.user = user;
        this.extra = extra;
    }

    public Message(MessageType type, Object extra) {
        this.type = type;
        this.extra = extra;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", extra=" + extra +
                '}';
    }

    public enum MessageType{
        SyncEmails,
        SendEmail,
        EmailSended,
        RemoveEmail,
        EmailRemoved,
        Message
    }
}