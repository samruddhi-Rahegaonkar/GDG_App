package com.example.gdg_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AdminActivity extends AppCompatActivity {
    private EditText questionInput, option1Input, option2Input, option3Input, option4Input, correctAnswerInput;
    private Button addQuestionButton, uploadQuizButton;
    private RecyclerView questionsRecyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionsList = new ArrayList<>();
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        selectedCategory = getIntent().getStringExtra("CATEGORY");

        questionInput = findViewById(R.id.questionInput);
        option1Input = findViewById(R.id.option1Input);
        option2Input = findViewById(R.id.option2Input);
        option3Input = findViewById(R.id.option3Input);
        option4Input = findViewById(R.id.option4Input);
        correctAnswerInput = findViewById(R.id.correctAnswerInput);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        uploadQuizButton = findViewById(R.id.uploadQuizButton);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);

        // Setup RecyclerView
        questionAdapter = new QuestionAdapter(questionsList);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setAdapter(questionAdapter);

        addQuestionButton.setOnClickListener(view -> addQuestion());
        uploadQuizButton.setOnClickListener(view -> uploadQuizToFirestore());
    }

    private void addQuestion() {
        String questionText = questionInput.getText().toString().trim();
        String option1 = option1Input.getText().toString().trim();
        String option2 = option2Input.getText().toString().trim();
        String option3 = option3Input.getText().toString().trim();
        String option4 = option4Input.getText().toString().trim();
        String correctAnswer = correctAnswerInput.getText().toString().trim();

        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() ||
                option3.isEmpty() || option4.isEmpty() || correctAnswer.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        Question newQuestion = new Question(questionText, option1, option2, option3, option4, correctAnswer);
        questionsList.add(newQuestion);
        questionAdapter.notifyDataSetChanged();

        uploadQuizButton.setVisibility(View.VISIBLE);
        questionsRecyclerView.setVisibility(View.VISIBLE);

        clearFields();
    }

    private void clearFields() {
        questionInput.setText("");
        option1Input.setText("");
        option2Input.setText("");
        option3Input.setText("");
        option4Input.setText("");
        correctAnswerInput.setText("");
    }

    private void uploadQuizToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("questions", questionsList);

        db.collection("questions").document(selectedCategory)
                .set(quizData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Quiz Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload quiz!", Toast.LENGTH_SHORT).show());
    }
}
