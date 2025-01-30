package com.example.gdg_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.List;


public class StudentActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView questionTextView, questionNumberText;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button submitButton;

    private String correctAnswer;
    private int questionIndex = 0;
    private List<Question> questions = new ArrayList<>();
    private String selectedCategory;  // Stores category (Java, C, C++)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        questionTextView = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitButton = findViewById(R.id.submitButton);
        questionNumberText = findViewById(R.id.questionNumberText);

        // Get selected category from intent (e.g., "Java", "C", "C++")
        selectedCategory = getIntent().getStringExtra("category");

        if (selectedCategory == null || selectedCategory.isEmpty()) {
            Toast.makeText(this, "No category selected!", Toast.LENGTH_SHORT).show();
            finish();  // Close activity if no category is selected
            return;
        }

        // Fetch questions from Firestore
        fetchQuestionsFromFirestore();

        submitButton.setOnClickListener(view -> {
            int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
            if (selectedOptionId == -1) {
                Toast.makeText(StudentActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedOption = findViewById(selectedOptionId);
                String selectedAnswer = selectedOption.getText().toString();

                if (selectedAnswer.equals(correctAnswer)) {
                    Toast.makeText(StudentActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StudentActivity.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                }

                if (questionIndex < questions.size() - 1) {
                    questionIndex++;
                    updateUI(questions.get(questionIndex));
                } else {
                    Toast.makeText(StudentActivity.this, "End of Questions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchQuestionsFromFirestore() {
        db.collection("questions").document(selectedCategory) // Select category (C, C++, Java)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore", "Error fetching data", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            questions.clear();
                            List<Object> questionList = (List<Object>) snapshot.get("questions");
                            if (questionList != null) {
                                for (Object obj : questionList) {
                                    if (obj instanceof java.util.Map) {
                                        Question question = new Question(
                                                (String) ((java.util.Map<?, ?>) obj).get("question"),
                                                (String) ((java.util.Map<?, ?>) obj).get("option1"),
                                                (String) ((java.util.Map<?, ?>) obj).get("option2"),
                                                (String) ((java.util.Map<?, ?>) obj).get("option3"),
                                                (String) ((java.util.Map<?, ?>) obj).get("option4"),
                                                (String) ((java.util.Map<?, ?>) obj).get("correctAnswer")
                                        );
                                        questions.add(question);
                                    }
                                }
                                if (!questions.isEmpty()) {
                                    questionIndex = 0;
                                    updateUI(questions.get(0));
                                }
                            }
                        }
                    }
                });
    }

    private void updateUI(Question question) {
        questionTextView.setText(question.getQuestionText());
        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        option4.setText(question.getOption4());
        correctAnswer = question.getCorrectAnswer();
        questionNumberText.setText("Question " + (questionIndex + 1) + "/" + questions.size());
    }

    // Question Model Class
    public static class Question {
        private String questionText;
        private String option1, option2, option3, option4, correctAnswer;

        public Question() {
            // Default constructor required for Firestore
        }

        public Question(String questionText, String option1, String option2, String option3, String option4, String correctAnswer) {
            this.questionText = questionText;
            this.option1 = option1;
            this.option2 = option2;
            this.option3 = option3;
            this.option4 = option4;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestionText() { return questionText; }
        public String getOption1() { return option1; }
        public String getOption2() { return option2; }
        public String getOption3() { return option3; }
        public String getOption4() { return option4; }
        public String getCorrectAnswer() { return correctAnswer; }
    }
}
