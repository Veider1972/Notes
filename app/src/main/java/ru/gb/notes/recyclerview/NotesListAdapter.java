package ru.gb.notes.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;
import ru.gb.notes.repository.Notes;
import ru.gb.notes.R;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListHolder> {

    private Notes notes;
    private OnNoteClickListener onNoteClickListener;

    public NotesListAdapter() {
        this.notes = Notes.getInstance();
    }

    @NonNull
    @Override
    public NotesListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note, parent, false);
        return new NotesListHolder(view, onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListHolder holder, int position) {
        holder.bind(notes.getNote(position));
    }

    @Override
    public int getItemCount() {
        return notes.getSize();
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
        notifyItemRangeChanged(0, notes.getSize());
        //notifyDataSetChanged();
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note, Constants.EditResult result);
    }
}