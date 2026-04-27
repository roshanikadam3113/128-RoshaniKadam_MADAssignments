package com.example.offlinecodingnotesapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String content;
    public String language;
    public String date;
    public boolean isFavorite;

    public int userId;
}