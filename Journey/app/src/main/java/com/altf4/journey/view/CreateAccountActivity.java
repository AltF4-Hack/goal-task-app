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
import com.altf4.journey.entity.User;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText passwordReInput;

    private String firstName;
    private String lastName;
    private String username;
    private String firstPassword;
    private String secondPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createFocusListeners();
    }

    private void createFocusListeners() {
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordReInput = (EditText) findViewById(R.id.passwordReInput);

        firstNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    onNameFocusExit(firstNameInput);
                }
            }
        });

        lastNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    onNameFocusExit(lastNameInput);
                }
            }
        });

        usernameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    onUsernameFocusExit(usernameInput);
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

        passwordReInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    onPasswordFocusExit(passwordReInput);
                }
            }
        });
    }

    public void onNameFocusExit(EditText input) {
        if (input.getText().toString().isEmpty()) {
            input.setError("Name is a required field");
        } else if (!input.getText().toString().matches("^[a-zA-Z]+$")) {
            input.setError("Name can only contain letters");
        } else {
            if (input.getId() == R.id.firstNameInput) {
                firstName = input.getText().toString();
            } else {
                lastName = input.getText().toString();
            }
        }
    }

    public void onUsernameFocusExit(EditText input) {
        if (input.getText().toString().isEmpty()) {
            input.setError("Email is a required field");
        } else if (!validateEmail(input.getText().toString())) {
            input.setError("Invalid email");
        } else {
            username = input.getText().toString();
        }
    }

    public void onPasswordFocusExit(EditText input) {
        if (input.getText().toString().isEmpty()) {
            input.setError("Password is a required field");
        } else {
            String hashed = Hashing.sha256()
                    .hashString(input.getText().toString(), StandardCharsets.UTF_8)
                    .toString();

            if (input.getId() == R.id.passwordInput) {
                firstPassword = hashed;
            } else {
                if (!hashed.equals(firstPassword)) {
                    input.setError("Passwords do not match");
                } else {
                    secondPassword = hashed;
                }
            }
        }
    }

    public void onCreateBtnClick(View view) {
        if (firstName == null || lastName == null || username == null || firstPassword == null || secondPassword == null) {
            // TODO show error message
        } else {
            User user = User.getInstance(username, firstName, lastName, firstPassword);
            // TODO add the user to the database

            // redirect to login page
            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
        }
    }

    private boolean validateEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }

        // TODO add the database validation check here

        return true;
    }

}