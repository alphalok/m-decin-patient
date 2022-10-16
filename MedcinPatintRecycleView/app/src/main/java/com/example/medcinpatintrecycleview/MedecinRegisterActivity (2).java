package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MedecinRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText editTextfullName, editTextNumTel,editTextemail,editTextpassword, editTextNumOrdre;
    private Button registerBtn;
    private ProgressBar progressBar;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_register);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });




        mAuth = FirebaseAuth.getInstance();

        editTextfullName=findViewById(R.id.editTextFullName);
        editTextNumTel =findViewById(R.id.editTextNumTel);
        editTextemail=findViewById(R.id.editTextRegistationEmail);
        editTextpassword=findViewById(R.id.editTextTextRegistratonPassword);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar=findViewById(R.id.progressBar);
        editTextNumOrdre =findViewById(R.id.editTextNumOrdre);

        registerBtn.setOnClickListener(this);


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
            case R.id.registerBtn:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String email = editTextemail.getText().toString().trim();
        Integer numTel =Integer.parseInt(editTextNumTel.getText().toString());
        String fullName = editTextfullName.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String numOrdre = editTextNumOrdre.getText().toString().trim();

        if(numOrdre.isEmpty()){
            editTextNumOrdre.setError("entrer votre CIN");
            editTextNumOrdre.requestFocus();
        }

        if(fullName.isEmpty()){
            editTextfullName.setError("entrer votre nom");
            editTextfullName.requestFocus();
            return;
        }
        if(editTextNumOrdre.getText().toString().trim().isEmpty()){
            editTextNumTel.setError("donner votre Numero d'ordre ");
            editTextNumTel.requestFocus();
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


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MedecinRegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Medcin medcin = new Medcin(fullName,numTel,email,numOrdre);


                    FirebaseDatabase.getInstance().getReference("Users").child("medecins").child(mAuth.getCurrentUser().getUid())
                            .setValue(medcin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MedecinRegisterActivity.this, "medcin enregister", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(MedecinRegisterActivity.this,MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(MedecinRegisterActivity.this, "non enregister", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                            });
                }
                else {
                    Toast.makeText(MedecinRegisterActivity.this, "Failed to regist'tetete! ", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });



    }
}