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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class RegisterUserActivity extends AppCompatActivity {
    private EditText userPhoneNum , patientUserCin, medecinUserNumOrdre , UserFullName,patientUserAge,patientUserEmail,patientUserPwd,patientUserConfirmPwd;
    private Button registerMedecinUser,registerPatientUser;
    private ProgressBar progressBar;
    private CheckBox isMedecin;

    private FirebaseAuth auth;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_register_user);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });



        userPhoneNum = findViewById(R.id.editTextUserNumTel);

        patientUserCin = findViewById(R.id.editTextPatientUserCIN);
        UserFullName =findViewById(R.id.editTextUserFullName);
        patientUserAge=findViewById(R.id.editTextPatientUserAge);
        patientUserEmail=findViewById(R.id.editTextPatientUserEmail);
        patientUserPwd=findViewById(R.id.editTextPatientUserPassword);
        patientUserConfirmPwd=findViewById(R.id.editTextPatientUserConfirmPassword);
        registerPatientUser=findViewById(R.id.patientUserRegisterBtn);



        medecinUserNumOrdre = findViewById(R.id.editTextUserNumOrdre);
        registerMedecinUser =findViewById(R.id.medecinUserRegisterBtn);
        progressBar = findViewById(R.id.userProgressBar);
        isMedecin = findViewById(R.id.isMedecin);

        auth=FirebaseAuth.getInstance();


        isMedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMedecin.isChecked()){
                    medecinUserNumOrdre.setVisibility(View.VISIBLE);
                    registerMedecinUser.setVisibility(View.VISIBLE);

                    registerPatientUser.setVisibility(View.GONE);
                    patientUserCin.setVisibility(View.GONE);
                    patientUserAge.setVisibility(View.GONE);
                    patientUserEmail.setVisibility(View.GONE);
                    patientUserAge.setVisibility(View.GONE);
                    patientUserPwd.setVisibility(View.GONE);
                    patientUserConfirmPwd.setVisibility(View.GONE);

                }
                else{
                    medecinUserNumOrdre.setVisibility(View.GONE);
                    registerMedecinUser.setVisibility(View.GONE);


                    registerPatientUser.setVisibility(View.VISIBLE);
                    patientUserCin.setVisibility(View.VISIBLE);
                    patientUserAge.setVisibility(View.VISIBLE);
                    patientUserEmail.setVisibility(View.VISIBLE);
                    patientUserAge.setVisibility(View.VISIBLE);
                    patientUserPwd.setVisibility(View.VISIBLE);
                    patientUserConfirmPwd.setVisibility(View.VISIBLE);
                }
            }
        });


        registerMedecinUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMedecin.isChecked()){
                    registerMedcin();
                }
            }
        });

        registerPatientUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMedecin.isChecked()){
                    registerPatient();
                }
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

    private void registerPatient() {
        String fullName = UserFullName.getText().toString().trim();
        String numTel = userPhoneNum.getText().toString().trim();
        String cin = patientUserCin.getText().toString().trim();
        String age = patientUserAge.getText().toString().trim();
        String email= patientUserEmail.getText().toString().trim();
        String pwd = patientUserPwd.getText().toString().trim();
        String confirmPwd=patientUserConfirmPwd.toString().trim();


        if(numTel.length() != 10){
            userPhoneNum.setError(getString(R.string.entrer_Numero_Valide));
            userPhoneNum.requestFocus();
            return;
        }

        if (cin.isEmpty()) {
            patientUserCin.setError(getString(R.string.entrer_CIN));
            //patientUserCin.requestFocus();
            return;
        }

        if(fullName.isEmpty()){
            UserFullName.setError(getString(R.string.entrer_nom_complet));
                    UserFullName.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            patientUserAge.setError(getString(R.string.entrer_age));
            patientUserAge.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            patientUserEmail.setError(getString(R.string.enter_email));
            patientUserEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            patientUserEmail.setError(getString(R.string.donner_email_valide));
            patientUserEmail.requestFocus();
            return;
        }

        if (pwd.isEmpty()) {
            patientUserPwd.setError(getString(R.string.entrer_password));
            patientUserPwd.requestFocus();
            return;
        }

        if(confirmPwd.contains(pwd)){
            patientUserConfirmPwd.setError(getString(R.string.match_passord));
            patientUserConfirmPwd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Patient patient = new Patient(fullName,age,email,cin,numTel);

                    FirebaseDatabase.getInstance().getReference("Users").child("patients").child(patient.getCin())
                            .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUserActivity.this, getString(R.string.enregistrer), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        //startActivity( new Intent(PatientRegisterActivity.this,MedecinProfileActivity.class));
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(RegisterUserActivity.this, getString(R.string.erreur), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.erreur), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void registerMedcin() {

        String numOrdre = medecinUserNumOrdre.getText().toString().trim();
        String fullName = UserFullName.getText().toString().trim();
        String numTel = userPhoneNum.getText().toString().trim();

        if(numTel.length()!=10){
            userPhoneNum.setError(getString(R.string.entrer_Numero_Valide));
            userPhoneNum.requestFocus();
            return;
        }

        if(numOrdre.isEmpty()){
            medecinUserNumOrdre.setError(getString(R.string.entrer_num_ordre));
            medecinUserNumOrdre.requestFocus();
            return;
        }

        if(fullName.isEmpty()){
            UserFullName.setError(getString(R.string.entrer_nom_complet));
            UserFullName.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Medcin medcin = new Medcin(fullName,numTel,numOrdre);
        FirebaseDatabase.getInstance().getReference("Users").child("newMedecin").child(medcin.getNumOrdre()).setValue(medcin).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.demande_being_verified), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else{
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.erreur), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

}