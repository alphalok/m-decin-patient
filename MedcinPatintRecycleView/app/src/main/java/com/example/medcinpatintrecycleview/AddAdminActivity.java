package com.example.medcinpatintrecycleview;

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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class AddAdminActivity extends AppCompatActivity {

    private EditText adminEmail,adminPwd;
    private Button makeAdminBtn;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        adminEmail = findViewById(R.id.editTextAdminEmail);
        adminPwd =findViewById(R.id.editTextAdminPassword);
        makeAdminBtn = findViewById(R.id.makeAdminBtn);
        progressBar = findViewById(R.id.adminProgressBar);
        auth = FirebaseAuth.getInstance();

        makeAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeAdmin();

            }
        });





    }

    private void makeAdmin() {

        String email = adminEmail.getText().toString().trim().toLowerCase(Locale.ROOT);
        String password = adminPwd.getText().toString().trim();

        if(email.isEmpty()){
            adminEmail.setError("donner votre email");
            adminEmail.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            adminEmail.setError("donner un email valide");
            adminEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            adminPwd.setError("mot de pass doit avoir plus de 6 charactere");
            adminPwd.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(AddAdminActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Admin admin = new Admin(email,password);


                    FirebaseDatabase.getInstance().getReference("Users").child("admin").child(auth.getCurrentUser().getUid())
                            .setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddAdminActivity.this, "Admin Ajouter", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(AddAdminActivity.this,MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(AddAdminActivity.this, "Non ajouter", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                            });
                }
                else {
                    Toast.makeText(AddAdminActivity.this, "Ajout échoué !! ", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }
}