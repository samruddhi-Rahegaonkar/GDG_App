package com.example.gdg_app;

public class User {
    private String email;
    private String role;

    // Required empty constructor for Firebase
    public User() {}

    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Setter for role
    public void setRole(String role) {
        this.role = role;
    }
}
