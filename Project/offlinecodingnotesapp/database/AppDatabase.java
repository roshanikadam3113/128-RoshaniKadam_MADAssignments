package com.example.offlinecodingnotesapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class, UserEntity.class}, version = 3) // 🔥 added UserEntity + version++
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract NoteDao noteDao();
    public abstract UserDao userDao(); // 🔥 ADD THIS

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "notes_db"
                    )
                    .fallbackToDestructiveMigration() // wipes old DB if schema changes
                    .allowMainThreadQueries() // okay for now (student project)
                    .build();
        }
        return instance;
    }
}