package ru.gb.notes.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.gb.notes.R;
import ru.gb.notes.databinding.FragmentHomeBinding;
import ru.gb.notes.recyclerview.NoteListItemDecoration;
import ru.gb.notes.recyclerview.NotesListAdapter;
import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;
import ru.gb.notes.repository.Notes;

public class HomeFragment extends Fragment implements NotesListAdapter.OnNoteClickListener, Constants {

    private final Notes notes = Notes.getInstance();

    private static NotesListAdapter notesListAdapter;

    public NotesListAdapter getNotesListAdapter() {
        return notesListAdapter;
    }

    private NotesListController notesListController;



    public interface NotesListController {
        void startNoteEdit(Note note);
    }

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof NotesListController)
            this.notesListController = (NotesListController) context;
        else
            throw new IllegalStateException("Activity must implement NotesListController");
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notesListAdapter = new NotesListAdapter();
        notesListAdapter.setNotes(notes);
        notesListAdapter.setOnNoteClickListener(this);
        RecyclerView notesViewer = root.findViewById(R.id.notes_list);
        notesViewer.addItemDecoration(new NoteListItemDecoration(ITEMS_SPACE));
        notesViewer.setLayoutManager(new LinearLayoutManager(getContext()));
        notesViewer.setAdapter(notesListAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onNoteClick(Note note, EditResult result) {
        switch (result) {
            case RESULT_UPDATE:
                notesListController.startNoteEdit(note);
                break;
            case RESULT_DELETE:
                int index = notes.getIndexByID(note.getId());
                notes.delete(note.getId());
                notesListAdapter.notifyItemRemoved(index);
                break;
        }
    }
}