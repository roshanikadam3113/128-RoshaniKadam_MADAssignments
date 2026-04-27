package com.example.offlinecodingnotesapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class NotesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences pref = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        boolean isDark = pref.getBoolean("is_dark", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}