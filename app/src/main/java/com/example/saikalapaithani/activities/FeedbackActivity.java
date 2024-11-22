package com.example.saikalapaithani.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saikalapaithani.Feedback;
import com.example.saikalapaithani.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextServiceProductUsed, editTextDeliveryExperience, editTextSuggestions;
    private RatingBar ratingBar;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        // Bind views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextServiceProductUsed = findViewById(R.id.editTextServiceProductUsed);
        editTextDeliveryExperience = findViewById(R.id.editTextDeliveryExperience);
        editTextSuggestions = findViewById(R.id.editTextSuggestions);
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);

        // Set onClick listener for submit button
        submitButton.setOnClickListener(v -> submitFeedback());

        // Custom touch listener for rating bar
        ratingBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float touchPositionX = event.getX();
                float width = ratingBar.getWidth();
                float stars = (touchPositionX / width) * ratingBar.getNumStars();
                int newRating = (int) stars + 1;

                if (newRating == ratingBar.getRating()) {
                    ratingBar.setRating(newRating - 1);
                } else {
                    ratingBar.setRating(newRating);
                }
            }
            return true;
        });
    }

    private void submitFeedback() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String serviceProductUsed = editTextServiceProductUsed.getText().toString().trim();
        String deliveryExperience = editTextDeliveryExperience.getText().toString().trim();
        String suggestions = editTextSuggestions.getText().toString().trim();
        float rating = ratingBar.getRating();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || serviceProductUsed.isEmpty() || deliveryExperience.isEmpty() || suggestions.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create feedback object
        Feedback feedback = new Feedback(name, email, serviceProductUsed, deliveryExperience, suggestions, rating);

        // Save feedback to Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference feedbackRef = database.getReference("feedbacks");
        feedbackRef.push().setValue(feedback).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(FeedbackActivity.this, "Feedback Submitted Successfully!", Toast.LENGTH_SHORT).show();
                resetForm();
            } else {
                Toast.makeText(FeedbackActivity.this, "Error submitting feedback. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextServiceProductUsed.setText("");
        editTextDeliveryExperience.setText("");
        editTextSuggestions.setText("");
        ratingBar.setRating(0);
    }
}
