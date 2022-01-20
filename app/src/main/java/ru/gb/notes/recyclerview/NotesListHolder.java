package ru.gb.notes.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;
import ru.gb.notes.R;

public class NotesListHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;
    private final TextView descriptionView;
    private final ImageView imageView;
    private final TextView dateView;
    private final ImageView popupMenuImageView;
    private Note note;

    public NotesListHolder(@NonNull View itemView, NotesListAdapter.OnNoteClickListener onNoteClickListener) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        descriptionView = itemView.findViewById(R.id.description);
        imageView = itemView.findViewById(R.id.image);
        dateView = itemView.findViewById(R.id.date);
        popupMenuImageView = itemView.findViewById(R.id.popup_menu);
        popupMenuImageView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), popupMenuImageView);
            popupMenu.inflate(R.menu.note_popup_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.change_note:
                        if (onNoteClickListener != null)
                            onNoteClickListener.onNoteClick(note, Constants.EditResult.RESULT_UPDATE);
                        return true;
                    case R.id.delete_note:
                        if (onNoteClickListener != null)
                            onNoteClickListener.onNoteClick(note, Constants.EditResult.RESULT_DELETE);
                        return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    void bind(Note note) {
        this.note = note;
        titleView.setText(note.getTitle());
        descriptionView.setText(note.getDescription());
        imageView.setImageResource(note.getPicture());
        dateView.setText(new SimpleDateFormat("d.MM.yy", Locale.getDefault()).format(note.getDate().getTime()));
    }
}