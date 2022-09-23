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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail,editTextPassword;
    private TextView testViewRegister,testViewResetPassword;
    private Button loginBtn;
    private ProgressBar progressBar;

    private RadioGroup userStatus;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private Boolean  isMedecin = true;

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

        userStatus = findViewById(R.id.userStatus);

        auth = FirebaseAuth.getInstance();


        testViewRegister.setOnClickListener(this);
        testViewResetPassword.setOnClickListener(this);
        loginBtn.setOnClickListener(this);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.testViewRegister:
                startActivity(new Intent(MainActivity.this,RegisterUserActivity.class));
                break;
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.testViewResetPassword:
                startActivity(new Intent(MainActivity.this,ResetPasswordActivity.class));
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

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child("patients");


                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(checkUserIfIsPatient(email,snapshot) == true){

                                if(user.isEmailVerified()){
                                    if(userStatus.getCheckedRadioButtonId() == R.id.PatientBtn  ){

                                        for(DataSnapshot ds : snapshot.getChildren()){
                                            Patient patient = ds.getValue(Patient.class);
                                            if(patient.getEmai().equals(auth.getCurrentUser().getEmail())){

                                                Intent intent =new Intent(MainActivity.this,PatientProfileActivity.class);
                                                intent.putExtra("PATIENT_CIN",patient.getCin());
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "ce nest pas un medcin", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    user.sendEmailVerification();
                                    Toast.makeText(MainActivity.this, "Email Not verifid check your email", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);

                            }
                            else{
                                    if(userStatus.getCheckedRadioButtonId() == R.id.MedecinBtn  ){
                                        startActivity(new Intent(MainActivity.this, MedecinProfileActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "ce nest pas un patient", Toast.LENGTH_SHORT).show();
                                    }

                                progressBar.setVisibility(View.GONE);


                            }
                            Log.d("tag",isMedecin.toString());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    if((userStatus.getCheckedRadioButtonId() == R.id.MedecinBtn && !isMedecin)||(isMedecin && userStatus.getCheckedRadioButtonId() == R.id.PatientBtn)){
                        // Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "faild to sign in", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            progressBar.setVisibility(View.VISIBLE);

            reference = FirebaseDatabase.getInstance().getReference().child("Users").child("patients");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(checkUserIfIsPatient(auth.getCurrentUser().getEmail(),snapshot) == true){

                        for(DataSnapshot ds : snapshot.getChildren()){
                            Patient patient = ds.getValue(Patient.class);
                            if(patient.getEmai().equals(auth.getCurrentUser().getEmail())){

                                Intent intent =new Intent(MainActivity.this,PatientProfileActivity.class);
                                intent.putExtra("PATIENT_CIN",patient.getCin());
                                startActivity(intent);
                                finish();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                    else{
                        startActivity(new Intent(MainActivity.this, MedecinProfileActivity.class));
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


/////////////////////// cheking i the uesr is a Patient or not /////////////////////////////
    public boolean checkUserIfIsPatient(String email,DataSnapshot dataSnapshot){
        Patient patient = new Patient();

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Log.d("Tag","cheking email existe : datasnapshot "+ ds);

            patient.setEmai(ds.getValue(Patient.class).getEmai());
            if(patient.getEmai().equals(email)){
                Log.d("tag ", "does exisste");
                return true;
            }
        }
        Log.d("tag ", "does not  exisste");
        return false;

    }


}