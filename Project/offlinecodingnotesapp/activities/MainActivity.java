package com.example.offlinecodingnotesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinecodingnotesapp.R;
import com.example.offlinecodingnotesapp.adaptor.NoteAdapter;
import com.example.offlinecodingnotesapp.database.AppDatabase;
import com.example.offlinecodingnotesapp.database.NoteEntity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteChangeListener {

    private EditText searchBar;
    private TextView noteCount, emptyView;
    private FloatingActionButton fabAdd;
    private BottomNavigationView bottomNav;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private View header, searchBarContainer, chipScroll, chipDivider, searchDivider;
    private LinearLayout chipContainer;

    private AppDatabase db;
    private List<NoteEntity> allNotes = new ArrayList<>();
    private String currentLanguageFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        initViews();
        setupRecyclerView();
        setupBottomNav();
        setupSearch();
        setupLanguageChips();

        // Restore state if activity was recreated (e.g. for theme change)
        if (savedInstanceState != null) {
            int selectedId = savedInstanceState.getInt("selected_nav_id", R.id.nav_notes);
            bottomNav.setSelectedItemId(selectedId);
            // The listener in setupBottomNav will handle showing the correct UI
        }

        // Use post() to ensure the click listener is attached after view layout if needed
        fabAdd.post(() -> {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            });
        });

        // Add this to handle the top-right edit icon as well
        View btnEdit = findViewById(R.id.btnEdit);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            });
        }

        // Add this to handle the filter icon
        View btnFilter = findViewById(R.id.btnFilter);
        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this, v);
                popup.getMenu().add("Sort by Newest");
                popup.getMenu().add("Sort by Oldest");
                popup.getMenu().add("Sort A-Z");
                
                popup.setOnMenuItemClickListener(item -> {
                    String title = item.getTitle().toString();
                    if (title.equals("Sort by Newest")) {
                        Collections.sort(allNotes, (n1, n2) -> {
                            if (n1.date == null || n2.date == null) return 0;
                            return n2.date.compareTo(n1.date);
                        });
                    } else if (title.equals("Sort by Oldest")) {
                        Collections.sort(allNotes, (n1, n2) -> {
                            if (n1.date == null || n2.date == null) return 0;
                            return n1.date.compareTo(n2.date);
                        });
                    } else if (title.equals("Sort A-Z")) {
                        Collections.sort(allNotes, (n1, n2) -> {
                            if (n1.title == null || n2.title == null) return 0;
                            return n1.title.compareToIgnoreCase(n2.title);
                        });
                    }
                    filterAndDisplay(searchBar.getText().toString());
                    return true;
                });
                popup.show();
            });
        }

        loadNotes();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_nav_id", bottomNav.getSelectedItemId());
    }

    private void initViews() {
        header = findViewById(R.id.header);
        searchBarContainer = findViewById(R.id.searchContainer);
        searchBar = findViewById(R.id.searchBar);
        chipScroll = findViewById(R.id.chipScroll);
        searchDivider = findViewById(R.id.searchDivider);
        chipDivider = findViewById(R.id.chipDivider);
        
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        noteCount = findViewById(R.id.noteCount);
        fabAdd = findViewById(R.id.fabAdd);
        bottomNav = findViewById(R.id.bottom_nav);

        chipContainer = findViewById(R.id.chipContainer);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this, new ArrayList<>());
        adapter.setOnNoteChangeListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_notes) {
                showListUI(true);
                currentLanguageFilter = "All";
                updateChipSelection("All");
                loadNotes();
                return true;
            } else if (id == R.id.nav_favorites) {
                showListUI(true);
                loadFavorites();
                return true;
            } else if (id == R.id.nav_profile) {
                showListUI(false);
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void showListUI(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        header.setVisibility(visibility);
        searchBarContainer.setVisibility(visibility);
        chipScroll.setVisibility(visibility);
        searchDivider.setVisibility(visibility);
        chipDivider.setVisibility(visibility);
        recyclerView.setVisibility(visibility);
        fabAdd.setVisibility(visibility);
        
        // Hide empty view when not in list UI
        if (!show) {
            emptyView.setVisibility(View.GONE);
        } else {
            // If showing list UI, check if it should be visible based on adapter count
            if (adapter != null && adapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            }
        }

        findViewById(R.id.fragment_container).setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAndDisplay(s.toString());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupLanguageChips() {
        if (chipContainer == null) return;
        for (int i = 0; i < chipContainer.getChildCount(); i++) {
            View child = chipContainer.getChildAt(i);
            if (child instanceof TextView) {
                TextView chip = (TextView) child;
                chip.setOnClickListener(v -> {
                    String text = chip.getText().toString();
                    currentLanguageFilter = text.replace("● ", "");
                    updateChipSelection(text);
                    filterAndDisplay(searchBar != null ? searchBar.getText().toString() : "");
                });
            }
        }
    }

    private void updateChipSelection(String selectedText) {
        if (chipContainer == null) return;
        for (int i = 0; i < chipContainer.getChildCount(); i++) {
            View child = chipContainer.getChildAt(i);
            if (child instanceof TextView) {
                TextView chip = (TextView) child;
                if (chip.getText().toString().equals(selectedText)) {
                    chip.setBackgroundResource(R.drawable.chip_bg_selected);
                    chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_purple)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.bg_main));
                    chip.setTypeface(null, android.graphics.Typeface.BOLD);
                } else {
                    chip.setBackgroundResource(R.drawable.chip_bg);
                    chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.bg_surface)));
                    
                    // Set text color based on language prefix
                    String text = chip.getText().toString();
                    int color = ContextCompat.getColor(this, R.color.text_secondary);
                    if (text.contains("JavaScript")) color = ContextCompat.getColor(this, R.color.lang_js);
                    else if (text.contains("TypeScript")) color = ContextCompat.getColor(this, R.color.lang_ts);
                    else if (text.contains("Python")) color = ContextCompat.getColor(this, R.color.lang_python);
                    else if (text.contains("Java")) color = ContextCompat.getColor(this, R.color.lang_java);
                    
                    chip.setTextColor(color);
                    chip.setTypeface(null, android.graphics.Typeface.NORMAL);
                }
            }
        }
    }

    private void loadNotes() {
        final String query = searchBar != null ? searchBar.getText().toString() : "";
        new Thread(() -> {
            SharedPreferences pref = getSharedPreferences("user_session", MODE_PRIVATE);
            String email = pref.getString("user_email", "Guest");
            allNotes = db.noteDao().getAllNotesByUser(email);
            filterAndDisplay(query);
        }).start();
    }

    private void loadFavorites() {
        final String query = searchBar != null ? searchBar.getText().toString() : "";
        new Thread(() -> {
            SharedPreferences pref = getSharedPreferences("user_session", MODE_PRIVATE);
            String email = pref.getString("user_email", "Guest");
            allNotes = db.noteDao().getFavoriteNotesByUser(email);
            filterAndDisplay(query);
        }).start();
    }

    private void filterAndDisplay(String query) {
        if (allNotes == null) allNotes = new ArrayList<>();
        String lowerQuery = query != null ? query.toLowerCase() : "";
        List<NoteEntity> filtered = new ArrayList<>();
        
        for (NoteEntity note : allNotes) {
            if (note == null || note.title == null) continue;

            boolean matchesSearch = note.title.toLowerCase().contains(lowerQuery) || 
                                  (note.content != null && note.content.toLowerCase().contains(lowerQuery));
            boolean matchesLang = currentLanguageFilter.equals("All") || (note.language != null && note.language.equalsIgnoreCase(currentLanguageFilter));
            
            if (matchesSearch && matchesLang) {
                filtered.add(note);
            }
        }

        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.updateList(filtered);
            }
            int count = filtered.size();
            if (noteCount != null) {
                noteCount.setText(count + (count == 1 ? " note" : " notes"));
            }
            
            // Only show emptyView if we are NOT on the profile page
            if (bottomNav != null && bottomNav.getSelectedItemId() != R.id.nav_profile) {
                if (count == 0) {
                    if (emptyView != null) {
                        emptyView.setText("📭 No notes yet\nTap + to add your first note");
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (emptyView != null) emptyView.setVisibility(View.GONE);
                }
            } else {
                if (emptyView != null) emptyView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNoteDeleted() {
        loadNotes();
    }

    @Override
    public void onFavoriteChanged() {
        if (bottomNav.getSelectedItemId() == R.id.nav_favorites) {
            loadFavorites();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav.getSelectedItemId() != R.id.nav_profile) {
            loadNotes();
        }
    }
}