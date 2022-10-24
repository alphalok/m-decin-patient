package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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
    private CardView cardView;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private Boolean  isMedecin = true;

    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_main);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });



        cardView = findViewById(R.id.mainCardView);

        editTextEmail =findViewById(R.id.editTextAdminEmail);
        editTextPassword = findViewById(R.id.editTextAdminPassword);
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
            editTextEmail.setError(getString(R.string.enter_email));
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getString(R.string.donner_email_valide));
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty() && password.length()<5){
            editTextPassword.setError(getString(R.string.match_passord));
            editTextPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users");


                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(checkUserIfIsAdmin(email,snapshot)== true){
                                Intent intent =new Intent(MainActivity.this,AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if(checkUserIfIsPatient(email,snapshot) == true){

                                if(user.isEmailVerified()){
                                    if(userStatus.getCheckedRadioButtonId() == R.id.PatientBtn  ){

                                        for(DataSnapshot ds : snapshot.child("patients").getChildren()){
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
                                        Toast.makeText(MainActivity.this, getString(R.string.non_medcin), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    user.sendEmailVerification();
                                    Toast.makeText(MainActivity.this, getString(R.string.verify_your_email), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);

                            }
                            else{
                                    if(userStatus.getCheckedRadioButtonId() == R.id.MedecinBtn  ){
                                        startActivity(new Intent(MainActivity.this, MedecinProfileActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, getString(R.string.non_patient), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, getString(R.string.erreur), Toast.LENGTH_LONG).show();
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

            reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(checkUserIfIsAdmin(auth.getCurrentUser().getEmail(),snapshot)== true){
                        Intent intent =new Intent(MainActivity.this,AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(checkUserIfIsPatient(auth.getCurrentUser().getEmail(),snapshot) == true){

                        for(DataSnapshot ds : snapshot.child("patients").getChildren()){
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

        for(DataSnapshot ds : dataSnapshot.child("patients").getChildren()){
            patient.setEmai(ds.getValue(Patient.class).getEmai());
            if(patient.getEmai().equals(email)){
                return true;
            }
        }
        return false;
    }

    private boolean  checkUserIfIsAdmin(String email,DataSnapshot dataSnapshot){
        Admin admin = new Admin();
        for(DataSnapshot ds : dataSnapshot.child("admin").getChildren()){

            admin.setEmai(ds.getValue(Patient.class).getEmai());
            if(admin.getEmai().equals(email)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.quite_app));
        builder.setMessage(getString(R.string.quiter_application));

        builder.setPositiveButton(getString(R.string.quitter), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.rester), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}