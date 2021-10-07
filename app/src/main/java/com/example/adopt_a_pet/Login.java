package com.example.adopt_a_pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        ProgressBar pb =  findViewById(R.id.loadingProgress);

        TextView regNow = findViewById(R.id.RegNow);
        Button login = findViewById(R.id.Login);
        TextView mEmail = findViewById(R.id.emailInput);
        TextView mPass = findViewById(R.id.passwordInput);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView readOnly = findViewById(R.id.readonly);

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "Welcome Back !!!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(intent);
        }

        readOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    pb.setVisibility(View.VISIBLE);

                    String email = mEmail.getText().toString().trim();
                    String password = mPass.getText().toString().trim();

                    if(TextUtils.isEmpty(email))
                    {
                        mEmail.setError("Enter Email ID");
                        return;
                    }
                    if(TextUtils.isEmpty(password))
                    {
                        mPass.setError("Enter Password");
                        return;
                    }



                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        pb.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "onComplete: Email Sign-In Complete");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        pb.setVisibility(View.INVISIBLE);
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });

        regNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , Register.class);
                startActivity(intent);
                finish();
            }
        });
    }
}