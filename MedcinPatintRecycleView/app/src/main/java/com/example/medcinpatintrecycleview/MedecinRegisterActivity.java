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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class MedecinRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText editTextfullName, editTextNumTel,editTextemail,editTextpassword, editTextNumOrdre;
    private Button registerBtn;
    private ProgressBar progressBar;
    private View view;

    private String medecin_num_ordre;

    private DatabaseReference reference;


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



        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            medecin_num_ordre = bundle.getString("MEDECIN_NUM_ORDRE");

        }
        else {
            Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
        }

        editTextNumOrdre.setText(medecin_num_ordre);
        editTextNumOrdre.setEnabled(false);

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
       // Integer numTel =Integer.parseInt(editTextNumTel.getText().toString());
        String fullName = editTextfullName.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String numOrdre = editTextNumOrdre.getText().toString().trim();


        if(editTextNumOrdre.getText().toString().trim().isEmpty()){
            editTextNumTel.setError(getString(R.string.entrer_num_ordre));
            editTextNumTel.requestFocus();
            return;
        }

        if(fullName.isEmpty()){
            editTextfullName.setError(getString(R.string.entrer_nom_complet));
            editTextfullName.requestFocus();
            return;
        }
        if(editTextNumTel.getText().toString().trim().isEmpty()){
            editTextNumTel.setError(getString(R.string.entrer_Numero_Valide));
            editTextNumTel.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextemail.setError(getString(R.string.enter_email));
            editTextemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError(getString(R.string.donner_email_valide));
            editTextemail.requestFocus();
            return;
        }

        if(password.isEmpty() && password.length()<5){
            editTextpassword.setError(getString(R.string.entrer_password));
            editTextpassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MedecinRegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Medcin medcin = new Medcin(fullName,Integer.parseInt(editTextNumTel.getText().toString()),email,numOrdre);


                    FirebaseDatabase.getInstance().getReference("Users").child("medecins").child(mAuth.getCurrentUser().getUid())
                            .setValue(medcin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MedecinRegisterActivity.this, getString(R.string.enregistrer), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(MedecinRegisterActivity.this,MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(MedecinRegisterActivity.this, getString(R.string.erreur), Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    finish();
                                }

                            });
                }
                else {
                    Toast.makeText(MedecinRegisterActivity.this, getString(R.string.erreur), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });



    }
}