package com.example.offlinecodingnotesapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinecodingnotesapp.R;
import com.example.offlinecodingnotesapp.database.AppDatabase;
import com.example.offlinecodingnotesapp.database.NoteEntity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    EditText etTitle, etContent, etLanguage;
    TextView tvLangLabel, tvHeaderTitle;
    View vLangDot;
    MaterialButton btnSave;
    ImageButton btnBack, btnDelete;
    AppDatabase db;

    int noteId = -1;
    NoteEntity existingNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etLanguage = findViewById(R.id.etLanguage);
        tvLangLabel = findViewById(R.id.tvLangLabel);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        vLangDot = findViewById(R.id.vLangDot);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        db = AppDatabase.getInstance(this);

        // 🔥 EDIT MODE
        if (getIntent() != null && getIntent().hasExtra("noteId")) {
            noteId = getIntent().getIntExtra("noteId", -1);

            new Thread(() -> {
                existingNote = db.noteDao().getNoteById(noteId);
                if (existingNote != null) {
                    runOnUiThread(() -> {
                        etTitle.setText(existingNote.title);
                        etContent.setText(existingNote.content);
                        etLanguage.setText(existingNote.language);
                        tvLangLabel.setText(existingNote.language);
                        updateLangColor(existingNote.language);
                        btnSave.setText("Update");
                        tvHeaderTitle.setText("Edit Note");
                        btnDelete.setVisibility(View.VISIBLE);
                    });
                }
            }).start();
        }

        btnSave.setOnClickListener(v -> saveNote());
        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> deleteNote());

        setupLanguageSync();
        setupLanguageSelector();
    }

    // 🔥 DELETE NOTE
    private void deleteNote() {
        if (existingNote != null) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Delete", (d, w) -> {
                        new Thread(() -> {
                            db.noteDao().delete(existingNote);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    // 🔥 LANGUAGE DROPDOWN
    private void setupLanguageSelector() {
        View langSelector = findViewById(R.id.langSelector);
        String[] languages = {"Java", "Python", "JavaScript", "C++"};

        langSelector.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, langSelector);
            for (String lang : languages) {
                popup.getMenu().add(lang);
            }
            popup.setOnMenuItemClickListener(item -> {
                String selected = item.getTitle().toString();
                etLanguage.setText(selected);
                tvLangLabel.setText(selected);
                updateLangColor(selected);
                return true;
            });
            popup.show();
        });
    }

    private void setupLanguageSync() {
        etLanguage.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void afterTextChanged(android.text.Editable s) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                tvLangLabel.setText(s.toString());
                updateLangColor(s.toString());
            }
        });
    }

    private void updateLangColor(String lang) {
        int color = Color.GRAY;

        switch (lang.toLowerCase()) {
            case "java": color = Color.parseColor("#F8981D"); break;
            case "python": color = Color.parseColor("#3776AB"); break;
            case "javascript": color = Color.parseColor("#FFD700"); break;
            case "c++": color = Color.parseColor("#00599C"); break;
        }

        etLanguage.setTextColor(color);
        tvLangLabel.setTextColor(color);
        vLangDot.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    // 🔥 SAVE NOTE WITH USER ID
    private void saveNote() {

        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String language = etLanguage.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content required", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(new Date());

        // ✅ GET USER ID FROM SESSION
        SharedPreferences pref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int userId = pref.getInt("userId", 1);

        if (existingNote != null) {
            existingNote.title = title;
            existingNote.content = content;
            existingNote.language = language;
            existingNote.date = date;
            existingNote.userId = userId;

            new Thread(() -> {
                db.noteDao().update(existingNote);
                runOnUiThread(this::finish);
            }).start();

        } else {
            NoteEntity note = new NoteEntity();
            note.title = title;
            note.content = content;
            note.language = language;
            note.date = date;
            note.isFavorite = false;
            note.userId = userId;

            new Thread(() -> {
                db.noteDao().insert(note);
                runOnUiThread(this::finish);
            }).start();
        }
    }
}