package com.example.assignment_9_notification;
import android.Manifest;
import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    String CHANNEL_ID = "order_channel";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied. Notifications won't show.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnNotify);

        createChannel();
        checkNotificationPermission();

        btn.setOnClickListener(v -> {
            startRealTimeUpdates();
        });
    }

    private void startRealTimeUpdates() {
        Handler handler = new Handler(Looper.getMainLooper());
        final int[] progress = {0};
        
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progress[0] <= 100) {
                    String status = "Processing...";
                    String details = "We are working on your order.";
                    
                    if (progress[0] < 25) {
                        status = "Order Placed";
                        details = "Your order has been received.";
                    } else if (progress[0] < 60) {
                        status = "Preparing";
                        details = "Chef is cooking your meal...";
                    } else if (progress[0] < 90) {
                        status = "Out for Delivery";
                        details = "Rider is heading to your location.";
                    } else {
                        status = "Arrived";
                        details = "Your food is at the door!";
                    }

                    showNotification(status, details, progress[0]);
                    
                    if (progress[0] < 100) {
                        progress[0] += 5;
                        handler.postDelayed(this, 1000); // Update every second
                    } else {
                        // Final state: Remove progress bar after a short delay
                        handler.postDelayed(() -> showNotification("Delivered", "Enjoy your meal!", -1), 1000);
                    }
                }
            }
        };

        handler.post(progressRunnable);
    }

    private void showNotification(String title, String text, int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Intent intent = new Intent(this, SecondActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_send)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);

        if (progress >= 0 && progress < 100) {
            builder.setProgress(100, progress, false);
        } else if (progress == 100) {
            builder.setProgress(0, 0, false); // Finished
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(101, builder.build());
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void sendNotification() {
        // Double check permission for API 33+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Notification permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        // Intent when notification clicked
        Intent intent = new Intent(this, SecondActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_send)
                .setContentTitle("Food Delivery")
                .setContentText("Your order has been delivered!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(101, builder.build());
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Order Updates",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}