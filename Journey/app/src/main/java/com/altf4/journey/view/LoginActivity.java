package com.altf4.journey.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.altf4.journey.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;

    private String email;
    private String password;


    private FirebaseAuth auth;



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

        FirebaseApp.initializeApp(LoginActivity.this);
        auth = FirebaseAuth.getInstance();

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
        onUsernameFocusExit(usernameInput);
        onPasswordFocusExit(passwordInput);
        if (email == null || password == null) {
            // TODO show error message (not enough fields full)
            Toast.makeText(LoginActivity.this, "not enough fields filled", Toast.LENGTH_SHORT).show();
        } else if (!validateLogin(email, password)) {
            // TODO show error message (invalid credentials)
            Toast.makeText(LoginActivity.this, "Invalid Credentials ", Toast.LENGTH_SHORT).show();
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

    private boolean validateLogin(String email, String password) {
        // TODO use the database authorization to check if the user exists and has the correct password
        final boolean[] logged = new boolean[1];
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    logged[0] = true;
                } else {
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    logged[0] = false;
                }
            }
        });
        return false;
    }
}