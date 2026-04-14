package com.example.assignment_6_inputcontrols;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner departmentSpinner;
    private Button btnPickDate, btnSubmit;
    private TextView tvSelectedDate, tvExperienceYears;
    private SeekBar experienceSeekBar;
    private SwitchMaterial darkModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Views
        departmentSpinner = findViewById(R.id.departmentSpinner);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        experienceSeekBar = findViewById(R.id.experienceSeekBar);
        tvExperienceYears = findViewById(R.id.tvExperienceYears);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        setupSpinner();
        setupDatePicker();
        setupSeekBar();
        setupSubmitButton();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departments_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);
    }

    private void setupDatePicker() {
        btnPickDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                        tvSelectedDate.setText(getString(R.string.selected_date, date));
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void setupSeekBar() {
        experienceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvExperienceYears.setText(getString(R.string.years_label, progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        // Initial state
        tvExperienceYears.setText(getString(R.string.years_label, 0));
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> Toast.makeText(this, "Details Submitted Successfully!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
