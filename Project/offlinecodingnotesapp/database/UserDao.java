package com.example.offlinecodingnotesapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void registerUser(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);
}