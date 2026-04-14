package com.example.assignment_4_fragments;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    Button btnHome, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = findViewById(R.id.btnHome);
        btnProfile = findViewById(R.id.btnProfile);

        // Default screen (like app opens Home)
        loadFragment(new HomeFragment());

        btnHome.setOnClickListener(v -> loadFragment(new HomeFragment()));
        btnProfile.setOnClickListener(v -> loadFragment(new ProfileFragment()));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}