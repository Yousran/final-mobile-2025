package com.example.finalmobile2025;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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
    }
    
    private void setupClickListeners() {
        btnJoin.setOnClickListener(v -> {
            String joinCode = etJoinCode.getText().toString().trim();
            
            if (joinCode.isEmpty()) {
                etJoinCode.setError("Please enter a join code");
                return;
            }
            
            // Navigate to TestActivity
            navigateToTestActivity(joinCode);
        });
    }
    
    private void navigateToTestActivity(String joinCode) {
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("JOIN_CODE", joinCode);
        startActivity(intent);
        
        // Show confirmation toast
        Toast.makeText(this, "Joining room: " + joinCode, Toast.LENGTH_SHORT).show();
    }
}
