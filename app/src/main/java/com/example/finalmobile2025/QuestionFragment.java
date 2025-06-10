package com.example.finalmobile2025;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalmobile2025.models.Question;

public class QuestionFragment extends Fragment {    private static final String ARG_QUESTION = "question";
    private static final String ARG_QUESTION_NUMBER = "question_number";
    private static final String ARG_TOTAL_QUESTIONS = "total_questions";
    private static final String ARG_CURRENT_ANSWER = "current_answer";
    private static final String ARG_IS_MARKED = "is_marked";
    
    private Question question;
    private int questionNumber;
    private int totalQuestions;
    private String currentAnswer;
    private boolean isMarked = false;
    
    private TextView tvQuestionNumber;
    private TextView tvQuestionType;
    private TextView tvQuestionText;
    private EditText etAnswer;    public static QuestionFragment newInstance(Question question, int questionNumber, 
                                             int totalQuestions, String currentAnswer, boolean isMarked) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        args.putInt(ARG_QUESTION_NUMBER, questionNumber);
        args.putInt(ARG_TOTAL_QUESTIONS, totalQuestions);
        args.putString(ARG_CURRENT_ANSWER, currentAnswer);
        args.putBoolean(ARG_IS_MARKED, isMarked);
        fragment.setArguments(args);
        return fragment;
    }

    // Backward compatibility method
    public static QuestionFragment newInstance(Question question, int questionNumber, 
                                             int totalQuestions, String currentAnswer) {
        return newInstance(question, questionNumber, totalQuestions, currentAnswer, false);
    }
      @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
            questionNumber = getArguments().getInt(ARG_QUESTION_NUMBER);
            totalQuestions = getArguments().getInt(ARG_TOTAL_QUESTIONS);
            currentAnswer = getArguments().getString(ARG_CURRENT_ANSWER, "");
            isMarked = getArguments().getBoolean(ARG_IS_MARKED, false);
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        
        initViews(view);
        setupQuestionData();
        
        return view;
    }
    
    private void initViews(View view) {
        tvQuestionNumber = view.findViewById(R.id.tv_question_number);
        tvQuestionType = view.findViewById(R.id.tv_question_type);
        tvQuestionText = view.findViewById(R.id.tv_question_text);
        etAnswer = view.findViewById(R.id.et_answer);
    }
    
    private void setupQuestionData() {
        if (question != null) {
            tvQuestionNumber.setText(String.valueOf(questionNumber));
            tvQuestionType.setText(question.getQuestionType());
            tvQuestionText.setText(question.getQuestionText());
            etAnswer.setText(currentAnswer);
        }
    }
    
    public String getCurrentAnswer() {
        return etAnswer.getText().toString().trim();
    }
      public boolean isMarked() {
        return this.isMarked;
    }
      public void toggleMarkStatus() {
        this.isMarked = !this.isMarked;
    }
    
    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
