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

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();



        setContentView(R.layout.activity_register);
        TextView mEmail = findViewById(R.id.emailInput);
        TextView mPass = findViewById(R.id.passwordInput);
        Button register = findViewById(R.id.registerButton);
        TextView mName = findViewById(R.id.nameInput);
        TextView mconPass = findViewById(R.id.conPass);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView regLogin = findViewById(R.id.RegLogin);
        FirebaseUser user = mAuth.getCurrentUser();
        ProgressBar pb =  findViewById(R.id.loadingProgress);



        register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String email = mEmail.getText().toString().trim();
                    String password = mPass.getText().toString().trim();
                    String name = mName.getText().toString();
                    String confirmPassword = mconPass.toString();


                    if (TextUtils.isEmpty(name)) {
                        mName.setError("Enter Name");
                        return;
                    }

                    if (TextUtils.isEmpty(email)) {
                        mEmail.setError("Enter Email ID");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        mPass.setError("Enter Password");
                        return;
                    }
                    if (TextUtils.isEmpty(confirmPassword)) {
                        mconPass.setError("Enter Confirmed Password");
                        return;
                    }
                    if (password.length() < 6) {
                        mPass.setError("Password should have more than 6 characters");
                        return;
                    }
                    pb.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        pb.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "Welcome " + name);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        pb.setVisibility(View.INVISIBLE);
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "no", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                }


            });

            regLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });

    }
}