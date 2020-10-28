package com.yeshen.myfitnessbud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText Login_email;
    private EditText Login_password;
    private Button Login_btn;

    private FirebaseAuth mAuth;
    private ProgressBar Login_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.Login_reg);
        register.setOnClickListener(this);

        Login_btn =(Button) findViewById(R.id.Login_btn);
        Login_btn.setOnClickListener(this);

        Login_email = (EditText) findViewById(R.id.Login_email);
        Login_password = (EditText) findViewById(R.id.Login_password);

        Login_progress = (ProgressBar) findViewById(R.id.Login_progress);

        mAuth = FirebaseAuth.   getInstance();
     }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login_reg:
                startActivity(new Intent(this,RegActivity.class));
                break;

            case R.id.Login_btn:
                userLogin();
                break;

        }

    }

    private void userLogin() {
        String email = Login_email.getText().toString().trim();
        String password = Login_password.getText().toString().trim();

        if (email.isEmpty()) {
            Login_email.setError("Email is Required");
            Login_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Login_email.setError("Please provide a valid email ");
            Login_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Login_password.setError("Password is required !");
            Login_password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Login_password.setError("Min password length is 6 characters !");
            Login_password.requestFocus();
            return;
        }
        Login_progress.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login Please Check your Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}