package com.example.ise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class LabGridActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_grid);

        // Show back arrow in top bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        gridView = findViewById(R.id.gridView);

        LabAdapter adapter = new LabAdapter(this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(LabGridActivity.this, LabDetailActivity.class);
                intent.putExtra("lab", position);
                startActivity(intent);

            }
        });
    }

    // Back button functionality
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}