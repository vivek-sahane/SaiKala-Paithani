package com.example.saikalapaithani;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Enable edge-to-edge display
        enableEdgeToEdge();

        // Retrieve latitude and longitude from the intent
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 20.012803128867848);
        longitude = intent.getDoubleExtra("longitude", 74.49310115933933);

        // Log the values to ensure they are correct
        Log.d("MapActivity", "Latitude: " + latitude + " Longitude: " + longitude);

        Button buttonOpenMap = findViewById(R.id.buttonOpenMap);

        buttonOpenMap.setOnClickListener(v -> {
            // Create a Uri for the geo location
            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Check if Google Maps is installed
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Google Maps is not installed, show a message
                Toast.makeText(MapActivity.this, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableEdgeToEdge() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }
}
