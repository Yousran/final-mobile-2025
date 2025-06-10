package com.example.finalmobile2025;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class TestActivity extends AppCompatActivity {    private TextView tvTestTitle;
    private TextView tvTestDuration;
    private TextView tvQuestionCount;
    private TextView tvParticipantCount;
    private MaterialButton btnStartTest;
    private String joinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        // Get join code from intent
        joinCode = getIntent().getStringExtra("JOIN_CODE");
        
        initViews();
        setupTestInfo();
        setupClickListeners();
    }
    
    private void initViews() {
        tvTestTitle = findViewById(R.id.tv_test_title);
        tvTestDuration = findViewById(R.id.tv_test_duration);
        tvQuestionCount = findViewById(R.id.tv_question_count);
        tvParticipantCount = findViewById(R.id.tv_participant_count);
        btnStartTest = findViewById(R.id.btn_start_test);
    }
      private void setupTestInfo() {
        // Set test information
        String testTitle = joinCode != null ? "Room: " + joinCode : "test multiple select";
        tvTestTitle.setText(testTitle);
        tvTestDuration.setText("30 minutes");
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
        MaterialButton btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
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
    }
    
    private void startTest(String username) {
        // TODO: Implement test start functionality with username
        Toast.makeText(this, "Starting test for: " + username, Toast.LENGTH_SHORT).show();
    }
}
