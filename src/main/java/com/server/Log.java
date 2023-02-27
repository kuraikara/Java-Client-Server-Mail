package com.server;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Log {
    private StringProperty about;
    private StringProperty description;
    private StringProperty date;

    public Log(String about, String description, Date date) {
        this.about = new SimpleStringProperty(about);
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleStringProperty(date.toString());
    }

    public Log(String about, String description, String date) {
        this.about = new SimpleStringProperty(about);
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleStringProperty(date);
    }

    public Log(String description, Date date) {
        this.about = new SimpleStringProperty("SERVER");
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleStringProperty(date.toString());
    }

    public String getAbout() {
        return about.get();
    }

    public StringProperty aboutProperty() {
        return about;
    }

    public void setAbout(String about) {
        this.about.set(about);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    @Override
    public String toString() {
        return "Log{" +
                "about=" + about +
                ", description=" + description +
                ", date=" + date +
                '}';
    }
}
