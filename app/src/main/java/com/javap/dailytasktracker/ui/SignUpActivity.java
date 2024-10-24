package com.javap.dailytasktracker.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.javap.dailytasktracker.R;
import com.javap.dailytasktracker.ViewModel.ClientsViewModel;

public class SignUpActivity extends AppCompatActivity {
    private ClientsViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signUpButton = findViewById(R.id.signUpButton);

        userViewModel = new ViewModelProvider(this).get(ClientsViewModel.class);

        userViewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        userViewModel.getLoginFailed().observe(this, signUpFailed -> {
            if (signUpFailed) {
                Toast.makeText(this, "Sign Up Failed, Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        signUpButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            userViewModel.signUpUser(email, password);
        });
    }
}
