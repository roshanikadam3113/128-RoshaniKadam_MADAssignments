package com.example.form_using_sqlitedb;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextInputEditText editName, editRoll, editEmail, editPhone, editCourse, editTextId, editDob, editAddress;
    RadioGroup radioGroupGender;
    Button btnAddData, btnviewAll, btnDelete, btnviewUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        // Initialize Views
        editTextId = findViewById(R.id.editTextId);
        editName = findViewById(R.id.editTextName);
        editDob = findViewById(R.id.editTextDob);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        editEmail = findViewById(R.id.editTextEmail);
        editPhone = findViewById(R.id.editTextPhone);
        editAddress = findViewById(R.id.editTextAddress);
        editRoll = findViewById(R.id.editTextRoll);
        editCourse = findViewById(R.id.editTextCourse);

        btnAddData = findViewById(R.id.buttonAdd);
        btnviewAll = findViewById(R.id.buttonViewAll);
        btnviewUpdate = findViewById(R.id.buttonUpdate);
        btnDelete = findViewById(R.id.buttonDelete);

        addData();
        viewAll();
        updateData();
        deleteData();
    }

    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == -1) return "Not Specified";
        RadioButton radioButton = findViewById(selectedId);
        return radioButton.getText().toString();
    }

    public void addData() {
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(
                        editName.getText().toString(),
                        editRoll.getText().toString(),
                        editEmail.getText().toString(),
                        editPhone.getText().toString(),
                        getSelectedGender(),
                        editDob.getText().toString(),
                        editCourse.getText().toString(),
                        editAddress.getText().toString()
                );
                if (isInserted)
                    Toast.makeText(MainActivity.this, "Student Registered Successfully", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateData() {
        btnviewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();
                if (id.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter ID to update", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isUpdate = myDb.updateData(
                        id,
                        editName.getText().toString(),
                        editRoll.getText().toString(),
                        editEmail.getText().toString(),
                        editPhone.getText().toString(),
                        getSelectedGender(),
                        editDob.getText().toString(),
                        editCourse.getText().toString(),
                        editAddress.getText().toString()
                );
                if (isUpdate)
                    Toast.makeText(MainActivity.this, "Record Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteData() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();
                if (id.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter ID to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer deletedRows = myDb.deleteData(id);
                if (deletedRows > 0)
                    Toast.makeText(MainActivity.this, "Record Deleted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "No Record Found with that ID", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Empty Database", "No student records found.");
                    return;
                }

                StringBuilder buffer = new StringBuilder();
                while (res.moveToNext()) {
                    buffer.append("ID: ").append(res.getString(0)).append("\n");
                    buffer.append("Name: ").append(res.getString(1)).append("\n");
                    buffer.append("Roll: ").append(res.getString(2)).append("\n");
                    buffer.append("Email: ").append(res.getString(3)).append("\n");
                    buffer.append("Phone: ").append(res.getString(4)).append("\n");
                    buffer.append("Gender: ").append(res.getString(5)).append("\n");
                    buffer.append("DOB: ").append(res.getString(6)).append("\n");
                    buffer.append("Course: ").append(res.getString(7)).append("\n");
                    buffer.append("Address: ").append(res.getString(8)).append("\n\n");
                }
                showMessage("Student Records", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
