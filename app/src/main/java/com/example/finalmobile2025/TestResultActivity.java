package com.example.finalmobile2025;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalmobile2025.api.ApiClient;
import com.example.finalmobile2025.api.ApiService;
import com.example.finalmobile2025.database.ParticipantDAO;
import com.example.finalmobile2025.models.TestResultResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestResultActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvScore;
    private TextView tvTestTitle;
    private MaterialButton btnBackToHome;
    private ParticipantDAO participantDAO;
    
    private String participantId;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        // Initialize ParticipantDAO
        participantDAO = new ParticipantDAO(this);

        // Get participant ID from intent
        participantId = getIntent().getStringExtra("PARTICIPANT_ID");
        if (participantId == null) {
            Toast.makeText(this, "No participant data found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        fetchTestResult();
    }    private void initViews() {
        tvUsername = findViewById(R.id.tv_username);
        tvScore = findViewById(R.id.tv_score);
        tvTestTitle = findViewById(R.id.tv_test_title);
        btnBackToHome = findViewById(R.id.btn_back_to_home);
        
        // Disable button and show loading state initially
        btnBackToHome.setEnabled(false);
        btnBackToHome.setText("Loading data...");
    }

    private void setupClickListeners() {
        btnBackToHome.setOnClickListener(v -> {
            // Navigate back to home and clear the task stack
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void fetchTestResult() {
        ApiService apiService = ApiClient.getApiService();
        Call<TestResultResponse> call = apiService.getTestResult(participantId);        call.enqueue(new Callback<TestResultResponse>() {
            @Override
            public void onResponse(Call<TestResultResponse> call, Response<TestResultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TestResultResponse result = response.body();
                    displayResult(result);
                    
                    // Delete participant data from local database after successful API response
                    deleteParticipantData();
                    
                } else {
                    Log.e("TestResultActivity", "Failed to fetch test result: " + response.code());
                    Toast.makeText(TestResultActivity.this, "Failed to load test result", Toast.LENGTH_SHORT).show();
                    
                    // Show fallback data or navigate back
                    showFallbackData();
                }
            }

            @Override
            public void onFailure(Call<TestResultResponse> call, Throwable t) {
                Log.e("TestResultActivity", "Network error: " + t.getMessage());
                Toast.makeText(TestResultActivity.this, "Network error occurred", Toast.LENGTH_SHORT).show();
                
                // Show fallback data
                showFallbackData();
            }
        });
    }    private void displayResult(TestResultResponse result) {
        if (result.getParticipant() != null) {
            tvUsername.setText(result.getParticipant().getUsername());
            
            // Format score to show as integer if it's a whole number
            double score = result.getParticipant().getScore();
            if (score == (int) score) {
                tvScore.setText(String.valueOf((int) score));
            } else {
                tvScore.setText(String.format("%.1f", score));
            }
        }

        if (result.getTest() != null) {
            tvTestTitle.setText(result.getTest().getTitle());
        }
        
        // Enable button and restore original text after data is loaded
        enableBackButton();
    }    private void showFallbackData() {
        // Show some default data if API call fails
        tvUsername.setText("Participant");
        tvScore.setText("--");
        tvTestTitle.setText("Test Completed");
        
        // Enable button even if API fails
        enableBackButton();
    }
    
    private void enableBackButton() {
        btnBackToHome.setEnabled(true);
        btnBackToHome.setText("Back to Home");
    }

    private void deleteParticipantData() {
        try {
            if (participantId != null && participantDAO != null) {
                participantDAO.deleteParticipant(participantId);
                Log.d("TestResultActivity", "Successfully deleted participant data for ID: " + participantId);
            }
        } catch (Exception e) {
            Log.e("TestResultActivity", "Error deleting participant data: " + e.getMessage());
            // Don't show error to user as this is cleanup operation
        }
    }
}
