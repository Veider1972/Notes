package ru.gb.notes.repository;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;

public class Notes implements Serializable {
    private final LinkedList<Note> notes;
    private static Notes instance;

    public static Notes getInstance() {
        if (instance == null)
            instance = new Notes();
        return instance;
    }

    public Notes() {
        notes = new LinkedList<>();
    }

    public int getSize() {
        return notes.size();
    }

    public void add(@NonNull String title, String description, Calendar calendar, int picture) {
        int id;
        if (notes.size() == 0) id = 0;
        else id = notes.getLast().getId() + 1;
        notes.add(new Note(id, title, description, calendar, picture));
    }

    public boolean delete(int id) {
        int index = getIndexByID(id);
        if (index != -1) {
            notes.remove(index);
            return true;
        }
        return false;
    }

    public int getNewID() {
        return notes.getLast().getId() + 1;
    }

    public int getIndexByID(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id)
                return i;
        }
        return -1;
    }

    public Note getNote(int i) {
        return notes.get(i);
    }

    public void addOrUpdateNote(Note note) {
        int index = getIndexByID(note.getId());
        if (index == -1) {
            notes.add(note);
        } else notes.set(index, note);
    }
}