package com.example.gdg_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class AdminCategoryActivity extends AppCompatActivity {
    private Button categoryC, categoryCpp, categoryJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        categoryC = findViewById(R.id.category_c);
        categoryCpp = findViewById(R.id.category_cpp);
        categoryJava = findViewById(R.id.category_java);

        categoryC.setOnClickListener(view -> openAddQuestionsScreen("C"));
        categoryCpp.setOnClickListener(view -> openAddQuestionsScreen("C++"));
        categoryJava.setOnClickListener(view -> openAddQuestionsScreen("Java"));
    }

    private void openAddQuestionsScreen(String category) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}
