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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView LoginTextView,reg_button;
    private EditText editTextTextPersonName11, editTextTextEmailAddress,editTextTextPassword2;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        LoginTextView = (TextView) findViewById(R.id.LogintextView);
        LoginTextView.setOnClickListener(this);

        reg_button = (Button) findViewById(R.id.reg_button);
        reg_button.setOnClickListener(this);

        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword2= (EditText) findViewById(R.id.editTextTextPassword2);
        editTextTextPersonName11= (EditText) findViewById(R.id.editTextTextPersonName11);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);


    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.LogintextView:
          startActivity(new Intent(this, LoginActivity.class));
          break;
            case R.id.reg_button:
                registerUser();
                break;


        }
    }

    private void registerUser() {
        final String email = editTextTextEmailAddress.getText().toString().trim();
        String password = editTextTextPassword2.getText().toString().trim();
        final String name = editTextTextPersonName11.getText().toString().trim();


            if(email.isEmpty()){
                editTextTextEmailAddress.setError("Email is Required !");
                editTextTextEmailAddress.requestFocus();
                return;
            }


            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextTextEmailAddress.setError("Please provide a valid email ");
                editTextTextEmailAddress.requestFocus();
                return;
            }
        if(password.isEmpty()){
            editTextTextPassword2.setError("Password is required !");
            editTextTextPassword2.requestFocus();
            return;
        }

            if(password.length() < 6){
                editTextTextPassword2.setError("Min password length should be 6 characters !");
                editTextTextPassword2.requestFocus();
                return;
            }
        if(name.isEmpty()) {
            editTextTextPersonName11.setError("Name is required! ");
            editTextTextPersonName11.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(email, name);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegActivity.this, "User has been registered successfully ",Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);

                                        //redirect to Login Layout

                                    }else{
                                        Toast.makeText(RegActivity.this, "User has NOT been  registered successfully TRY AGAIN!",Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegActivity.this, "User has NOT been  registered successfully TRY AGAIN!",Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
        }




    }
