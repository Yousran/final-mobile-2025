package com.example.finalmobile2025;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.example.finalmobile2025.api.ApiClient;
import com.example.finalmobile2025.api.ApiService;
import com.example.finalmobile2025.models.TestDetailsResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private TextInputEditText etJoinCode;
    private MaterialButton btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        initViews();
        setupClickListeners();
    }
      private void initViews() {
        etJoinCode = findViewById(R.id.et_join_code);
        btnJoin = findViewById(R.id.btn_join);
        
        // Set up input filters to ensure uppercase and 6 character limit
        InputFilter uppercaseFilter = new InputFilter.AllCaps();
        InputFilter lengthFilter = new InputFilter.LengthFilter(6);
        etJoinCode.setFilters(new InputFilter[]{uppercaseFilter, lengthFilter});
        
        // Add TextWatcher to handle real-time input validation
        etJoinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear any previous errors when user starts typing
                if (s.length() > 0) {
                    etJoinCode.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ensure text is uppercase (additional safety)
                String upperText = s.toString().toUpperCase();
                if (!s.toString().equals(upperText)) {
                    etJoinCode.removeTextChangedListener(this);
                    etJoinCode.setText(upperText);
                    etJoinCode.setSelection(upperText.length());
                    etJoinCode.addTextChangedListener(this);
                }
            }
        });
    }      private void setupClickListeners() {
        btnJoin.setOnClickListener(v -> {
            String joinCode = etJoinCode.getText().toString().trim();
            
            if (joinCode.isEmpty()) {
                etJoinCode.setError("Please enter a join code");
                return;
            }
            
            if (joinCode.length() != 6) {
                etJoinCode.setError("Join code must be exactly 6 characters");
                return;
            }
            
            // Fetch test details from endpoint 2
            fetchTestDetails(joinCode);
        });
    }
    
    private void fetchTestDetails(String joinCode) {
        ApiService apiService = ApiClient.getApiService();
        Call<TestDetailsResponse> call = apiService.getTestDetails(joinCode);
        
        // Show loading (disable button)
        btnJoin.setEnabled(false);
        btnJoin.setText("Joining...");
        
        call.enqueue(new Callback<TestDetailsResponse>() {
            @Override
            public void onResponse(Call<TestDetailsResponse> call, Response<TestDetailsResponse> response) {
                // Re-enable button
                btnJoin.setEnabled(true);
                btnJoin.setText("Join Room");
                
                if (response.isSuccessful() && response.body() != null) {
                    TestDetailsResponse testDetails = response.body();
                    Log.d("HomeActivity", "Test details fetched: " + testDetails.getTitle());
                    
                    // Navigate to TestActivity with test details
                    navigateToTestActivity(testDetails);
                } else {
                    Log.e("HomeActivity", "Error response: " + response.code());
                    if (response.code() == 404) {
                        etJoinCode.setError("Test not found. Please check the join code.");
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to join room. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TestDetailsResponse> call, Throwable t) {
                // Re-enable button
                btnJoin.setEnabled(true);
                btnJoin.setText("Join Room");
                
                Log.e("HomeActivity", "Network error: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
      private void navigateToTestActivity(TestDetailsResponse testDetails) {
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("JOIN_CODE", testDetails.getJoinCode());
        intent.putExtra("TEST_TITLE", testDetails.getTitle());
        intent.putExtra("TEST_DESCRIPTION", testDetails.getDescription());
        intent.putExtra("TEST_DURATION", testDetails.getTestDuration());
        intent.putExtra("ACCEPT_RESPONSES", testDetails.isAcceptResponses());
        startActivity(intent);
        
        // Show confirmation toast
        Toast.makeText(this, "Joined room: " + testDetails.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
