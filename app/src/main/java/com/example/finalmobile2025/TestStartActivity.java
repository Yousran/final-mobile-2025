package com.example.finalmobile2025;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalmobile2025.api.ApiClient;
import com.example.finalmobile2025.api.ApiService;
import com.example.finalmobile2025.database.ParticipantDAO;
import com.example.finalmobile2025.models.Answer;
import com.example.finalmobile2025.models.ApiQuestion;
import com.example.finalmobile2025.models.EssayAnswerRequest;
import com.example.finalmobile2025.models.EssayAnswerResponse;
import com.example.finalmobile2025.models.ParticipantData;
import com.example.finalmobile2025.models.Question;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestStartActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvTimer;
    private TextView tvTestTitle;
    private MaterialButton btnPrevious;
    private MaterialButton btnMark;
    private MaterialButton btnNext;
    private MaterialButton btnFinish;
    
    private String participantId;
    private ParticipantData participantData;
    private List<Question> questions;
    private List<ApiQuestion> apiQuestions;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private ParticipantDAO participantDAO;
      // Track previous answers to detect changes
    private Map<String, String> previousAnswers = new HashMap<>();
      // Track if essay submission is in progress
    private boolean isEssaySubmissionInProgress = false;
    
    // Reference to the dialog confirm button for dynamic updates
    private MaterialButton dialogConfirmButton = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_start);
        
        participantDAO = new ParticipantDAO(this);
        
        // Get participant ID from intent or database
        participantId = getIntent().getStringExtra("PARTICIPANT_ID");
        if (participantId == null) {
            participantId = participantDAO.getLatestParticipantId();
        }
        
        if (participantId == null) {
            Toast.makeText(this, "No participant data found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        setupClickListeners();
        fetchParticipantData();
    }
      private void initViews() {
        tvUsername = findViewById(R.id.tv_username);
        tvTimer = findViewById(R.id.tv_timer);
        tvTestTitle = findViewById(R.id.tv_test_title);
        btnPrevious = findViewById(R.id.btn_previous);
        btnMark = findViewById(R.id.btn_mark);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.btn_finish);
    }
      private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> navigateToPreviousQuestion());
        btnNext.setOnClickListener(v -> navigateToNextQuestion());
        btnFinish.setOnClickListener(v -> showFinishConfirmationDialog());
        btnMark.setOnClickListener(v -> markCurrentQuestion());
    }
    
    private void fetchParticipantData() {
        ApiService apiService = ApiClient.getApiService();
        Call<ParticipantData> call = apiService.getParticipantData(participantId);
        
        call.enqueue(new Callback<ParticipantData>() {
            @Override
            public void onResponse(Call<ParticipantData> call, Response<ParticipantData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    participantData = response.body();
                    setupTestInterface();
                } else {
                    Log.e("TestStartActivity", "Failed to fetch participant data: " + response.code());
                    Toast.makeText(TestStartActivity.this, "Failed to load test data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            
            @Override
            public void onFailure(Call<ParticipantData> call, Throwable t) {
                Log.e("TestStartActivity", "Network error: " + t.getMessage());
                Toast.makeText(TestStartActivity.this, "Network error occurred", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }      private void setupTestInterface() {
        // Set participant info
        tvUsername.setText(participantData.getUsername());
        
        // Set test title
        if (participantData.getTest() != null) {
            tvTestTitle.setText(participantData.getTest().getTitle());
        }
        
        // Get questions and convert to our Question model
        apiQuestions = participantData.getQuestions();
        if (apiQuestions == null || apiQuestions.isEmpty()) {
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Convert ApiQuestion to Question for compatibility
        questions = convertApiQuestionsToQuestions(apiQuestions);
        
        // Initialize previous answers map with current answers from API
        initializePreviousAnswers();
        
        // Start timer
        startTimer(participantData.getTimeRemaining());
        
        // Load first question
        loadQuestion(currentQuestionIndex);
        
        // Update navigation buttons
        updateNavigationButtons();
    }
    
    private List<Question> convertApiQuestionsToQuestions(List<ApiQuestion> apiQuestions) {
        List<Question> convertedQuestions = new ArrayList<>();
        for (ApiQuestion apiQuestion : apiQuestions) {
            Question question = new Question();
            question.setId(apiQuestion.getId());
            question.setQuestionText(stripHtmlTags(apiQuestion.getQuestionText())); // Remove HTML tags
            question.setQuestionType(apiQuestion.getType());
            question.setOrderIndex(apiQuestion.getOrder());
            question.setPoints(0); // Default value
            convertedQuestions.add(question);
        }
        return convertedQuestions;
    }
    
    private void initializePreviousAnswers() {
        // Initialize the previous answers map with current answers from the API
        if (apiQuestions != null) {
            for (ApiQuestion apiQuestion : apiQuestions) {
                if ("ESSAY".equals(apiQuestion.getType()) &&
                    apiQuestion.getEssay() != null && 
                    apiQuestion.getEssay().getAnswer() != null) {
                    
                    String questionId = apiQuestion.getId();
                    String answerText = apiQuestion.getEssay().getAnswer().getAnswerText();
                    previousAnswers.put(questionId, answerText != null ? answerText : "");
                }
            }
        }
        Log.d("TestStartActivity", "Initialized previous answers for " + previousAnswers.size() + " essay questions");
    }
    
    private String stripHtmlTags(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]*>", "").trim();
    }
    
    private void startTimer(int timeRemainingInSeconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        countDownTimer = new CountDownTimer(timeRemainingInSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }
            
            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                // Auto-submit test when time runs out
                finishTest();
            }
        };
        
        countDownTimer.start();
    }
    
    private void loadQuestion(int questionIndex) {
        if (questions == null || questionIndex < 0 || questionIndex >= questions.size()) {
            return;
        }
        
        Question question = questions.get(questionIndex);
        
        // Create and load question fragment
        QuestionFragment fragment = QuestionFragment.newInstance(
            question,
            questionIndex + 1,
            questions.size(),
            getCurrentAnswer(question.getId())
        );
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }    private String getCurrentAnswer(String questionId) {
        // Get answer from the API data structure
        if (apiQuestions != null) {
            for (ApiQuestion apiQuestion : apiQuestions) {
                if (questionId.equals(apiQuestion.getId()) && 
                    apiQuestion.getEssay() != null && 
                    apiQuestion.getEssay().getAnswer() != null) {
                    String answerText = apiQuestion.getEssay().getAnswer().getAnswerText();
                    return answerText != null ? answerText : "";
                }
            }
        }
        return "";
    }
      private void navigateToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            saveCurrentAnswerAndSubmitIfChanged();
            currentQuestionIndex--;
            loadQuestion(currentQuestionIndex);
            updateNavigationButtons();
        }
    }    private void navigateToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            saveCurrentAnswerAndSubmitIfChanged();
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
            updateNavigationButtons();
        }
    }    private void showFinishConfirmationDialog() {
        saveCurrentAnswerAndSubmitIfChanged();
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_finish, null);
        
        // Get dialog components
        com.google.android.material.button.MaterialButton btnCancelFinish = dialogView.findViewById(R.id.btn_cancel_finish);
        dialogConfirmButton = dialogView.findViewById(R.id.btn_confirm_finish);
        
        // Create and configure the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        // Make dialog background transparent for custom rounded corners
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        
        // Set initial button state
        updateDialogConfirmButtonState();
        
        // Set up button click listeners
        btnCancelFinish.setOnClickListener(v -> {
            dialogConfirmButton = null; // Clear reference when dialog is dismissed
            dialog.dismiss();
        });
        
        dialogConfirmButton.setOnClickListener(v -> {
            // Only allow finishing if no essay submission is in progress
            if (!isEssaySubmissionInProgress) {
                // User confirmed, finish the test
                dialogConfirmButton = null; // Clear reference before finishing
                finishTest();
                dialog.dismiss();
            } else {
                Toast.makeText(TestStartActivity.this, "Please wait for answer submission to complete", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Clear reference when dialog is dismissed
        dialog.setOnDismissListener(d -> dialogConfirmButton = null);
        
        dialog.show();
    }
    
    private void markCurrentQuestion() {
        // Toggle mark status for current question
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof QuestionFragment) {
            ((QuestionFragment) fragment).toggleMarkStatus();
        }
        
        // Update mark button appearance
        updateMarkButton();
    }
    
    private void updateMarkButton() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof QuestionFragment) {
            boolean isMarked = ((QuestionFragment) fragment).isMarked();
            if (isMarked) {
                btnMark.setText("Unmark");
                btnMark.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            } else {
                btnMark.setText("Mark");
                btnMark.setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }
      private void updateNavigationButtons() {
        // Update Previous button
        btnPrevious.setEnabled(currentQuestionIndex > 0);
        
        // Update Next and Finish button visibility
        if (currentQuestionIndex == questions.size() - 1) {
            // Last question - show Finish button, hide Next button
            btnNext.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        } else {
            // Not last question - show Next button, hide Finish button
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        }
        
        updateMarkButton();
    }
    
    private void saveCurrentAnswer() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof QuestionFragment) {
            String answer = ((QuestionFragment) fragment).getCurrentAnswer();
            boolean isMarked = ((QuestionFragment) fragment).isMarked();
            
            // Save answer to participant data
            Question currentQuestion = questions.get(currentQuestionIndex);
            updateAnswerInData(currentQuestion.getId(), answer, isMarked);
        }
    }    private void updateAnswerInData(String questionId, String answerText, boolean isMarked) {
        // Update answer in the API data structure
        if (apiQuestions != null) {
            for (ApiQuestion apiQuestion : apiQuestions) {
                if (questionId.equals(apiQuestion.getId()) && 
                    apiQuestion.getEssay() != null && 
                    apiQuestion.getEssay().getAnswer() != null) {
                    apiQuestion.getEssay().getAnswer().setAnswerText(answerText);
                    return;
                }
            }
        }
        Log.w("TestStartActivity", "Answer not found for question: " + questionId);
    }    private void submitEssayAnswerToServer(String questionId, String answerText) {
        // Find the answer ID for this question
        String answerId = getAnswerIdForQuestion(questionId);
        if (answerId == null) {
            Log.w("TestStartActivity", "Answer ID not found for question: " + questionId);
            return;
        }

        // Set submission in progress flag
        isEssaySubmissionInProgress = true;
        // Update dialog button state if dialog is open
        updateDialogConfirmButtonState();

        EssayAnswerRequest request = new EssayAnswerRequest(answerId, participantId, answerText);
        ApiService apiService = ApiClient.getApiService();
        Call<EssayAnswerResponse> call = apiService.submitEssayAnswer(request);

        call.enqueue(new Callback<EssayAnswerResponse>() {
            @Override
            public void onResponse(Call<EssayAnswerResponse> call, Response<EssayAnswerResponse> response) {
                // Clear submission in progress flag after response
                isEssaySubmissionInProgress = false;
                // Update dialog button state if dialog is open
                updateDialogConfirmButtonState();
                
                if (response.isSuccessful() && response.body() != null) {
                    EssayAnswerResponse essayResponse = response.body();
                    Log.d("TestStartActivity", "Essay answer submitted successfully for question: " + questionId);
                    if (essayResponse.getAnswer() != null) {
                        Log.d("TestStartActivity", "Server response - Score: " + essayResponse.getAnswer().getScore());
                    }
                } else {
                    Log.e("TestStartActivity", "Failed to submit essay answer: " + response.code() + " " + response.message());
                    // Note: We don't revert the previousAnswers map here, so if the user navigates again
                    // without changing the answer, it won't try to submit again unless they actually modify it
                }
            }

            @Override
            public void onFailure(Call<EssayAnswerResponse> call, Throwable t) {
                // Clear submission in progress flag after failure
                isEssaySubmissionInProgress = false;
                // Update dialog button state if dialog is open
                updateDialogConfirmButtonState();
                
                Log.e("TestStartActivity", "Error submitting essay answer: " + t.getMessage());
                // Note: Network failures don't revert the previousAnswers map either
                // This prevents spam retries on every navigation if there's a persistent network issue
            }
        });
    }

    private String getAnswerIdForQuestion(String questionId) {
        if (apiQuestions != null) {
            for (ApiQuestion apiQuestion : apiQuestions) {
                if (questionId.equals(apiQuestion.getId()) && 
                    apiQuestion.getEssay() != null && 
                    apiQuestion.getEssay().getAnswer() != null) {
                    return apiQuestion.getEssay().getAnswer().getId();
                }
            }
        }
        return null;
    }

    private void saveCurrentAnswerAndSubmitIfChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof QuestionFragment) {
            String currentAnswer = ((QuestionFragment) fragment).getCurrentAnswer();
            boolean isMarked = ((QuestionFragment) fragment).isMarked();
            
            Question currentQuestion = questions.get(currentQuestionIndex);
            String questionId = currentQuestion.getId();
            
            // Check if this is an essay question and answer has changed
            if ("ESSAY".equals(currentQuestion.getQuestionType())) {
                String previousAnswer = previousAnswers.get(questionId);
                if (previousAnswer == null) {
                    previousAnswer = "";
                }
                
                // Only submit if answer has changed
                if (!currentAnswer.equals(previousAnswer)) {
                    Log.d("TestStartActivity", "Answer changed for question " + questionId + ", submitting to server");
                    Log.d("TestStartActivity", "Previous: '" + previousAnswer + "' -> Current: '" + currentAnswer + "'");
                    submitEssayAnswerToServer(questionId, currentAnswer);
                    // Update the previous answer tracker
                    previousAnswers.put(questionId, currentAnswer);
                } else {
                    Log.d("TestStartActivity", "No change in answer for question " + questionId + ", skipping submission");
                }
            } else {
                Log.d("TestStartActivity", "Question " + questionId + " is not an essay question (type: " + currentQuestion.getQuestionType() + "), skipping API submission");
            }
            
            // Save answer to local data structure
            updateAnswerInData(questionId, currentAnswer, isMarked);
        }
    }    private void finishTest() {
        
        // Show completion message
        Toast.makeText(this, "Test completed!", Toast.LENGTH_SHORT).show();
        
        // Navigate to result screen
        Intent intent = new Intent(this, TestResultActivity.class);
        intent.putExtra("PARTICIPANT_ID", participantId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }    @SuppressWarnings("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Prevent user from going back during test
        Toast.makeText(this, "Cannot go back during test", Toast.LENGTH_SHORT).show();
        // Intentionally not calling super.onBackPressed() to prevent going back
    }
    
    private void updateDialogConfirmButtonState() {
        if (dialogConfirmButton != null) {
            dialogConfirmButton.setEnabled(!isEssaySubmissionInProgress);
            if (isEssaySubmissionInProgress) {
                dialogConfirmButton.setText("Submitting...");
            } else {
                dialogConfirmButton.setText("Finish Test");
            }
        }
    }
}
