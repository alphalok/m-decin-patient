package com.example.authentifationapp;

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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail,editTextPassword;
    private TextView testViewRegister,testViewResetPassword;
    private Button loginBtn;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail =findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        testViewRegister=findViewById(R.id.testViewRegister);
        testViewResetPassword=findViewById(R.id.testViewResetPassword);
        loginBtn=findViewById(R.id.loginBtn);
        progressBar=findViewById(R.id.LoginProgressBar);

        auth = FirebaseAuth.getInstance();


        testViewRegister.setOnClickListener(this);
        testViewResetPassword.setOnClickListener(this);
        loginBtn.setOnClickListener(this);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.testViewRegister:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.testViewResetPassword:
                startActivity(new Intent(this,ResetPasswordActivity.class));
                break;
        }


    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("donner votre email");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("donner un email valide");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty() && password.length()<5){
            editTextPassword.setError("mot de pass doit avoir plus de 6 charactere");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Email Not verifid check your email", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "faild to sign in", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });




    }
}