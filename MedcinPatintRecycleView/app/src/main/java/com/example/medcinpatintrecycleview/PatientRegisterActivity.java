package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class PatientRegisterActivity extends AppCompatActivity {

    private EditText editTextemail, editTextage, editTextFullName, editTextCIN, editTextPassword;
    private Button registerBtn;
    private ProgressBar patientProgressBar;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        ///////////instaciation des Ui element///////////////
        editTextage=findViewById(R.id.editTextPatientAge);
        editTextemail=findViewById(R.id.editTextPatRegistEmail);
        editTextPassword=findViewById(R.id.editTextTextPatRegistPassword);
        editTextFullName=findViewById(R.id.editTextPatientFullName);
        editTextCIN=findViewById(R.id.editTextPatientCIN);

        registerBtn=findViewById(R.id.patientRegistBtn);
        patientProgressBar=findViewById(R.id.patientProgressBar);

        auth=FirebaseAuth.getInstance();


        ////////////////// Receive referral Link //////////////////

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();


                            String referLink = deepLink.toString();
                            try {
                                referLink = referLink.substring(referLink.lastIndexOf("=") + 1);

                                String PatientCin = referLink.substring(0, referLink.indexOf("-"));

                                editTextCIN.setText(PatientCin);
                                editTextCIN.setEnabled(false);


                            } catch (Exception exception) {
                                Log.d("exception", exception.toString());
                            }

                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Patient Registration", "getDynamicLink:onFailure", e);
                    }
                });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser () {

        String email = editTextemail.getText().toString().trim();
        String age = editTextage.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cin = editTextCIN.getText().toString().trim();


        if (fullName.isEmpty()) {
            editTextFullName.setError("entrer votre nom");
            editTextFullName.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            editTextage.setError("donner votre age");
            editTextage.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextemail.setError("donner votre email");
            editTextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("donner un email valide");
            editTextemail.requestFocus();
            return;
        }

        if (password.isEmpty() && password.length() < 5) {
            editTextPassword.setError("mot de pass doit avoir plus de 6 charactere");
            editTextPassword.requestFocus();
            return;
        }

        patientProgressBar.setVisibility(View.VISIBLE);



        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(PatientRegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Patient patient = new Patient(fullName,age,email,cin);

                    FirebaseDatabase.getInstance().getReference("Users").child("patients").child(patient.getCin())
                            .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PatientRegisterActivity.this, " enregistrer", Toast.LENGTH_LONG).show();
                                        patientProgressBar.setVisibility(View.GONE);
                                        //startActivity( new Intent(PatientRegisterActivity.this,ProfileActivity.class));
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(PatientRegisterActivity.this, "non enregister", Toast.LENGTH_LONG).show();
                                        patientProgressBar.setVisibility(View.GONE);
                                        auth.signOut();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(PatientRegisterActivity.this, "Failed to register !! ", Toast.LENGTH_LONG).show();
                    patientProgressBar.setVisibility(View.GONE);
                }




            }
        });



    }


}