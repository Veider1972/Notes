package ru.gb.notes.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;
import ru.gb.notes.R;

public class NoteEditorDialogFragment extends DialogFragment implements Constants {

    private TextInputEditText titleTextInputEditText;
    private TextInputEditText descriptionTextInputEditText;
    private TextView dateTextView;

    private static Note note;

    public void setNote(Note note) {
        NoteEditorDialogFragment.note = note;
    }

    private NoteEditorController noteEditorController;

    public interface NoteEditorController {
        void resultNoteEdit(Constants.EditResult result, Note note, NoteEditorDialogFragment noteEditorDialogFragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof NoteEditorController)
            this.noteEditorController = (NoteEditorController) context;
        else
            throw new IllegalStateException("Activity must implement NoteEditorController");
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        View dialogView = getLayoutInflater().inflate(R.layout.note_editor_fragment, null);
        titleTextInputEditText = dialogView.findViewById(R.id.title_text_editor);
        descriptionTextInputEditText = dialogView.findViewById(R.id.description_text_editor);
        dateTextView = dialogView.findViewById(R.id.date);
        dateTextView.setOnClickListener(view -> {
            new DatePickerDialog(this.getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                note.setDate(date);
                dateTextView.setText(new SimpleDateFormat("d.MM.yy", Locale.getDefault()).format(note.getDate().getTime()));
            }, note.getDate().get(Calendar.YEAR), note.getDate().get(Calendar.MONTH), note.getDate().get(Calendar.DAY_OF_MONTH)).show();
        });
        Spinner spinner = dialogView.findViewById(R.id.spinner_image);
        assert this.getContext() != null;
        SpinnerAdapter adapter = new SpinnerAdapter(this.getContext());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                note.setPicture(images[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (savedInstanceState == null) {
            titleTextInputEditText.setText(note.getTitle());
            descriptionTextInputEditText.setText(note.getDescription());
            dateTextView.setText(new SimpleDateFormat("d.MM.yy", Locale.getDefault()).format(note.getDate().getTime()));
            spinner.setSelection(getImageById(note.getPicture()));
        }
        dialog.setView(dialogView);
        dialog.setPositiveButton(R.string.button_accept_text, (dialogInterface, i) -> {
            assert titleTextInputEditText.getText() != null;
            assert descriptionTextInputEditText.getText() != null;
            if (titleTextInputEditText.getText().toString().isEmpty() && descriptionTextInputEditText.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(getContext(), R.string.editor_empty_close_message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else
                new AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.title_attention)
                        .setMessage(R.string.alert_accept)
                        .setIcon(R.drawable.attention)
                        .setNegativeButton(R.string.button_no_text, null)
                        .setPositiveButton(R.string.button_yes_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateNote();
                            }
                        })
                        .show();
        });
        dialog.setNegativeButton(R.string.button_cancel_text, (dialogInterface, i) -> {
            noteEditorController.resultNoteEdit(Constants.EditResult.RESULT_CANCEL, note, this);
            noteEditorController = null;
        });
        dialog.setCancelable(false);
        return dialog.show();
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.setCanceledOnTouchOutside(false);
    }

    public void updateNote() {
        assert titleTextInputEditText.getText() != null;
        note.setTitle(titleTextInputEditText.getText().toString());
        assert descriptionTextInputEditText.getText() != null;
        note.setDescription(descriptionTextInputEditText.getText().toString());
        noteEditorController.resultNoteEdit(EditResult.RESULT_UPDATE, note, this);
        noteEditorController = null;
    }

    static class SpinnerAdapter extends BaseAdapter implements Constants {
        private final LayoutInflater layoutInflater;

        public SpinnerAdapter(@NonNull Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.row_image, null);
            ImageView imageView = view.findViewById(R.id.image_icon);
            imageView.setImageResource(images[position]);
            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }
}
