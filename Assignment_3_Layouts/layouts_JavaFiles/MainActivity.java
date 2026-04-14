package com.example.assignment_3_layout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        View toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Edge-to-edge handling for main view (bottom insets)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Edge-to-edge handling for toolbar (top insets)
        if (toolbar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }

        // Real-life Example 1: Social Media Post (ConstraintLayout)
        ImageButton btnLike = findViewById(R.id.btn_like);
        TextView likesTv = findViewById(R.id.likes_tv);
        final boolean[] isLiked = {false};
        final int[] likesCount = {0};
        if (btnLike != null && likesTv != null) {
            btnLike.setOnClickListener(v -> {
                isLiked[0] = !isLiked[0];
                if (isLiked[0]) {
                    likesCount[0]++;
                    btnLike.setImageResource(android.R.drawable.btn_star_big_on);
                    btnLike.setColorFilter(getResources().getColor(R.color.blue_primary, getTheme()));
                } else {
                    likesCount[0]--;
                    btnLike.setImageResource(android.R.drawable.btn_star_big_off);
                    btnLike.clearColorFilter();
                }
                likesTv.setText(getString(R.string.likes_count, likesCount[0]));
            });
        }

        // Real-life Example 2: Shopping Cart (TableLayout)
        EditText qty1 = findViewById(R.id.qty_item1);
        EditText qty2 = findViewById(R.id.qty_item2);
        TextView cartTotalTv = findViewById(R.id.cart_total_tv);
        if (qty1 != null && qty2 != null && cartTotalTv != null) {
            TextWatcher cartWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        int q1 = Integer.parseInt(qty1.getText().toString());
                        int q2 = Integer.parseInt(qty2.getText().toString());
                        double total = (q1 * 999) + (q2 * 49);
                        cartTotalTv.setText(getString(R.string.total_label, total));
                    } catch (NumberFormatException e) {
                        cartTotalTv.setText(getString(R.string.total_label, 0.0));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            };
            qty1.addTextChangedListener(cartWatcher);
            qty2.addTextChangedListener(cartWatcher);
        }

        // Real-life Example 3: Music Player (RelativeLayout)
        SeekBar musicSeekBar = findViewById(R.id.music_seek_bar);
        ImageButton btnPlayPause = findViewById(R.id.btn_play_pause);
        TextView timeElapsed = findViewById(R.id.music_time_elapsed);
        final boolean[] isPlaying = {true};
        Handler musicHandler = new Handler(Looper.getMainLooper());
        if (musicSeekBar != null && btnPlayPause != null) {
            btnPlayPause.setOnClickListener(v -> {
                isPlaying[0] = !isPlaying[0];
                btnPlayPause.setImageResource(isPlaying[0] ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
            });

            musicHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isPlaying[0]) {
                        int progress = musicSeekBar.getProgress();
                        if (progress < 100) {
                            musicSeekBar.setProgress(progress + 1);
                            if (timeElapsed != null) {
                                int seconds = (progress * 225) / 100; // 3:45 = 225 seconds
                                int min = seconds / 60;
                                int sec = seconds % 60;
                                timeElapsed.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
                            }
                        }
                        else musicSeekBar.setProgress(0);
                    }
                    musicHandler.postDelayed(this, 1000);
                }
            });
        }

        // Real-life Example 4: Dashboard (GridLayout)
        TextView cpuTv = findViewById(R.id.cpu_tv);
        TextView ramTv = findViewById(R.id.ram_tv);
        TextView netTv = findViewById(R.id.net_tv);
        TextView tempTv = findViewById(R.id.temp_tv);
        Random random = new Random();
        if (cpuTv != null) {
            Handler dashboardHandler = new Handler(Looper.getMainLooper());
            dashboardHandler.post(new Runnable() {
                @Override
                public void run() {
                    int cpu = random.nextInt(100);
                    int ram = 2000 + random.nextInt(2000);
                    int net = random.nextInt(500);
                    int temp = 40 + random.nextInt(30);

                    cpuTv.setText(getString(R.string.cpu_usage, cpu));
                    ramTv.setText(getString(R.string.ram_usage, ram));
                    netTv.setText(getString(R.string.net_speed, net));
                    tempTv.setText(getString(R.string.temp, temp));

                    // Visual feedback for high net speed
                    if (net > 400) {
                        netTv.setTextColor(getResources().getColor(android.R.color.holo_red_light, getTheme()));
                    } else {
                        netTv.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                    }

                    dashboardHandler.postDelayed(this, 2000);
                }
            });
        }

        // FAB Interaction
        if (fab != null) {
            fab.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "FAB Clicked! This is a real-time event.", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
