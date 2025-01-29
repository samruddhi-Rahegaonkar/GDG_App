package com.example.gdg_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText et_username, et_email, et_password, et_confirm_password, et_role;
    Button btn_submit;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        btn_submit = findViewById(R.id.btn_submit);
        et_role = findViewById(R.id.et_role);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Register Button Click Listener
        btn_submit.setOnClickListener(view -> {
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            String confirmPassword = et_confirm_password.getText().toString().trim();
            String role = et_role.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, password, role);
            }
        });
    }

    private void registerUser(String email, String password, String role) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            User newUser = new User(email, role);
                            databaseReference.child(userId).setValue(newUser)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            //Log.d("SignUpActivity", "User registered successfully.");
                                            // Ensuring Toast runs on UI thread
                                            new Handler(Looper.getMainLooper()).post(() -> {
                                                Toast.makeText(SignUpActivity.this, "Registration successful! You can now log in.", Toast.LENGTH_LONG).show();
                                            });

                                            // Delayed finish to ensure Toast visibility
                                            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 2000);
                                        } else {
                                            showToast("Failed to save user data");
                                        }
                                    });
                        }
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
        });
    }
}
