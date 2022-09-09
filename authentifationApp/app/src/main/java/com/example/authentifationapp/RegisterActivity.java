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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText editTextfullName,editTextage,editTextemail,editTextpassword;
    private Button registerBtn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextfullName=findViewById(R.id.editTextFullName);
        editTextage=findViewById(R.id.editTextAge);
        editTextemail=findViewById(R.id.editTextRegistationEmail);
        editTextpassword=findViewById(R.id.editTextTextRegistratonPassword);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar=findViewById(R.id.progressBar);

        registerBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtn:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String email = editTextemail.getText().toString().trim();
        String age = editTextage.getText().toString().trim();
        String fullName = editTextfullName.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextfullName.setError("entrer votre nom");
            editTextfullName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            editTextage.setError("donner votre age");
            editTextage.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextemail.setError("donner votre email");
            editTextemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("donner un email valide");
            editTextemail.requestFocus();
            return;
        }

        if(password.isEmpty() && password.length()<5){
            editTextpassword.setError("mot de pass doit avoir plus de 6 charactere");
            editTextpassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    User user = new User(fullName,age,email);


                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, " enregister", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "non enregister", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Failed to regist'tetete! ", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }


            }
        });




    }
}