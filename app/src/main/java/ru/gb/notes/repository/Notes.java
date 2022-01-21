package ru.gb.notes.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

import ru.gb.notes.ui.MainActivity;

public class Notes implements Serializable, LifecycleObserver, Constants {
    private LinkedList<Note> notes;
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
        saveNotes();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void loadNotes() {
        String loadedString = MainActivity.getSharedPreferences().getString(PREFERENCES, "");
        if (!loadedString.isEmpty()) {
            notes = new Gson().fromJson(loadedString, new TypeToken<LinkedList<Note>>(){}.getType());
        }else {
            Random rnd = new Random();
            for (int i = 0; i < 10; i++)
                add("Заголовок " + i, "Текст заметки " + i, Calendar.getInstance(), images[rnd.nextInt(3)]);
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void saveNotes() {
        String notesToString = new Gson().toJson(notes);
        MainActivity.getSharedPreferences().edit().putString(PREFERENCES, notesToString).apply();
    }
}