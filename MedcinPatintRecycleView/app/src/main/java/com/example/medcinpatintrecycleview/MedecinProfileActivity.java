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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedecinProfileActivity extends AppCompatActivity {

    private Button addPatientBtn,singoutBtn;


    private DatabaseReference database;
    private PatientAdapter adapter ;
    private ArrayList<Patient> patients;
    private ArrayList<String> PatientCin;

    private EditText searchPatient;

    private FirebaseUser user;

    private RelativeLayout parent;
    private RecyclerView recyclerView;

    private FirebaseAuth auth;


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
        setContentView(R.layout.activity_profile);



        addPatientBtn = findViewById(R.id.addPatientBtn);
        singoutBtn = findViewById(R.id.singOutBtn);

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


        singoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MedecinProfileActivity.this,MainActivity.class));
                finish();

            }
        });


        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(MedecinProfileActivity.this,AddPatientActivity.class));
                startActivity(intent);
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



