package ru.gb.notes.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import ru.gb.notes.R;
import ru.gb.notes.databinding.ActivityMainBinding;
import ru.gb.notes.home.HomeFragment;
import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;
import ru.gb.notes.repository.Notes;

public class MainActivity extends AppCompatActivity implements LifecycleObserver, HomeFragment.NotesListController, NoteEditorDialogFragment.NoteEditorController, Constants {

    private static Notes notes;
    private HomeFragment homeFragment;
    private static MainActivity mainActivity;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        mainActivity = this;
        homeFragment = new HomeFragment();
        notes = Notes.getInstance();

        getLifecycle().addObserver(notes);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> {
            Note note = new Note(notes.getNewID(), "", "", Calendar.getInstance(), 0);
            startNoteEdit(note);
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_info)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mainActivity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Вызов настроек", Toast.LENGTH_LONG).show();
                break;
            case R.id.app_bar_search:
                Toast.makeText(this, "Поиск заметки", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void startNoteEdit(Note note) {
        NoteEditorDialogFragment noteEditorDialogFragment = new NoteEditorDialogFragment();
        noteEditorDialogFragment.setNote(note);
        noteEditorDialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void resultNoteEdit(EditResult result, Note note, NoteEditorDialogFragment noteEditorDialogFragment) {
        switch (result) {
            case RESULT_UPDATE:
                if (note != null) {
                    notes.addOrUpdateNote(note);
                    homeFragment
                            .getNotesListAdapter()
                            .notifyItemChanged(
                                    notes.getIndexByID(note.getId())
                            );
                }
                break;
            case RESULT_CANCEL:
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .remove(noteEditorDialogFragment)
                .commit();
    }

    boolean isCloseAccepted = false;

    @Override
    public void onBackPressed() {
        if (isCloseAccepted) {
            this.finish();
            return;
        }
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), R.string.main_activity_on_close_question, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.button_yes_text, view -> {
                isCloseAccepted = true;
                mainActivity.onBackPressed();
            });
            snackbar.show();
        }
        if (backStackEntryCount > 0)
            super.onBackPressed();
    }
}