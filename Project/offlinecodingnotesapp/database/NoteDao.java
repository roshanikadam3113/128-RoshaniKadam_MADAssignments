package com.example.offlinecodingnotesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    // 🔥 MAIN METHOD (USE THIS ONLY)
    @Query("SELECT notes.* FROM notes INNER JOIN users ON notes.userId = users.id WHERE users.email = :email ORDER BY notes.id DESC")
    List<NoteEntity> getAllNotesByUser(String email);

    @Query("SELECT notes.* FROM notes INNER JOIN users ON notes.userId = users.id WHERE users.email = :email AND notes.isFavorite = 1 ORDER BY notes.id DESC")
    List<NoteEntity> getFavoriteNotesByUser(String email);

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY id DESC")
    List<NoteEntity> getNotesByUser(int userId);

    // 🔥 DEBUG METHOD (TEMP)
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<NoteEntity> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId LIMIT 1")
    NoteEntity getNoteById(int noteId);
}