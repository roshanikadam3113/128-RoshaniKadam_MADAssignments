package com.example.ise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPassword;
    Button btnLogin;

    String correctName = "admin";
    String correctPassword = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(name.equals(correctName) && password.equals(correctPassword)) {

                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LabGridActivity.class);
                    startActivity(intent);

                }
                else {

                    Toast.makeText(MainActivity.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}