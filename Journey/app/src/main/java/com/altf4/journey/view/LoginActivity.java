package com.altf4.journey.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Patterns;

import com.altf4.journey.R;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createFocusListeners();
    }

    private void createFocusListeners() {
        emailInput = findViewById(R.id.loginEmail);
        passwordInput = findViewById(R.id.loginPassword);

        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    onUsernameFocusExit(emailInput);
                }
            }
        });

        passwordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    onPasswordFocusExit(passwordInput);
                }
            }
        });
    }

    public void onUsernameFocusExit(EditText input) {
        if (input.getText().toString().isEmpty()) {
            input.setError("Email is a required field");
        } else if (!validateEmail(input.getText().toString())) {
            input.setError("Invalid email");
        } else {
            email = input.getText().toString();
        }
    }

    public void onPasswordFocusExit(EditText input) {
        if (input.getText().toString().isEmpty()) {
            input.setError("Password is a required field");
        } else {
            if (input.getText().length() > 8) {
                password = Hashing.sha256()
                        .hashString(input.getText().toString(), StandardCharsets.UTF_8)
                        .toString();
            } else {
                input.setError("Password must be at least 8 characters long");
            }
        }
    }

    public void onLoginBtnClick(View view) {
        if (email == null || password == null) {
            // TODO show error message (not enough fields full)
        } else if (!validateLogin(email, password)) {
            // TODO show error message (invalid credentials)
        } else {
            // TODO login the user
            // redirect to main page
            startActivity(new Intent(LoginActivity.this, ManageGoalsActivity.class));
        }
    }

    public void onSignUpHypertextClick(View view) {
        // redirect to create account page
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateLogin(String username, String password) {
        // TODO use the database authorization to check if the user exists and has the correct password
        return true;  // stub
    }

}