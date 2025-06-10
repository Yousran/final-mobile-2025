package com.example.finalmobile2025;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalmobile2025.api.ApiClient;
import com.example.finalmobile2025.api.ApiService;
import com.example.finalmobile2025.database.ParticipantDAO;
import com.example.finalmobile2025.models.PoolingResponse;
import com.example.finalmobile2025.models.StartTestRequest;
import com.example.finalmobile2025.models.StartTestResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    private TextView tvTestTitle;
    private TextView tvTestDuration;
    private TextView tvQuestionCount;
    private TextView tvParticipantCount;    
    private MaterialButton btnStartTest;
    private MaterialButton btnConfirm; // Add this field
    private String joinCode;
    private String testTitle;    private String testDescription;
    private int testDuration;
    private boolean acceptResponses;
    private ParticipantDAO participantDAO;
    
    // Polling mechanism
    private Handler pollingHandler;
    private Runnable pollingRunnable;
    private static final int POLLING_INTERVAL = 5000; // 5 seconds@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        // Get data from intent
        joinCode = getIntent().getStringExtra("JOIN_CODE");
        testTitle = getIntent().getStringExtra("TEST_TITLE");
        testDescription = getIntent().getStringExtra("TEST_DESCRIPTION");        
        testDuration = getIntent().getIntExtra("TEST_DURATION", 0);
        acceptResponses = getIntent().getBooleanExtra("ACCEPT_RESPONSES", false);
        
        participantDAO = new ParticipantDAO(this);
        
        initViews();
        setupTestInfo();
        setupClickListeners();
        setupPolling();
    }
    
    private void initViews() {
        tvTestTitle = findViewById(R.id.tv_test_title);
        tvTestDuration = findViewById(R.id.tv_test_duration);
        tvQuestionCount = findViewById(R.id.tv_question_count);
        tvParticipantCount = findViewById(R.id.tv_participant_count);
        btnStartTest = findViewById(R.id.btn_start_test);
    }    private void setupTestInfo() {
        // Set test information from fetched data
        if (testTitle != null && !testTitle.isEmpty()) {
            tvTestTitle.setText(testTitle);
        } else {
            String fallbackTitle = joinCode != null ? "Room: " + joinCode : "Test Room";
            tvTestTitle.setText(fallbackTitle);
        }
        
        // Set test duration
        if (testDuration > 0) {
            tvTestDuration.setText(testDuration + " minutes");
        } else {
            tvTestDuration.setText("Duration not set");
        }
        
        // Initial values for question and participant counts (will be updated by polling)
        tvQuestionCount.setText("0 Questions");
        tvParticipantCount.setText("0 Participants");
    }
    
    private void setupClickListeners() {
        btnStartTest.setOnClickListener(v -> {
            // Show confirm start dialog
            showConfirmStartDialog();
        });
    }
    
    private void showConfirmStartDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_start, null);
          // Get dialog components
        TextInputEditText etUsername = dialogView.findViewById(R.id.et_username);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
        // Create and configure the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        // Make dialog background transparent for custom rounded corners
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        
        // Set up button click listeners
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (username.isEmpty()) {
                etUsername.setError("Please enter your name");
                return;
            }

            // Start the actual test
            startTest(username);
            dialog.dismiss();
        });
        
        dialog.show();
    }      private void startTest(String username) {
        // Show loading state
        btnConfirm.setEnabled(false);
        btnConfirm.setText("Starting...");
        btnStartTest.setEnabled(false);
        btnStartTest.setText("Starting...");
        
        // Create request
        StartTestRequest request = new StartTestRequest(username);
        
        // Make API call
        ApiService apiService = ApiClient.getApiService();
        Call<StartTestResponse> call = apiService.startTest(joinCode, request);
        
        call.enqueue(new Callback<StartTestResponse>() {
            @Override
            public void onResponse(Call<StartTestResponse> call, Response<StartTestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StartTestResponse startResponse = response.body();
                    String participantId = startResponse.getParticipantId();
                    
                    // Store participant ID in SQLite
                    long result = participantDAO.insertParticipant(participantId, username, joinCode);
                    
                    if (result != -1) {
                        // Successfully stored, navigate to test activity
                        Intent intent = new Intent(TestActivity.this, TestStartActivity.class);
                        intent.putExtra("PARTICIPANT_ID", participantId);
                        startActivity(intent);
                        finish(); // Close this activity
                    } else {
                        Toast.makeText(TestActivity.this, "Failed to save participant data", Toast.LENGTH_SHORT).show();
                        resetConfirmButton();
                    }
                } else {
                    Toast.makeText(TestActivity.this, "Failed to start test: " + response.code(), Toast.LENGTH_SHORT).show();
                    resetConfirmButton();
                }
            }
            
            @Override
            public void onFailure(Call<StartTestResponse> call, Throwable t) {
                Toast.makeText(TestActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetConfirmButton();
            }
        });
    }
    
    private void resetConfirmButton() {
        btnConfirm.setEnabled(true);
        btnConfirm.setText("Confirm");
        btnStartTest.setEnabled(true);
        btnStartTest.setText("Start Test");
    }
    
    private void setupPolling() {
        pollingHandler = new Handler(Looper.getMainLooper());
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                fetchPoolingData();
                // Schedule next polling
                pollingHandler.postDelayed(this, POLLING_INTERVAL);
            }
        };
        
        // Start polling immediately
        pollingHandler.post(pollingRunnable);
    }
    
    private void fetchPoolingData() {
        if (joinCode == null || joinCode.isEmpty()) {
            Log.w("TestActivity", "Join code is null or empty, skipping polling");
            return;
        }
        
        ApiService apiService = ApiClient.getApiService();
        Call<PoolingResponse> call = apiService.getTestPooling(joinCode);
        
        call.enqueue(new Callback<PoolingResponse>() {
            @Override
            public void onResponse(Call<PoolingResponse> call, Response<PoolingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PoolingResponse poolingData = response.body();
                    updateUI(poolingData);
                } else {
                    Log.e("TestActivity", "Polling error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PoolingResponse> call, Throwable t) {
                Log.e("TestActivity", "Polling network error: " + t.getMessage());
            }
        });
    }
    
    private void updateUI(PoolingResponse poolingData) {
        // Update question count
        int questionCount = poolingData.getQuestionCount();
        String questionText = questionCount + (questionCount == 1 ? " Question" : " Questions");
        tvQuestionCount.setText(questionText);
        
        // Update participant count
        int participantCount = poolingData.getParticipantCount();
        String participantText = participantCount + (participantCount == 1 ? " Participant" : " Participants");
        tvParticipantCount.setText(participantText);
        
        // Update button state based on acceptResponses
        boolean accepting = poolingData.isAcceptResponses();
        btnStartTest.setEnabled(accepting);
        if (!accepting) {
            btnStartTest.setText("Test Not Available");
        } else {
            btnStartTest.setText("Start Test");
        }
        
        Log.d("TestActivity", "UI Updated - Questions: " + questionCount + ", Participants: " + participantCount + ", Accepting: " + accepting);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop polling when activity is destroyed
        if (pollingHandler != null && pollingRunnable != null) {
            pollingHandler.removeCallbacks(pollingRunnable);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Stop polling when activity is paused
        if (pollingHandler != null && pollingRunnable != null) {
            pollingHandler.removeCallbacks(pollingRunnable);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Resume polling when activity is resumed
        if (pollingHandler != null && pollingRunnable != null) {
            pollingHandler.post(pollingRunnable);
        }
    }
}
