package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedecinProfileActivity extends AppCompatActivity {



    private boolean clicked = false;

    private FloatingActionButton plus_btn,nouveauPatient_btn,signOut_btn;

    private DatabaseReference database;
    private PatientAdapter adapter ;
    private ArrayList<Patient> patients;
    private ArrayList<String> PatientCin;

    private EditText searchPatient;

    private FirebaseUser user;

    private RelativeLayout parent;
    private RecyclerView recyclerView;

    private FirebaseAuth auth;

    private View view;

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("voulez vous quitter l'application");
        builder.setMessage("etes vous sur de vouloir quitter l'application");

        builder.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MedecinProfileActivity.super.onBackPressed();
                finish();
            }
        });
        builder.setNegativeButton("Rester", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_profile);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });


        plus_btn = findViewById(R.id.addButton);
        signOut_btn = findViewById(R.id.signOut_btn);
        nouveauPatient_btn= findViewById(R.id.nouveauPatient_btn);

        recyclerView = findViewById(R.id.patientsRecycleView);
        parent = findViewById(R.id.relativeLayoutP);
        searchPatient= findViewById(R.id.searchPatient);
        database = FirebaseDatabase.getInstance().getReference("Users");

        patients= new ArrayList<Patient>();

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(this);
        adapter.setPatients(patients);
        recyclerView.setAdapter(adapter);

        PatientCin = medecinCinPatient(user.getUid());

        database.child("patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String cin : PatientCin){
                    if(snapshot.child(cin).exists()){
                        Patient patient = snapshot.child(cin).getValue(Patient.class);
                        patients.add(patient);
                    }
                    else{
                    }
                }
                adapter.setPatients(patients);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onAddButtonClicked();

            }
        });

        nouveauPatient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(MedecinProfileActivity.this,AddPatientActivity.class));
                startActivity(intent);
                finish();

            }
        });

        signOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MedecinProfileActivity.this,MainActivity.class));
                finish();


            }
        });

        searchPatient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtSearch(editable.toString());

            }
        });


    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;

    }

    private void setVisibility(boolean clicked) {
        if(!clicked){
            nouveauPatient_btn.setVisibility(View.VISIBLE);
            signOut_btn.setVisibility(View.VISIBLE);

        }
        else{
            nouveauPatient_btn.setVisibility(View.GONE);
            signOut_btn.setVisibility(View.GONE);
        }

    }
    private void setAnimation(boolean clicked){
        if(!clicked){
            nouveauPatient_btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim));
            signOut_btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim));
            plus_btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim));

        }else{
            nouveauPatient_btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim));
            signOut_btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim));
            plus_btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim));
        }

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

    private void txtSearch(String fullName){

        patients= new ArrayList<Patient>();
        adapter = new PatientAdapter(this);
        adapter.setPatients(patients);
        recyclerView.setAdapter(adapter);

        database.child("patients").orderByChild("fullname").startAt(fullName).endAt(fullName+"~").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    if(PatientCin.contains(patient.getCin())){
                        patients.add(patient);
                    }
                }
                adapter.setPatients(patients);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private ArrayList<String> medecinCinPatient(String medecinUid){
        ArrayList<String> patientCin = new ArrayList<String>();
        database.child("medecins").child(medecinUid).child("medcinPatients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    patientCin.add(String.valueOf(dataSnapshot.getValue()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return patientCin;
    }


}



