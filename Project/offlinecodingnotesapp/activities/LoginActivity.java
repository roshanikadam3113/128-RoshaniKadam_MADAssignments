package com.example.offlinecodingnotesapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinecodingnotesapp.R;
import com.example.offlinecodingnotesapp.database.AppDatabase;
import com.example.offlinecodingnotesapp.database.UserEntity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etName;
    private MaterialButton btnLogin;
    private TextView btnGuest, tvToggleText, btnToggleAction, tvTitle, tvSubtitle;
    private boolean isLoginMode = true;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGuest = findViewById(R.id.btnGuest);
        tvToggleText = findViewById(R.id.tvToggleText);
        btnToggleAction = findViewById(R.id.btnToggleAction);

        btnToggleAction.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateUI();
        });

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String name = etName.getText().toString().trim();

            // ================= LOGIN =================
            if (isLoginMode) {

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    UserEntity user = db.userDao().login(email, pass);

                    runOnUiThread(() -> {
                        if (user != null) {
                            saveSession(user.id, user.email, user.name);
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }

            // ================= SIGNUP =================
            else {

                if (email.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    UserEntity existing = db.userDao().getUserByEmail(email);

                    runOnUiThread(() -> {
                        if (existing != null) {
                            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                        } else {

                            UserEntity newUser = new UserEntity();
                            newUser.email = email;
                            newUser.password = pass;
                            newUser.name = name; // Set name

                            new Thread(() -> {
                                db.userDao().registerUser(newUser);

                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
                                    isLoginMode = true;
                                    updateUI();
                                });
                            }).start();
                        }
                    });
                }).start();
            }
        });

        // ================= GUEST =================
        btnGuest.setOnClickListener(v -> {
            saveSession(-1, "Guest", "Guest User");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }

    private void updateUI() {
        if (isLoginMode) {
            tvTitle.setText("Welcome Back");
            tvSubtitle.setText("Sign in to manage your snippets");
            btnLogin.setText("Sign In");
            tvToggleText.setText("Don't have an account? ");
            btnToggleAction.setText("Sign Up");
            etName.setVisibility(View.GONE);
        } else {
            tvTitle.setText("Create Account");
            tvSubtitle.setText("Join us to start taking notes");
            btnLogin.setText("Sign Up");
            tvToggleText.setText("Already have an account? ");
            btnToggleAction.setText("Login");
            etName.setVisibility(View.VISIBLE);
        }
    }

    // ================= SESSION =================
    private void saveSession(int userId, String email, String name) {
        SharedPreferences pref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("userId", userId);
        editor.putString("user_email", email);
        editor.putString("user_name", name);
        editor.apply();
    }
}