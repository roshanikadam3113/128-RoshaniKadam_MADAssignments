package com.example.assignment_7_progess__ratingbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private ProgressBar circularProgress;
    private ProgressBar horizontalProgress;
    private TextView progressPercentText;
    private MaterialButton btnStartTask;
    private RatingBar ratingBar;
    private TextView ratingStatusText;
    private MaterialButton btnSubmitFeedback;

    private int progressStatus = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

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
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize Views
        circularProgress = findViewById(R.id.circular_progress);
        horizontalProgress = findViewById(R.id.horizontal_progress);
        progressPercentText = findViewById(R.id.progress_percent_text);
        btnStartTask = findViewById(R.id.btn_start_task);
        ratingBar = findViewById(R.id.rating_bar);
        ratingStatusText = findViewById(R.id.rating_status_text);
        btnSubmitFeedback = findViewById(R.id.btn_submit_feedback);

        // Initially hide circular progress
        circularProgress.setVisibility(View.GONE);

        // Progress Simulation Logic
        btnStartTask.setOnClickListener(v -> startProgressSimulation());

        // RatingBar Change Listener
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            updateRatingStatus(rating);
        });

        // Submit Feedback Button
        btnSubmitFeedback.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            if (rating > 0) {
                String message = getString(R.string.feedback_thanks, rating);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.feedback_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startProgressSimulation() {
        btnStartTask.setEnabled(false);
        circularProgress.setVisibility(View.VISIBLE);
        progressStatus = 0;

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> {
                    horizontalProgress.setProgress(progressStatus);
                    String progressText = getString(R.string.current_progress, progressStatus);
                    progressPercentText.setText(progressText);
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.post(() -> {
                circularProgress.setVisibility(View.GONE);
                btnStartTask.setEnabled(true);
                Toast.makeText(MainActivity.this, getString(R.string.task_completed), Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void updateRatingStatus(float rating) {
        String status;
        if (rating <= 1.0) {
            status = "Needs Improvement";
        } else if (rating <= 2.0) {
            status = "Fair";
        } else if (rating <= 3.0) {
            status = "Good";
        } else if (rating <= 4.0) {
            status = "Very Good";
        } else {
            status = "Excellent";
        }
        ratingStatusText.setText(status);
    }
}
