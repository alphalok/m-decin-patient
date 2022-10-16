package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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

    private EditText editTextemail, editTextage, editTextFullName, editTextCIN, editTextPassword,editTextNumTelephone,editTextConfirmPassword;
    private Button registerBtn;
    private ProgressBar patientProgressBar;

    private FirebaseAuth auth;
    private String medcinId,patient_cin;
    private View view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_patient_register);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });





        ///////////instaciation des Ui element///////////////
        editTextage=findViewById(R.id.editTextPatientAge);
        editTextemail=findViewById(R.id.editTextPatRegistEmail);
        editTextPassword=findViewById(R.id.editTextTextPatRegistPassword);
        editTextFullName=findViewById(R.id.editTextPatientNumTel);
        editTextCIN=findViewById(R.id.editTextPatientCIN);
        editTextNumTelephone=findViewById(R.id.editTextTextNTel);

        registerBtn=findViewById(R.id.patientRegistBtn);
        patientProgressBar=findViewById(R.id.patientProgressBar);
        editTextConfirmPassword=findViewById(R.id.editTextTextPatConfRegistPassword);

        auth=FirebaseAuth.getInstance();


        ////////////////// Receive referral Link //////////////////

        medcinId = getIntent().getStringExtra("MEDECIN_NUM_ORDRE");

        patient_cin = getIntent().getStringExtra("MEDECIN_NUM_ORDRE");

        editTextCIN.setText(patient_cin);
        editTextCIN.setEnabled(false);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            view.setSystemUiVisibility(hideSystemUi());
        }

    }

    private int hideSystemUi(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    private void registerUser () {

        String email = editTextemail.getText().toString().trim();
        String age = editTextage.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cin = editTextCIN.getText().toString().trim();
        String numTel =editTextNumTelephone.getText().toString().trim();

        String confirmPwd=editTextConfirmPassword.toString().trim();



        if (cin.isEmpty()) {
            editTextCIN.setError("entrer votre CIN");
            editTextCIN.requestFocus();
            return;
        }

        if(numTel.length()!=10){
            editTextNumTelephone.setError("Entrer un Numero Valide");
            editTextNumTelephone.requestFocus();
            return;
        }


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
        if (confirmPwd.isEmpty()) {
            editTextConfirmPassword.setError("confirmer votre mot de pass");
            editTextConfirmPassword.requestFocus();
            return;
        }
        if(confirmPwd.contains(password)){
            editTextConfirmPassword.setError("entrer un mot de pass valide");
            editTextConfirmPassword.requestFocus();
            return;
        }

        patientProgressBar.setVisibility(View.VISIBLE);



        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(PatientRegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Patient patient = new Patient(fullName,age,email,cin,numTel);

                    FirebaseDatabase.getInstance().getReference("Users").child("patients").child(patient.getCin())
                            .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PatientRegisterActivity.this, " enregistrer", Toast.LENGTH_LONG).show();
                                        patientProgressBar.setVisibility(View.GONE);
                                        FirebaseDatabase.getInstance().getReference("Users").child("patients").child(patient.getCin()).child("patientMedcins").push().setValue(medcinId);

                                        //startActivity( new Intent(PatientRegisterActivity.this,MedecinProfileActivity.class));
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(PatientRegisterActivity.this, "non enregister", Toast.LENGTH_LONG).show();
                                        patientProgressBar.setVisibility(View.GONE);
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