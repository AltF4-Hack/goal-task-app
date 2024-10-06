package com.altf4.journey.view;

import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Patterns;
import android.widget.Toast;

import com.altf4.journey.R;
import com.altf4.journey.entity.Updatable;
import com.altf4.journey.entity.User;
import com.altf4.journey.network.ResponseContainer;
import com.altf4.journey.network.ServerTalker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.StandardCharsets;

public class CreateAccountActivity extends AppCompatActivity implements Updatable {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordReInput;

    private String firstName;
    private String lastName;
    private String email;
    private String firstPassword;
    private String secondPassword;

    private Button register;
    private FirebaseAuth auth;

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

        FirebaseApp.initializeApp(CreateAccountActivity.this);
        auth = FirebaseAuth.getInstance();

        register = findViewById(R.id.createAccountBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = email;
                String text_password = firstPassword;

                if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)) {
                    Toast.makeText(CreateAccountActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else if (text_password.length() < 6) {
                    Toast.makeText(CreateAccountActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(text_email, text_password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void createFocusListeners() {
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        emailInput = (EditText) findViewById(R.id.usernameInput);
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
            email = input.getText().toString();
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
        if (firstName == null || lastName == null || email == null || firstPassword == null || secondPassword == null) {
            // TODO show error message
            Toast.makeText(CreateAccountActivity.this, "Invalid fields", Toast.LENGTH_SHORT).show();
        } else {
            User user = User.getInstance(email, firstName, lastName, firstPassword);
            // TODO add the user to the database
//            ServerTalker.initRequestQueue(this.getApplicationContext());
            ServerTalker.saveNewUser(user, new ResponseContainer(this));


            // redirect to login page
            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
        }
    }

    public void onLoginHypertextClick(View view) {
        // redirect to create account page
        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean updateField(ResponseContainer publisher, String fieldValue) {
        // TODO display the error message
        return true;
    }

}