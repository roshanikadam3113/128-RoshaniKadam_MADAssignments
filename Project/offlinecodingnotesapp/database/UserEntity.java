package com.example.offlinecodingnotesapp.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {@Index(value = "email", unique = true)}
)
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String email;
    public String password;
    public String name;
}