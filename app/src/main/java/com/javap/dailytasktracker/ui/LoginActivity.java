package com.javap.dailytasktracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.javap.dailytasktracker.R;
import com.javap.dailytasktracker.ViewModel.ClientsViewModel;
import com.javap.dailytasktracker.ui.SignUpActivity;

public class LoginActivity extends AppCompatActivity {
    private ClientsViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // FirebaseAuth.getInstance()
        FirebaseAuth.getInstance().signOut();

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        Button goToSignUpButton = findViewById(R.id.goToSignUpButton);

        userViewModel = new ViewModelProvider(this).get(ClientsViewModel.class);

        userViewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userViewModel.getLoginFailed().observe(this, loginFailed -> {
            if (loginFailed) {
                Toast.makeText(this, "Login failed, Check username and pass", Toast.LENGTH_SHORT).show();
            }
        });

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            userViewModel.loginUser(email, password);
        });

        goToSignUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
