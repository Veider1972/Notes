package ru.gb.notes.repository;

import java.io.Serializable;
import java.util.Calendar;

public class Note implements Serializable {
    private String title;
    private String description;
    private Calendar date;
    private int picture;
    private final int id;

    public int getId() {
        return id;
    }

    public Note(int id, String title, String description, Calendar date, int picture) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.picture = picture;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}