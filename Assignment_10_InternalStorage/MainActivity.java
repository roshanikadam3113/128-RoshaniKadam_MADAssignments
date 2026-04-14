package com.example.assignment_10_internalstorage;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    EditText etName, etID, etDept;
    TextView tvDisplay;
    Button btnSave, btnRead, btnClear;

    String fileName = "student_info.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etID = findViewById(R.id.etID);
        etDept = findViewById(R.id.etDept);
        tvDisplay = findViewById(R.id.tvDisplay);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        btnClear = findViewById(R.id.btnClear);

        // Clear Data
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etID.setText("");
                etDept.setText("");
                tvDisplay.setText("No data displayed yet");
            }
        });

        // Save Data
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String id = etID.getText().toString();
                String dept = etDept.getText().toString();

                if (name.isEmpty() || id.isEmpty() || dept.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String data = "Name: " + name + "\nID: " + id + "\nDept: " + dept;

                try {
                    FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                    fos.write(data.getBytes());
                    fos.close();

                    etName.setText("");
                    etID.setText("");
                    etDept.setText("");

                    Toast.makeText(MainActivity.this, "Student Info Saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Read Data
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fis = openFileInput(fileName);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);

                    String line;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    tvDisplay.setText(sb.toString());
                    fis.close();

                } catch (FileNotFoundException e) {
                    tvDisplay.setText("No data found");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error reading data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
