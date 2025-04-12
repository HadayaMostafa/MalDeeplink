package com.example.maldeeplink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profile extends AppCompatActivity {

    private TextView malToken, malUserId, malUsername, malEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize TextViews
        malToken = findViewById(R.id.malToken);
        malUserId = findViewById(R.id.malUserId);
        malUsername = findViewById(R.id.malUsername);
        malEmail = findViewById(R.id.malEmail);

        // Get data from SharedPreferences (where you saved it in deeplink.java)
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String token = prefs.getString("token", "No token found");
        String userId = prefs.getString("user_id", "No ID found");
        String username = prefs.getString("username", "No username found");
        String email = prefs.getString("email", "No email found");

        // Display the data
        malToken.setText("Token: " + token);
        malUserId.setText("User ID: " + userId);
        malUsername.setText("Username: " + username);
        malEmail.setText("Email: " + email);
    }
}