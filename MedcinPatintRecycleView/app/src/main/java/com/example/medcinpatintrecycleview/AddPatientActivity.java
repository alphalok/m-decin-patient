package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPatientActivity extends AppCompatActivity {
    private EditText editTextfullName,editTextage,editTextemail,editTextpassword,editTextCIN ;
    private Button registerBtn;
    private ProgressBar progressBar;

    private DatabaseReference reference;

    private String userCin;
    private ArrayList<Patient> patients;

    FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        userCin = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //userCin = getIntent().getStringExtra("USER_CIN");

        auth = FirebaseAuth.getInstance();

        editTextfullName=findViewById(R.id.editTextPatientFullName);
        editTextage=findViewById(R.id.editTextPatientAge);
        editTextemail=findViewById(R.id.editTextPatientRegistationEmail);
        editTextpassword=findViewById(R.id.editTextTextPatientRegistratonPassword);
        registerBtn = findViewById(R.id.patientRegisterBtn);
        progressBar=findViewById(R.id.patientProgressBar);
        editTextCIN=findViewById(R.id.editTextPatientCIN);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });
    }

    private void registerUser() {

        String email = editTextemail.getText().toString().trim();
        String age = editTextage.getText().toString().trim();
        String fullName = editTextfullName.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String cin = editTextCIN.getText().toString().trim();

        if(cin.isEmpty()){
            editTextCIN.setError("entrer votre CIN");
            editTextCIN.requestFocus();
        }

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

        reference =FirebaseDatabase.getInstance().getReference("Users");
        //reference.child("medecins").child(userCin).child("medcinPatients").push().setValue(cin);

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(AddPatientActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Patient patient = new Patient(fullName,age,email,cin);

                    FirebaseDatabase.getInstance().getReference("Users").child("patients").child(patient.getCin())
                                    .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        reference.child("medecins").child(userCin).child("medcinPatients").push().setValue(cin);
                                        Toast.makeText(AddPatientActivity.this, "Patient enregistrer", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        startActivity( new Intent(AddPatientActivity.this,ProfileActivity.class));
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(AddPatientActivity.this, "non enregister", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });







                }
                else{
                    Toast.makeText(AddPatientActivity.this, "Failed to regist'tetete! ", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }




            }
        });

/*
        FirebaseDatabase.getInstance().getReference("Users").child("patients").child(patient.getCin()).setValue(patient).addOnCompleteListener(this,new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reference.child("medecins").child(userCin).child("medcinPatients").push().setValue(cin);

                    Toast.makeText(AddPatientActivity.this, "Patient enregistrer", Toast.LENGTH_LONG).show();
                    startActivity( new Intent(AddPatientActivity.this,ProfileActivity.class));
                    finish();


                }
            }
        });

 */



    }

}