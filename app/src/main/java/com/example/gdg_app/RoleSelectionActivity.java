package com.example.gdg_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RoleSelectionActivity extends AppCompatActivity {

    private TextView roleTextView;
    private Button btnAdmin, btnStudent;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        // Initialize UI components
        roleTextView = findViewById(R.id.roleTextView);
        btnAdmin = findViewById(R.id.btn_admin);
        btnStudent = findViewById(R.id.btn_student);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // On click listeners for Admin and Student buttons
        btnAdmin.setOnClickListener(view -> {
            Intent intent=new Intent(RoleSelectionActivity.this, AdminCategoryActivity.class);
            startActivity(intent);
        });

        btnStudent.setOnClickListener(view -> {
            Intent intent=new Intent(RoleSelectionActivity.this, AdminCategoryActivity.class);
            startActivity(intent);
        });
    }

//    private void checkUserRole(String role) {
//        // Get the current user's ID
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        if (userId != null) {
//            // Fetch user data from Firebase
//            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // Extract the role of the user from Firebase
//                        String userRole = dataSnapshot.child("role").getValue(String.class);
//
//                        // Check if the role matches the selected one
//                        if (userRole != null && userRole.equalsIgnoreCase(role)) {
//                            // Navigate to the respective activity based on role
//                            if (role.equalsIgnoreCase("Admin")) {
//                                Intent adminIntent = new Intent(RoleSelectionActivity.this, AdminActivity.class);
//                                startActivity(adminIntent);
//                            } else if (role.equalsIgnoreCase("Student")) {
//                                Intent studentIntent = new Intent(RoleSelectionActivity.this, StudentActivity.class);
//                                startActivity(studentIntent);
//                            }
//                        } else {
//                            Toast.makeText(RoleSelectionActivity.this, "You don't have the required role to proceed", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(RoleSelectionActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Handle errors in retrieving data
//                    Toast.makeText(RoleSelectionActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
//        }
    }

